package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.localization.AppLanguage
import com.example.data.localization.LocalizationManager
import com.example.data.localization.SpeechInputManager
import com.example.data.localization.StringKey
import com.example.data.localization.VoiceAssistantManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VoiceAssistantViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("aarogya_care_voice_prefs", Context.MODE_PRIVATE)
    private val voiceAssistantManager = VoiceAssistantManager(application)
    private val speechInputManager = SpeechInputManager(application)

    private val _currentLanguage = MutableStateFlow(
        AppLanguage.fromCode(prefs.getString("selected_language_code", "te") ?: "te")
    )
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _isVoiceGuideEnabled = MutableStateFlow(
        prefs.getBoolean("voice_guide_enabled", true)
    )
    val isVoiceGuideEnabled: StateFlow<Boolean> = _isVoiceGuideEnabled.asStateFlow()

    private val _speechRate = MutableStateFlow(
        prefs.getFloat("speech_rate", 0.85f)
    )
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    val isSpeaking: StateFlow<Boolean> = voiceAssistantManager.isSpeaking
    val currentUtteranceId: StateFlow<String?> = voiceAssistantManager.currentUtteranceId
    val currentSpokenText: StateFlow<String?> = voiceAssistantManager.currentSpokenText
    val isListening: StateFlow<Boolean> = speechInputManager.isListening
    val isTtsReady: StateFlow<Boolean> = voiceAssistantManager.isTtsReady

    private val _isLanguageDialogVisible = MutableStateFlow(false)
    val isLanguageDialogVisible: StateFlow<Boolean> = _isLanguageDialogVisible.asStateFlow()

    private val _isVoiceInputDialogVisible = MutableStateFlow(false)
    val isVoiceInputDialogVisible: StateFlow<Boolean> = _isVoiceInputDialogVisible.asStateFlow()

    private val _lastSpokenTranscription = MutableStateFlow("")
    val lastSpokenTranscription: StateFlow<String> = _lastSpokenTranscription.asStateFlow()

    init {
        voiceAssistantManager.setSpeechRate(_speechRate.value)
    }

    fun setLanguage(language: AppLanguage, speakGreeting: Boolean = true) {
        _currentLanguage.value = language
        prefs.edit().putString("selected_language_code", language.code).apply()

        if (speakGreeting) {
            voiceAssistantManager.speak(
                text = language.sampleGreeting,
                language = language,
                utteranceId = "lang_greeting_${language.code}"
            )
        }
    }

    fun setSpeechRate(rate: Float) {
        _speechRate.value = rate
        prefs.edit().putFloat("speech_rate", rate).apply()
        voiceAssistantManager.setSpeechRate(rate)
    }

    fun toggleVoiceGuide(enabled: Boolean? = null) {
        val newState = enabled ?: !_isVoiceGuideEnabled.value
        _isVoiceGuideEnabled.value = newState
        prefs.edit().putBoolean("voice_guide_enabled", newState).apply()

        if (newState) {
            val announcement = when (_currentLanguage.value) {
                AppLanguage.TELUGU -> "వాయిస్ సహాయకుడు ఆన్ చేయబడింది. కార్డులపై నొక్కితే చదివి వినిపిస్తుంది."
                AppLanguage.HINDI -> "वॉयस सहायक चालू हो गया है। कार्ड दबाने पर बोलकर सुनाया जाएगा।"
                AppLanguage.TAMIL -> "குரல் வழிகாட்டி இயக்கப்பட்டது."
                AppLanguage.KANNADA -> "ಧ್ವನಿ ಸಹಾಯಕ ಸಕ್ರಿಯಗೊಂಡಿದೆ."
                AppLanguage.BENGALI -> "ভয়েস সহকারী চালু হয়েছে।"
                AppLanguage.MARATHI -> "व्हॉइस सहाय्यक सुरू झाला आहे."
                else -> "Voice guide active. Tap cards to hear them read aloud."
            }
            voiceAssistantManager.speak(announcement, _currentLanguage.value, "voice_guide_toggle")
        } else {
            voiceAssistantManager.stop()
        }
    }

    fun speak(text: String, utteranceId: String = "voice_${System.currentTimeMillis()}") {
        voiceAssistantManager.speak(text, _currentLanguage.value, utteranceId)
    }

    fun speakScreen(screenTag: String, userName: String?) {
        val narration = LocalizationManager.getScreenNarration(
            screenTag = screenTag,
            language = _currentLanguage.value,
            userName = userName
        )
        voiceAssistantManager.speak(narration, _currentLanguage.value, "screen_$screenTag")
    }

    fun stopSpeaking() {
        voiceAssistantManager.stop()
    }

    fun showLanguageDialog(show: Boolean) {
        _isLanguageDialogVisible.value = show
    }

    fun showVoiceInputDialog(show: Boolean) {
        _isVoiceInputDialogVisible.value = show
    }

    fun startListening(onResult: (String) -> Unit = {}, onError: (String) -> Unit = {}) {
        speechInputManager.startListening(
            language = _currentLanguage.value,
            onResult = { text ->
                _lastSpokenTranscription.value = text
                onResult(text)
            },
            onError = onError
        )
    }

    fun stopListening() {
        speechInputManager.stopListening()
    }

    fun getString(key: StringKey): String {
        return LocalizationManager.get(key, _currentLanguage.value)
    }

    fun cleanup() {
        voiceAssistantManager.shutdown()
        speechInputManager.stopListening()
    }

    override fun onCleared() {
        super.onCleared()
        cleanup()
    }
}
