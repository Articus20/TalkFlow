package com.example.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class VoiceEngine(private val context: Context) {

    private val TAG = "VoiceEngine"

    private var textToSpeech: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    private val _isTtsReady = MutableStateFlow(false)
    val isTtsReady: StateFlow<Boolean> = _isTtsReady.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _userSpeaking = MutableStateFlow(false)
    val userSpeaking: StateFlow<Boolean> = _userSpeaking.asStateFlow()

    private val _currentRmsLevel = MutableStateFlow(0f) // 0f to 1f
    val currentRmsLevel: StateFlow<Float> = _currentRmsLevel.asStateFlow()

    private val _partialTranscript = MutableStateFlow("")
    val partialTranscript: StateFlow<String> = _partialTranscript.asStateFlow()

    var onUserFinishedSpeaking: ((transcript: String) -> Unit)? = null
    var onAiFinishedSpeaking: (() -> Unit)? = null

    private var isMuted = false
    private var isSpeakerEnabled = true

    init {
        initTts()
        initSpeechRecognizer()
    }

    private fun initTts() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    textToSpeech?.setLanguage(Locale.getDefault())
                }
                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        mainHandler.post {
                            _isSpeaking.value = true
                        }
                    }

                    override fun onDone(utteranceId: String?) {
                        mainHandler.post {
                            _isSpeaking.value = false
                            onAiFinishedSpeaking?.invoke()
                        }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        mainHandler.post {
                            _isSpeaking.value = false
                            onAiFinishedSpeaking?.invoke()
                        }
                    }
                })
                _isTtsReady.value = true
                Log.d(TAG, "TTS initialized successfully")
            } else {
                Log.e(TAG, "TTS initialization failed: $status")
            }
        }
    }

    private fun initSpeechRecognizer() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Log.w(TAG, "Speech recognition not available on this device")
            return
        }
        mainHandler.post {
            try {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(createRecognitionListener())
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error creating SpeechRecognizer", e)
            }
        }
    }

    private fun createRecognitionListener(): RecognitionListener {
        return object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _isListening.value = true
                _userSpeaking.value = false
                Log.d(TAG, "SpeechRecognizer onReadyForSpeech")
            }

            override fun onBeginningOfSpeech() {
                _userSpeaking.value = true
                Log.d(TAG, "User started speaking")
            }

            override fun onRmsChanged(rmsdB: Float) {
                // rmsdB typically ranges from -2 to 10
                val normalized = ((rmsdB + 2f) / 12f).coerceIn(0f, 1f)
                _currentRmsLevel.value = normalized
            }

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                // Crucial requirement: user finished speaking!
                _userSpeaking.value = false
                _isListening.value = false
                Log.d(TAG, "SpeechRecognizer onEndOfSpeech - user ended speaking")
            }

            override fun onError(error: Int) {
                _isListening.value = false
                _userSpeaking.value = false
                _currentRmsLevel.value = 0f
                Log.w(TAG, "SpeechRecognizer onError: $error")
                // If there's partial text accumulated before error (e.g. NO_MATCH / SPEECH_TIMEOUT), treat it as finished turn
                val currentText = _partialTranscript.value.trim()
                if (currentText.isNotEmpty()) {
                    mainHandler.postDelayed({
                        onUserFinishedSpeaking?.invoke(currentText)
                        _partialTranscript.value = ""
                    }, 300)
                }
            }

            override fun onResults(results: Bundle?) {
                _isListening.value = false
                _userSpeaking.value = false
                _currentRmsLevel.value = 0f
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: _partialTranscript.value
                Log.d(TAG, "SpeechRecognizer onResults: $text")
                _partialTranscript.value = ""
                onUserFinishedSpeaking?.invoke(text.trim())
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull()
                if (!text.isNullOrEmpty()) {
                    _partialTranscript.value = text
                    _userSpeaking.value = true
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    fun startListening() {
        if (isMuted) return
        stopTts()

        mainHandler.post {
            try {
                if (speechRecognizer == null) {
                    initSpeechRecognizer()
                }
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                    // Config silence delay so it doesn't cut off too quickly:
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1800L)
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1500L)
                }
                _partialTranscript.value = ""
                speechRecognizer?.startListening(intent)
                _isListening.value = true
            } catch (e: Exception) {
                Log.e(TAG, "Error starting speech recognition", e)
                _isListening.value = false
            }
        }
    }

    fun stopListening() {
        mainHandler.post {
            try {
                speechRecognizer?.stopListening()
            } catch (e: Exception) {
                Log.e(TAG, "Error stopping listening", e)
            }
            _isListening.value = false
            _userSpeaking.value = false
            _currentRmsLevel.value = 0f
        }
    }

    fun cancelListening() {
        mainHandler.post {
            try {
                speechRecognizer?.cancel()
            } catch (e: Exception) {
                Log.e(TAG, "Error cancelling listening", e)
            }
            _isListening.value = false
            _userSpeaking.value = false
            _currentRmsLevel.value = 0f
        }
    }

    fun speak(text: String, speechRate: Float = 0.95f) {
        if (!isSpeakerEnabled) {
            // Speaker muted: complete immediately after simulated delay
            mainHandler.postDelayed({
                onAiFinishedSpeaking?.invoke()
            }, (text.length * 50L).coerceIn(1200L, 3000L))
            return
        }

        stopListening()
        mainHandler.post {
            try {
                textToSpeech?.apply {
                    setSpeechRate(speechRate.coerceIn(0.7f, 1.3f))
                    speak(text, TextToSpeech.QUEUE_FLUSH, null, "Utterance_${System.currentTimeMillis()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in TTS speak", e)
                onAiFinishedSpeaking?.invoke()
            }
        }
    }

    fun stopTts() {
        mainHandler.post {
            try {
                if (textToSpeech?.isSpeaking == true) {
                    textToSpeech?.stop()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error stopping TTS", e)
            }
            _isSpeaking.value = false
        }
    }

    fun setMute(muted: Boolean) {
        isMuted = muted
        if (muted) {
            cancelListening()
        }
    }

    fun setSpeaker(enabled: Boolean) {
        isSpeakerEnabled = enabled
        if (!enabled) {
            stopTts()
        }
    }

    fun release() {
        mainHandler.post {
            try {
                speechRecognizer?.destroy()
            } catch (e: Exception) {
                Log.e(TAG, "Error destroying speechRecognizer", e)
            }
            speechRecognizer = null

            try {
                textToSpeech?.stop()
                textToSpeech?.shutdown()
            } catch (e: Exception) {
                Log.e(TAG, "Error shutting down TTS", e)
            }
            textToSpeech = null
        }
    }
}
