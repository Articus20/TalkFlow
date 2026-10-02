package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CallHistoryEntity
import com.example.data.local.SavedVocabEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.CallEngineState
import com.example.data.model.CallFeedback
import com.example.data.model.CoachingStrictness
import com.example.data.model.NativeIdiom
import com.example.data.model.PhonemeScore
import com.example.data.model.PracticeScenario
import com.example.data.model.ScenarioData
import com.example.data.model.VocabWord
import com.example.util.CostaRicaTime
import com.example.voice.CoachAndTranslator
import com.example.voice.CoachTip
import com.example.voice.VoiceEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

sealed class AppScreen {
    object Onboarding : AppScreen()
    object Dashboard : AppScreen()
    object Practice : AppScreen()
    object Progress : AppScreen()
    data class PreCallRoom(val scenario: PracticeScenario) : AppScreen()
    data class ActiveCall(val scenario: PracticeScenario) : AppScreen()
    data class Feedback(val feedback: CallFeedback) : AppScreen()
}

class TalkFlowViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val profileDao = db.userProfileDao()
    private val historyDao = db.callHistoryDao()
    private val vocabDao = db.savedVocabDao()

    val voiceEngine = VoiceEngine(application)

    // Current navigation screen
    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Dashboard)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // User Profile
    val userProfile: StateFlow<UserProfileEntity?> = profileDao.getUserProfileFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // Call History
    val callHistory: StateFlow<List<CallHistoryEntity>> = historyDao.getAllHistoryFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Saved Vocabulary
    val savedVocabulary: StateFlow<List<SavedVocabEntity>> = vocabDao.getAllVocabFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Costa Rica live formatted time
    private val _costaRicaTime = MutableStateFlow(CostaRicaTime.getCurrentFormattedTime())
    val costaRicaTime: StateFlow<String> = _costaRicaTime.asStateFlow()

    private val _costaRicaGreeting = MutableStateFlow(CostaRicaTime.getGreeting())
    val costaRicaGreeting: StateFlow<String> = _costaRicaGreeting.asStateFlow()

    // Pre-call configuration
    private val _selectedStrictness = MutableStateFlow(CoachingStrictness.BALANCED)
    val selectedStrictness: StateFlow<CoachingStrictness> = _selectedStrictness.asStateFlow()

    private val _selectedKeywords = MutableStateFlow<Set<String>>(emptySet())
    val selectedKeywords: StateFlow<Set<String>> = _selectedKeywords.asStateFlow()

    // Active Call State
    private val _callState = MutableStateFlow(CallEngineState.THINKING)
    val callState: StateFlow<CallEngineState> = _callState.asStateFlow()

    private val _currentTurnIndex = MutableStateFlow(0)
    val currentTurnIndex: StateFlow<Int> = _currentTurnIndex.asStateFlow()

    private val _currentBotText = MutableStateFlow("")
    val currentBotText: StateFlow<String> = _currentBotText.asStateFlow()

    private val _currentBotSpanish = MutableStateFlow("")
    val currentBotSpanish: StateFlow<String> = _currentBotSpanish.asStateFlow()

    private val _isTranslationVisible = MutableStateFlow(false)
    val isTranslationVisible: StateFlow<Boolean> = _isTranslationVisible.asStateFlow()

    private val _callSeconds = MutableStateFlow(0L)
    val callSeconds: StateFlow<Long> = _callSeconds.asStateFlow()

    private val _adaptiveSpeed = MutableStateFlow(0.95f)
    val adaptiveSpeed: StateFlow<Float> = _adaptiveSpeed.asStateFlow()

    private val _adaptiveVocabulary = MutableStateFlow("Intermediate")
    val adaptiveVocabulary: StateFlow<String> = _adaptiveVocabulary.asStateFlow()

    private val _coachTip = MutableStateFlow<CoachTip?>(null)
    val coachTip: StateFlow<CoachTip?> = _coachTip.asStateFlow()

    private val _userTranscriptHistory = MutableStateFlow<List<String>>(emptyList())
    val userTranscriptHistory: StateFlow<List<String>> = _userTranscriptHistory.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _isSpeakerOn = MutableStateFlow(true)
    val isSpeakerOn: StateFlow<Boolean> = _isSpeakerOn.asStateFlow()

    // Panic Mode BottomSheet State
    private val _isPanicModeOpen = MutableStateFlow(false)
    val isPanicModeOpen: StateFlow<Boolean> = _isPanicModeOpen.asStateFlow()

    private val _panicSpanishInput = MutableStateFlow("")
    val panicSpanishInput: StateFlow<String> = _panicSpanishInput.asStateFlow()

    private val _panicEnglishTranslation = MutableStateFlow("")
    val panicEnglishTranslation: StateFlow<String> = _panicEnglishTranslation.asStateFlow()

    // Active scenario reference
    private var activeScenario: PracticeScenario = ScenarioData.interview
    private var callTimerJob: Job? = null
    private var timeClockJob: Job? = null
    private var callStartTime: Long = 0L

    init {
        // Observe profile to route to onboarding if first launch
        viewModelScope.launch {
            val existing = profileDao.getUserProfile()
            if (existing == null || existing.username.isBlank()) {
                _currentScreen.value = AppScreen.Onboarding
            } else {
                checkAndUpdateDayReset(existing)
            }
        }

        // Setup VoiceEngine callbacks
        voiceEngine.onAiFinishedSpeaking = {
            // Once AI finishes speaking, the engine transitions to LISTENING and listens for the user!
            if (_callState.value == CallEngineState.SPEAKING) {
                _callState.value = CallEngineState.LISTENING
                voiceEngine.startListening()
            }
        }

        voiceEngine.onUserFinishedSpeaking = { transcript ->
            handleUserFinishedSpeaking(transcript)
        }

        // Costa Rica clock update loop
        startTimeClockLoop()
    }

    private fun startTimeClockLoop() {
        timeClockJob?.cancel()
        timeClockJob = viewModelScope.launch {
            while (true) {
                _costaRicaTime.value = CostaRicaTime.getCurrentFormattedTime()
                _costaRicaGreeting.value = CostaRicaTime.getGreeting()
                delay(15000L)
            }
        }
    }

    private suspend fun checkAndUpdateDayReset(profile: UserProfileEntity) {
        val todayKey = CostaRicaTime.getTodayDateKey()
        if (profile.todayDateString != todayKey) {
            val yesterdayKey = CostaRicaTime.getYesterdayDateKey()
            val newStreak = if (profile.lastCallDate == yesterdayKey) {
                profile.currentStreak
            } else if (profile.lastCallDate == todayKey) {
                profile.currentStreak
            } else {
                0 // Streak resets to 0 if yesterday was missed!
            }
            profileDao.insertOrUpdate(
                profile.copy(
                    todayDateString = todayKey,
                    todayMinutesPracticed = 0,
                    currentStreak = newStreak
                )
            )
        }
    }

    fun saveUserProfile(username: String, goals: String, dailyTargetMins: Int) {
        viewModelScope.launch {
            val existing = profileDao.getUserProfile()
            val todayKey = CostaRicaTime.getTodayDateKey()
            val profile = UserProfileEntity(
                id = 1,
                username = username.trim().ifBlank { "Maya" },
                goalsDescription = goals.trim().ifBlank { "Speak English confidently and fluently without anxiety in meetings and interviews." },
                dailyTargetMinutes = dailyTargetMins.coerceIn(3, 60),
                currentStreak = existing?.currentStreak ?: 0,
                bestStreak = existing?.bestStreak ?: 0,
                lastCallDate = existing?.lastCallDate ?: "",
                todayMinutesPracticed = existing?.todayMinutesPracticed ?: 0,
                todayDateString = todayKey
            )
            profileDao.insertOrUpdate(profile)
            _currentScreen.value = AppScreen.Dashboard
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun openPreCall(scenario: PracticeScenario) {
        activeScenario = scenario
        _selectedKeywords.value = scenario.targetKeywords.toSet()
        _currentScreen.value = AppScreen.PreCallRoom(scenario)
    }

    fun setStrictness(strictness: CoachingStrictness) {
        _selectedStrictness.value = strictness
    }

    fun toggleKeyword(keyword: String) {
        val current = _selectedKeywords.value.toMutableSet()
        if (current.contains(keyword)) {
            current.remove(keyword)
        } else {
            if (current.size < 6) current.add(keyword)
        }
        _selectedKeywords.value = current
    }

    fun addCustomKeyword(keyword: String) {
        val clean = keyword.trim().lowercase(Locale.ROOT)
        if (clean.isNotBlank() && clean.length in 2..20) {
            val current = _selectedKeywords.value.toMutableSet()
            if (current.size < 6) {
                current.add(clean)
                _selectedKeywords.value = current
            }
        }
    }

    // Call Lifecycle
    fun startCall(scenario: PracticeScenario) {
        activeScenario = scenario
        _currentTurnIndex.value = 0
        _callSeconds.value = 0L
        _adaptiveSpeed.value = 0.95f
        _adaptiveVocabulary.value = when (scenario.level) {
            com.example.data.model.ScenarioLevel.BASIC -> "Everyday"
            com.example.data.model.ScenarioLevel.INTERMEDIATE -> "Intermediate"
            com.example.data.model.ScenarioLevel.ADVANCED -> "Advanced"
        }
        _coachTip.value = null
        _userTranscriptHistory.value = emptyList()
        _isTranslationVisible.value = false
        _isMuted.value = false
        _isSpeakerOn.value = true
        _isPanicModeOpen.value = false
        voiceEngine.setMute(false)
        voiceEngine.setSpeaker(true)

        callStartTime = System.currentTimeMillis()
        _currentScreen.value = AppScreen.ActiveCall(scenario)

        // Start call duration timer
        callTimerJob?.cancel()
        callTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _callSeconds.value += 1L
            }
        }

        // Deliver initial bot turn
        playBotTurn(0)
    }

    private fun playBotTurn(turnIndex: Int) {
        if (turnIndex < activeScenario.turns.size) {
            val turn = activeScenario.turns[turnIndex]
            _currentBotText.value = turn.botEnglish
            _currentBotSpanish.value = turn.botSpanish
            _isTranslationVisible.value = false

            _panicSpanishInput.value = turn.panic.userSpanish
            _panicEnglishTranslation.value = turn.panic.translatedEnglish

            _callState.value = CallEngineState.SPEAKING
            voiceEngine.speak(turn.botEnglish, _adaptiveSpeed.value)
        } else {
            // Closing Turn
            _currentBotText.value = activeScenario.closingEnglish
            _currentBotSpanish.value = activeScenario.closingSpanish
            _isTranslationVisible.value = false

            _callState.value = CallEngineState.SPEAKING
            voiceEngine.speak(activeScenario.closingEnglish, _adaptiveSpeed.value)

            viewModelScope.launch {
                delay(3000L)
                _callState.value = CallEngineState.DONE
            }
        }
    }

    fun handleUserFinishedSpeaking(rawTranscript: String) {
        val transcript = rawTranscript.trim()
        if (transcript.isBlank()) {
            // If empty, return to listening after short pause
            _callState.value = CallEngineState.LISTENING
            voiceEngine.startListening()
            return
        }

        voiceEngine.stopListening()
        _callState.value = CallEngineState.THINKING

        // Analyze turn with coach
        val tip = CoachAndTranslator.analyzeTurn(
            text = transcript,
            strictness = _selectedStrictness.value.title,
            targetKeywords = _selectedKeywords.value.toList()
        )
        _coachTip.value = tip

        val history = _userTranscriptHistory.value.toMutableList()
        history.add(transcript)
        _userTranscriptHistory.value = history

        // Adaptive engine pacing & complexity adjustment
        val wordCount = transcript.split(Regex("\\s+")).size
        if (wordCount >= 6 && tip.isPositive) {
            _adaptiveSpeed.value = (_adaptiveSpeed.value + 0.05f).coerceAtMost(1.25f)
        } else if (wordCount < 4) {
            _adaptiveSpeed.value = (_adaptiveSpeed.value - 0.05f).coerceAtLeast(0.75f)
        }

        viewModelScope.launch {
            // Natural conversational thinking delay
            delay(1000L)

            val nextIndex = _currentTurnIndex.value + 1
            _currentTurnIndex.value = nextIndex
            playBotTurn(nextIndex)
        }
    }

    fun toggleSubtitleTranslation() {
        _isTranslationVisible.value = !_isTranslationVisible.value
    }

    fun toggleMute() {
        val newMute = !_isMuted.value
        _isMuted.value = newMute
        voiceEngine.setMute(newMute)
        if (newMute) {
            _callState.value = CallEngineState.WAITING
        } else if (_callState.value == CallEngineState.WAITING) {
            _callState.value = CallEngineState.LISTENING
            voiceEngine.startListening()
        }
    }

    fun toggleSpeaker() {
        val newSpeaker = !_isSpeakerOn.value
        _isSpeakerOn.value = newSpeaker
        voiceEngine.setSpeaker(newSpeaker)
    }

    // Panic Mode BottomSheet
    fun openPanicMode() {
        _isPanicModeOpen.value = true
    }

    fun closePanicMode() {
        _isPanicModeOpen.value = false
    }

    fun updatePanicSpanishInput(input: String) {
        _panicSpanishInput.value = input
        val translated = CoachAndTranslator.translateSpanishToEnglish(input)
        _panicEnglishTranslation.value = translated
    }

    fun usePanicSuggestion(suggestionText: String) {
        closePanicMode()
        // Injects response and triggers user finish speaking
        handleUserFinishedSpeaking(suggestionText)
    }

    fun endCallAndShowFeedback() {
        callTimerJob?.cancel()
        voiceEngine.stopListening()
        voiceEngine.stopTts()

        val duration = _callSeconds.value
        val answeredCount = _userTranscriptHistory.value.size
        val scenario = activeScenario

        viewModelScope.launch {
            val profile = profileDao.getUserProfile()
            val todayKey = CostaRicaTime.getTodayDateKey()
            val yesterdayKey = CostaRicaTime.getYesterdayDateKey()

            val currentStreak = profile?.currentStreak ?: 0
            val wasZeroStreak = currentStreak == 0

            // STREAK REQUIREMENT:
            // "que la racha inicie en 0 y que tenga que hacer una llamda para iniciar una racha"
            // If streak was 0, completing this call starts the streak at 1!
            // If user practiced on consecutive day, increment!
            val newStreak = if (profile?.lastCallDate == todayKey) {
                currentStreak.coerceAtLeast(1)
            } else if (profile?.lastCallDate == yesterdayKey) {
                currentStreak + 1
            } else {
                1 // Starts/restarts streak at 1 on completed call
            }

            val bestStreak = maxOf(profile?.bestStreak ?: 0, newStreak)
            val addedMinutes = maxOf(1, (duration / 60).toInt())
            val newTodayMinutes = (profile?.todayMinutesPracticed ?: 0) + addedMinutes

            if (profile != null) {
                profileDao.insertOrUpdate(
                    profile.copy(
                        currentStreak = newStreak,
                        bestStreak = bestStreak,
                        lastCallDate = todayKey,
                        todayMinutesPracticed = newTodayMinutes,
                        todayDateString = todayKey
                    )
                )
            }

            // Calculate scoring metrics
            val basePace = (82 + (answeredCount * 3)).coerceIn(60, 96)
            val baseClarity = (80 + if (_coachTip.value?.isPositive == true) 8 else 0).coerceIn(65, 95)
            val baseVocab = when (scenario.level) {
                com.example.data.model.ScenarioLevel.BASIC -> 82
                com.example.data.model.ScenarioLevel.INTERMEDIATE -> 88
                com.example.data.model.ScenarioLevel.ADVANCED -> 92
            }
            val baseConfidence = (80 + answeredCount * 4).coerceIn(65, 96)
            val baseKeywords = 86

            val overallScore = ((basePace + baseClarity + baseVocab + baseConfidence + baseKeywords) / 5)

            // Save call record
            historyDao.insert(
                CallHistoryEntity(
                    scenarioId = scenario.id,
                    scenarioTitle = scenario.title,
                    fluencyScore = overallScore,
                    durationSeconds = duration,
                    dateFormatted = CostaRicaTime.getFormattedDate(),
                    turnsCount = answeredCount
                )
            )

            val feedback = CallFeedback(
                scenarioId = scenario.id,
                scenarioTitle = scenario.title,
                fluencyScore = overallScore,
                deltaFromLast = if (wasZeroStreak) 6 else 4,
                paceScore = basePace,
                clarityScore = baseClarity,
                vocabScore = baseVocab,
                confidenceScore = baseConfidence,
                keywordScore = baseKeywords,
                headline = if (overallScore >= 85) "Calm and persuasive" else "Steady and fluent",
                subtitle = "Your pace stabilized as conversational complexity increased.",
                phonemes = listOf(
                    PhonemeScore("/θ/", "think", 68),
                    PhonemeScore("/r/", "role", 91),
                    PhonemeScore("/v/", "value", 84)
                ),
                nativeIdioms = scenario.idioms,
                targetWords = scenario.vocabulary,
                durationSeconds = duration,
                turnsAnswered = answeredCount,
                isFirstCallStreakStarter = wasZeroStreak
            )

            _currentScreen.value = AppScreen.Feedback(feedback)
        }
    }

    fun toggleSaveVocabWord(word: VocabWord) {
        viewModelScope.launch {
            val existing = savedVocabulary.value.any { it.word == word.word }
            if (existing) {
                vocabDao.deleteWord(word.word)
            } else {
                vocabDao.insert(
                    SavedVocabEntity(
                        word = word.word,
                        translationSpanish = word.translationSpanish,
                        scenarioId = activeScenario.id
                    )
                )
            }
        }
    }

    fun clearAllUserData() {
        viewModelScope.launch {
            historyDao.clearAll()
            vocabDao.clearAll()
            profileDao.insertOrUpdate(
                UserProfileEntity(
                    id = 1,
                    username = "",
                    goalsDescription = "",
                    dailyTargetMinutes = 10,
                    currentStreak = 0,
                    bestStreak = 0,
                    lastCallDate = "",
                    todayMinutesPracticed = 0,
                    todayDateString = CostaRicaTime.getTodayDateKey()
                )
            )
            _currentScreen.value = AppScreen.Onboarding
        }
    }

    override fun onCleared() {
        super.onCleared()
        callTimerJob?.cancel()
        timeClockJob?.cancel()
        voiceEngine.release()
    }
}
