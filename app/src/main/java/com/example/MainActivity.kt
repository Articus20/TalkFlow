package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.ScenarioData
import com.example.ui.screens.ActiveCallScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PanicModeSheet
import com.example.ui.screens.PostCallFeedbackScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.PreCallRoomScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.theme.BorderLine
import com.example.ui.theme.CoolGray
import com.example.ui.theme.DeepNightBlue
import com.example.ui.theme.ElevatedSurface
import com.example.ui.theme.LilacAccent
import com.example.ui.theme.NeonMint
import com.example.ui.theme.TalkFlowTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.TalkFlowViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TalkFlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TalkFlowTheme {
                TalkFlowApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TalkFlowApp(viewModel: TalkFlowViewModel) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val callHistory by viewModel.callHistory.collectAsState()
    val savedVocabulary by viewModel.savedVocabulary.collectAsState()
    val costaRicaTime by viewModel.costaRicaTime.collectAsState()
    val costaRicaGreeting by viewModel.costaRicaGreeting.collectAsState()

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
        if (!isGranted) {
            Toast.makeText(
                context,
                "Microphone permission is recommended for voice coaching. You can also type answers.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasAudioPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // Determine if the current screen is one of the top-level bottom nav destinations
    val showBottomBar = currentScreen is AppScreen.Dashboard ||
            currentScreen is AppScreen.Practice ||
            currentScreen is AppScreen.Progress

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNightBlue),
        containerColor = DeepNightBlue,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = ElevatedSurface,
                    modifier = Modifier
                        .height(72.dp)
                        .border(1.dp, BorderLine, RectangleShape)
                        .testTag("main_bottom_nav"),
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen is AppScreen.Dashboard,
                        onClick = { viewModel.navigateTo(AppScreen.Dashboard) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = {
                            Text(
                                text = "Home",
                                fontSize = 11.sp,
                                fontWeight = if (currentScreen is AppScreen.Dashboard) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonMint,
                            selectedTextColor = NeonMint,
                            indicatorColor = NeonMint.copy(alpha = 0.15f),
                            unselectedIconColor = CoolGray,
                            unselectedTextColor = CoolGray
                        ),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        selected = currentScreen is AppScreen.Practice,
                        onClick = { viewModel.navigateTo(AppScreen.Practice) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Practice"
                            )
                        },
                        label = {
                            Text(
                                text = "Practice",
                                fontSize = 11.sp,
                                fontWeight = if (currentScreen is AppScreen.Practice) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonMint,
                            selectedTextColor = NeonMint,
                            indicatorColor = NeonMint.copy(alpha = 0.15f),
                            unselectedIconColor = CoolGray,
                            unselectedTextColor = CoolGray
                        ),
                        modifier = Modifier.testTag("nav_item_practice")
                    )

                    NavigationBarItem(
                        selected = currentScreen is AppScreen.Progress,
                        onClick = { viewModel.navigateTo(AppScreen.Progress) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Progress"
                            )
                        },
                        label = {
                            Text(
                                text = "Progress",
                                fontSize = 11.sp,
                                fontWeight = if (currentScreen is AppScreen.Progress) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonMint,
                            selectedTextColor = NeonMint,
                            indicatorColor = NeonMint.copy(alpha = 0.15f),
                            unselectedIconColor = CoolGray,
                            unselectedTextColor = CoolGray
                        ),
                        modifier = Modifier.testTag("nav_item_progress")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is AppScreen.Onboarding -> {
                    BackHandler(enabled = userProfile != null && userProfile!!.username.isNotBlank()) {
                        viewModel.navigateTo(AppScreen.Dashboard)
                    }
                    OnboardingScreen(
                        currentName = userProfile?.username ?: "",
                        currentGoals = userProfile?.goalsDescription ?: "",
                        currentTargetMins = userProfile?.dailyTargetMinutes ?: 10,
                        costaRicaTime = costaRicaTime,
                        onSaveProfile = { name, goals, targetMins ->
                            viewModel.saveUserProfile(name, goals, targetMins)
                        }
                    )
                }

                is AppScreen.Dashboard -> {
                    DashboardScreen(
                        userProfile = userProfile,
                        greeting = costaRicaGreeting,
                        costaRicaTime = costaRicaTime,
                        onSelectScenario = { scenario ->
                            viewModel.openPreCall(scenario)
                        },
                        onViewAllScenarios = {
                            viewModel.navigateTo(AppScreen.Practice)
                        },
                        onNotificationClick = {
                            Toast.makeText(
                                context,
                                "Daily speaking reminder set for Costa Rica local time.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }

                is AppScreen.Practice -> {
                    BackHandler {
                        viewModel.navigateTo(AppScreen.Dashboard)
                    }
                    PracticeScreen(
                        onSelectScenario = { scenario ->
                            viewModel.openPreCall(scenario)
                        }
                    )
                }

                is AppScreen.Progress -> {
                    BackHandler {
                        viewModel.navigateTo(AppScreen.Dashboard)
                    }
                    ProgressScreen(
                        userProfile = userProfile,
                        callHistory = callHistory,
                        savedVocabulary = savedVocabulary,
                        costaRicaTime = costaRicaTime,
                        onEditProfile = {
                            viewModel.navigateTo(AppScreen.Onboarding)
                        },
                        onRemoveVocabWord = { word ->
                            viewModel.toggleSaveVocabWord(word)
                        },
                        onClearAllData = {
                            viewModel.clearAllUserData()
                        }
                    )
                }

                is AppScreen.PreCallRoom -> {
                    val strictness by viewModel.selectedStrictness.collectAsState()
                    val selectedKeywords by viewModel.selectedKeywords.collectAsState()

                    BackHandler {
                        viewModel.navigateTo(AppScreen.Dashboard)
                    }

                    PreCallRoomScreen(
                        scenario = screen.scenario,
                        strictness = strictness,
                        selectedKeywords = selectedKeywords,
                        voiceEngine = viewModel.voiceEngine,
                        onBack = { viewModel.navigateTo(AppScreen.Dashboard) },
                        onStrictnessChange = { viewModel.setStrictness(it) },
                        onToggleKeyword = { viewModel.toggleKeyword(it) },
                        onAddKeyword = { viewModel.addCustomKeyword(it) },
                        onStartCall = {
                            if (!hasAudioPermission) {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                            viewModel.startCall(screen.scenario)
                        }
                    )
                }

                is AppScreen.ActiveCall -> {
                    val engineState by viewModel.callState.collectAsState()
                    val botEnglishText by viewModel.currentBotText.collectAsState()
                    val botSpanishText by viewModel.currentBotSpanish.collectAsState()
                    val isTranslationVisible by viewModel.isTranslationVisible.collectAsState()
                    val callSeconds by viewModel.callSeconds.collectAsState()
                    val adaptiveSpeed by viewModel.adaptiveSpeed.collectAsState()
                    val adaptiveVocabulary by viewModel.adaptiveVocabulary.collectAsState()
                    val coachTip by viewModel.coachTip.collectAsState()
                    val userTranscriptHistory by viewModel.userTranscriptHistory.collectAsState()
                    val isMuted by viewModel.isMuted.collectAsState()
                    val isSpeakerOn by viewModel.isSpeakerOn.collectAsState()

                    val isPanicModeOpen by viewModel.isPanicModeOpen.collectAsState()
                    val panicSpanishInput by viewModel.panicSpanishInput.collectAsState()
                    val panicEnglishTranslation by viewModel.panicEnglishTranslation.collectAsState()
                    val turnIndex by viewModel.currentTurnIndex.collectAsState()
                    val currentTurn = screen.scenario.turns.getOrNull(turnIndex)
                    val defaultPanic = currentTurn?.panic ?: ScenarioData.interview.turns.first().panic

                    BackHandler {
                        viewModel.endCallAndShowFeedback()
                    }

                    ActiveCallScreen(
                        scenario = screen.scenario,
                        engineState = engineState,
                        botEnglishText = botEnglishText,
                        botSpanishText = botSpanishText,
                        isTranslationVisible = isTranslationVisible,
                        callSeconds = callSeconds,
                        costaRicaTime = costaRicaTime,
                        adaptiveSpeed = adaptiveSpeed,
                        adaptiveVocabulary = adaptiveVocabulary,
                        coachTip = coachTip,
                        userTranscriptHistory = userTranscriptHistory,
                        isMuted = isMuted,
                        isSpeakerOn = isSpeakerOn,
                        voiceEngine = viewModel.voiceEngine,
                        onToggleTranslation = { viewModel.toggleSubtitleTranslation() },
                        onToggleMute = { viewModel.toggleMute() },
                        onToggleSpeaker = { viewModel.toggleSpeaker() },
                        onUserSubmitAnswer = { text ->
                            viewModel.handleUserFinishedSpeaking(text)
                        },
                        onOpenPanicMode = { viewModel.openPanicMode() },
                        onEndCall = { viewModel.endCallAndShowFeedback() }
                    )

                    // Panic Mode Bottom Sheet Overlay
                    PanicModeSheet(
                        isOpen = isPanicModeOpen,
                        currentSuggestion = defaultPanic,
                        spanishInput = panicSpanishInput,
                        translatedEnglish = panicEnglishTranslation,
                        personaFirstName = screen.scenario.persona.firstName,
                        onSpanishInputChange = { viewModel.updatePanicSpanishInput(it) },
                        onPreviewAudio = { text -> viewModel.voiceEngine.speak(text, 0.95f) },
                        onUseResponse = { text -> viewModel.usePanicSuggestion(text) },
                        onDismiss = { viewModel.closePanicMode() }
                    )
                }

                is AppScreen.Feedback -> {
                    BackHandler {
                        viewModel.navigateTo(AppScreen.Dashboard)
                    }

                    PostCallFeedbackScreen(
                        feedback = screen.feedback,
                        savedVocabulary = savedVocabulary,
                        voiceEngine = viewModel.voiceEngine,
                        onToggleSaveVocab = { word -> viewModel.toggleSaveVocabWord(word) },
                        onClose = { viewModel.navigateTo(AppScreen.Dashboard) }
                    )
                }
            }
        }
    }
}
