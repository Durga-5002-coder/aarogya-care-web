package com.example.data.localization

import java.util.Locale

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val shortBadge: String,
    val locale: Locale,
    val sampleGreeting: String
) {
    ENGLISH(
        code = "en",
        nativeName = "English",
        englishName = "English",
        shortBadge = "EN",
        locale = Locale("en", "IN"),
        sampleGreeting = "Welcome to AarogyaCare! Voice assistance is now active in English."
    ),
    TELUGU(
        code = "te",
        nativeName = "తెలుగు",
        englishName = "Telugu",
        shortBadge = "తె",
        locale = Locale("te", "IN"),
        sampleGreeting = "నమస్కారం! ఆరోగ్యకేర్ వాయిస్ సహాయకుడు ఇప్పుడు తెలుగులో సిద్ధంగా ఉంది."
    ),
    HINDI(
        code = "hi",
        nativeName = "हिन्दी",
        englishName = "Hindi",
        shortBadge = "हि",
        locale = Locale("hi", "IN"),
        sampleGreeting = "नमस्ते! आरोग्यकेयर वॉयस सहायक अब हिन्दी में सक्रिय है।"
    ),
    TAMIL(
        code = "ta",
        nativeName = "தமிழ்",
        englishName = "Tamil",
        shortBadge = "த",
        locale = Locale("ta", "IN"),
        sampleGreeting = "வணக்கம்! ஆரோக்கியகேர் குரல் உதவி இப்போது தமிழில் தயார்."
    ),
    KANNADA(
        code = "kn",
        nativeName = "ಕನ್ನಡ",
        englishName = "Kannada",
        shortBadge = "ಕ",
        locale = Locale("kn", "IN"),
        sampleGreeting = "ನಮಸ್ಕಾರ! ಆರೋಗ್ಯಕೇರ್ ಧ್ವನಿ ಸಹಾಯಕ ಈಗ ಕನ್ನಡದಲ್ಲಿ ಸಿದ್ಧವಾಗಿದೆ."
    ),
    BENGALI(
        code = "bn",
        nativeName = "বাংলা",
        englishName = "Bengali",
        shortBadge = "বা",
        locale = Locale("bn", "IN"),
        sampleGreeting = "নমস্কার! আরোগ্যকেয়ার ভয়েস সহকারী এখন বাংলায় প্রস্তুত।"
    ),
    MARATHI(
        code = "mr",
        nativeName = "मराठी",
        englishName = "Marathi",
        shortBadge = "म",
        locale = Locale("mr", "IN"),
        sampleGreeting = "नमस्कार! आरोग्यकेअर व्हॉइस असिस्टंट आता मराठीमध्ये सुरू आहे."
    );

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}
