package com.example.data.localization

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class VoiceAssistantManager(private val context: Context) : TextToSpeech.OnInitListener {

    private val TAG = "VoiceAssistantManager"
    private var tts: TextToSpeech? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _isTtsReady = MutableStateFlow(false)
    val isTtsReady: StateFlow<Boolean> = _isTtsReady.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentUtteranceId = MutableStateFlow<String?>(null)
    val currentUtteranceId: StateFlow<String?> = _currentUtteranceId.asStateFlow()

    private val _currentSpokenText = MutableStateFlow<String?>("")
    val currentSpokenText: StateFlow<String?> = _currentSpokenText.asStateFlow()

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing TextToSpeech: ${e.message}")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    scope.launch {
                        _isSpeaking.value = true
                        _currentUtteranceId.value = utteranceId
                    }
                }

                override fun onDone(utteranceId: String?) {
                    scope.launch {
                        _isSpeaking.value = false
                        _currentUtteranceId.value = null
                    }
                }

                override fun onError(utteranceId: String?) {
                    scope.launch {
                        _isSpeaking.value = false
                        _currentUtteranceId.value = null
                    }
                }
            })
            // Default rate slightly slower for elderly/illiterate clarity
            tts?.setSpeechRate(0.88f)
            tts?.setPitch(1.0f)
            _isTtsReady.value = true
            Log.d(TAG, "TextToSpeech successfully initialized")
        } else {
            Log.e(TAG, "TextToSpeech initialization failed with status: $status")
            _isTtsReady.value = false
        }
    }

    fun speak(
        text: String,
        language: AppLanguage,
        utteranceId: String = "voice_${System.currentTimeMillis()}"
    ) {
        if (text.isBlank()) return

        try {
            tts?.let { engine ->
                // Apply language locale
                val result = engine.setLanguage(language.locale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Fallback to Indian English or default if specific language pack not installed
                    engine.setLanguage(Locale("en", "IN"))
                }

                _currentSpokenText.value = text
                _currentUtteranceId.value = utteranceId
                _isSpeaking.value = true

                val params = Bundle().apply {
                    putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
                }

                engine.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error speaking text: ${e.message}")
            _isSpeaking.value = false
        }
    }

    fun stop() {
        try {
            tts?.stop()
            _isSpeaking.value = false
            _currentUtteranceId.value = null
            _currentSpokenText.value = ""
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping TTS: ${e.message}")
        }
    }

    fun setSpeechRate(rate: Float) {
        try {
            tts?.setSpeechRate(rate.coerceIn(0.6f, 1.5f))
        } catch (e: Exception) {
            Log.e(TAG, "Error setting speech rate: ${e.message}")
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
            _isTtsReady.value = false
            _isSpeaking.value = false
        } catch (e: Exception) {
            Log.e(TAG, "Error shutting down TTS: ${e.message}")
        }
    }
}
