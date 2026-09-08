package com.example.data.localization

enum class StringKey {
    APP_NAME,
    NAV_HOME,
    NAV_TRENDS,
    NAV_MEDICATIONS,
    NAV_AI_DOCTOR,
    NAV_SCANNER,
    NAV_SUMMARY,
    NAV_PROFILE,

    VOICE_GUIDE_TITLE,
    VOICE_GUIDE_SUBTITLE,
    VOICE_READ_ALOUD,
    VOICE_SPEAK_NOW,
    VOICE_STOP_AUDIO,
    VOICE_LISTENING,
    VOICE_SPEAK_PROMPT,
    VOICE_LANGUAGE_SELECT,

    EMERGENCY_SOS,
    EMERGENCY_SUBTITLE,
    EMERGENCY_SPOKEN,

    MEDICATIONS_TITLE,
    MEDICATIONS_SUBTITLE,
    MEDICATIONS_NEXT_DOSE,
    MEDICATIONS_TAKE_NOW,

    OP_TICKETS_TITLE,
    OP_WAIT_MINUTES,
    OP_ROOM,

    SERVICES_TITLE,
    SERVICE_MED_ALARMS,
    SERVICE_AI_BOT,
    SERVICE_SCAN_REPORTS,
    SERVICE_DOCTOR_SUMMARY,
    SERVICE_HEALTH_TRENDS,

    HOSPITAL_DIRECTORY_TITLE,
    BOOK_OP_BUTTON,

    CHAT_INPUT_HINT,
    CHAT_SPEAK_SYMPTOM,
    CHAT_AI_LISTENING,

    SCAN_DOCUMENTS_TITLE,
    SCAN_CAMERA_BUTTON,
    SCAN_GALLERY_BUTTON,

    PROFILE_TITLE,
    AADHAAR_VERIFIED
}

object LocalizationManager {

    private val translations: Map<StringKey, Map<AppLanguage, String>> = mapOf(
        StringKey.APP_NAME to mapOf(
            AppLanguage.ENGLISH to "AarogyaCare",
            AppLanguage.TELUGU to "ఆరోగ్యకేర్",
            AppLanguage.HINDI to "आरोग्यकेयर",
            AppLanguage.TAMIL to "ஆரோக்கியகேர்",
            AppLanguage.KANNADA to "ಆರೋಗ್ಯಕೇರ್",
            AppLanguage.BENGALI to "আরোগ্যকেয়ার",
            AppLanguage.MARATHI to "आरोग्यकेअर"
        ),

        StringKey.NAV_HOME to mapOf(
            AppLanguage.ENGLISH to "Home",
            AppLanguage.TELUGU to "హోమ్",
            AppLanguage.HINDI to "होम",
            AppLanguage.TAMIL to "முகப்பு",
            AppLanguage.KANNADA to "ಮುಖಪುಟ",
            AppLanguage.BENGALI to "হোম",
            AppLanguage.MARATHI to "मुख्यपृष्ठ"
        ),

        StringKey.NAV_TRENDS to mapOf(
            AppLanguage.ENGLISH to "Trends",
            AppLanguage.TELUGU to "ట్రెండ్స్",
            AppLanguage.HINDI to "ट्रेंड्स",
            AppLanguage.TAMIL to "விவரங்கள்",
            AppLanguage.KANNADA to "ವರದಿಗಳು",
            AppLanguage.BENGALI to "ট্রেন্ড",
            AppLanguage.MARATHI to "ट्रेंड्स"
        ),

        StringKey.NAV_MEDICATIONS to mapOf(
            AppLanguage.ENGLISH to "Meds",
            AppLanguage.TELUGU to "మందులు",
            AppLanguage.HINDI to "दवाइयां",
            AppLanguage.TAMIL to "மருந்துகள்",
            AppLanguage.KANNADA to "ಔಷಧಿಗಳು",
            AppLanguage.BENGALI to "ওষুধ",
            AppLanguage.MARATHI to "औषधे"
        ),

        StringKey.NAV_AI_DOCTOR to mapOf(
            AppLanguage.ENGLISH to "AI Doctor",
            AppLanguage.TELUGU to "AI డాక్టర్",
            AppLanguage.HINDI to "AI डॉक्टर",
            AppLanguage.TAMIL to "AI மருத்துவர்",
            AppLanguage.KANNADA to "AI ವೈದ್ಯರು",
            AppLanguage.BENGALI to "AI ডাক্তার",
            AppLanguage.MARATHI to "AI डॉक्टर"
        ),

        StringKey.NAV_SCANNER to mapOf(
            AppLanguage.ENGLISH to "Reports",
            AppLanguage.TELUGU to "రిపోర్టులు",
            AppLanguage.HINDI to "रिपोर्ट्स",
            AppLanguage.TAMIL to "அறிக்கைகள்",
            AppLanguage.KANNADA to "ದಾಖಲೆಗಳು",
            AppLanguage.BENGALI to "রিপোর্ট",
            AppLanguage.MARATHI to "अहवाल"
        ),

        StringKey.NAV_SUMMARY to mapOf(
            AppLanguage.ENGLISH to "Summary",
            AppLanguage.TELUGU to "సారాంశం",
            AppLanguage.HINDI to "सारांश",
            AppLanguage.TAMIL to "சுருக்கம்",
            AppLanguage.KANNADA to "ಸಾರಾಂಶ",
            AppLanguage.BENGALI to "সারাংশ",
            AppLanguage.MARATHI to "सारांश"
        ),

        StringKey.NAV_PROFILE to mapOf(
            AppLanguage.ENGLISH to "Profile",
            AppLanguage.TELUGU to "ప్రొఫైల్",
            AppLanguage.HINDI to "प्रोफाइल",
            AppLanguage.TAMIL to "சுயவிவரம்",
            AppLanguage.KANNADA to "ಪ್ರೊಫೈಲ್",
            AppLanguage.BENGALI to "প্রোফাইল",
            AppLanguage.MARATHI to "प्रोफाइल"
        ),

        StringKey.VOICE_GUIDE_TITLE to mapOf(
            AppLanguage.ENGLISH to "Voice Guide for Patients",
            AppLanguage.TELUGU to "రోగుల వాయిస్ సహాయకుడు",
            AppLanguage.HINDI to "मरीजों के लिए वॉयस सहायक",
            AppLanguage.TAMIL to "நோயாளிக்கான குரல் வழிகாட்டி",
            AppLanguage.KANNADA to "ರೋಗಿಗಳ ಧ್ವನಿ ಮಾರ್ಗದರ್ಶಿ",
            AppLanguage.BENGALI to "রোগীদের ভয়েস সহকারী",
            AppLanguage.MARATHI to "रुग्णांसाठी व्हॉइस मार्गदर्शक"
        ),

        StringKey.VOICE_GUIDE_SUBTITLE to mapOf(
            AppLanguage.ENGLISH to "Tap any card or button to listen. Speak to ask questions.",
            AppLanguage.TELUGU to "వినడానికి ఏదైనా కార్డుపై నొక్కండి. మీ సమస్యను మాట్లాడి అడగండి.",
            AppLanguage.HINDI to "सुनने के लिए किसी भी कार्ड को दबाएं। सवाल पूछने के लिए बोलें।",
            AppLanguage.TAMIL to "கேட்க ஏதேனும் அட்டையைத் தொடவும். பேச மைக் பயன்படுத்தவும்.",
            AppLanguage.KANNADA to "ಕೇಳಲು ಯಾವುದೇ ಕಾರ್ಡ್ ಒತ್ತಿರಿ. ಪ್ರಶ್ನೆ ಕೇಳಲು ಮಾತನಾಡಿ.",
            AppLanguage.BENGALI to "শুনতে যেকোনো কার্ডে স্পর্শ করুন। কথা বলতে মাইক ব্যবহার করুন।",
            AppLanguage.MARATHI to "ऐकण्यासाठी कोणत्याही कार्डवर दाबा. विचारण्यासाठी बोला."
        ),

        StringKey.VOICE_READ_ALOUD to mapOf(
            AppLanguage.ENGLISH to "Read Aloud",
            AppLanguage.TELUGU to "చదవండి",
            AppLanguage.HINDI to "बोलकर सुनाएं",
            AppLanguage.TAMIL to "வாசிக்கவும்",
            AppLanguage.KANNADA to "ಓದಿ ಹೇಳಿ",
            AppLanguage.BENGALI to "পড়ে শোনান",
            AppLanguage.MARATHI to "वाचून दाखवा"
        ),

        StringKey.VOICE_SPEAK_NOW to mapOf(
            AppLanguage.ENGLISH to "Speak",
            AppLanguage.TELUGU to "మాట్లాడండి",
            AppLanguage.HINDI to "बोलें",
            AppLanguage.TAMIL to "பேசுங்கள்",
            AppLanguage.KANNADA to "ಮಾತನಾಡಿ",
            AppLanguage.BENGALI to "কথা বলুন",
            AppLanguage.MARATHI to "बोला"
        ),

        StringKey.VOICE_STOP_AUDIO to mapOf(
            AppLanguage.ENGLISH to "Stop Voice",
            AppLanguage.TELUGU to "వాయిస్ ఆపు",
            AppLanguage.HINDI to "आवाज़ रोकें",
            AppLanguage.TAMIL to "நிறுத்து",
            AppLanguage.KANNADA to "ನಿಲ್ಲಿಸಿ",
            AppLanguage.BENGALI to "থামুন",
            AppLanguage.MARATHI to "थांबवा"
        ),

        StringKey.VOICE_LISTENING to mapOf(
            AppLanguage.ENGLISH to "Listening... Speak your problem clearly",
            AppLanguage.TELUGU to "వింటున్నాను... మీ సమస్యను స్పష్టంగా మాట్లాడండి",
            AppLanguage.HINDI to "सुन रहा हूँ... अपनी बीमारी या समस्या बोलें",
            AppLanguage.TAMIL to "கேட்கிறேன்... உங்கள் நோயைப் பேசுங்கள்",
            AppLanguage.KANNADA to "ಕೇಳುತ್ತಿದ್ದೇನೆ... ನಿಮ್ಮ ಸಮಸ್ಯೆಯನ್ನು ಮಾತನಾಡಿ",
            AppLanguage.BENGALI to "শুনছি... আপনার সমস্যার কথা স্পষ্টভাবে বলুন",
            AppLanguage.MARATHI to "ऐकत आहे... आपली समस्या स्पष्टपणे बोला"
        ),

        StringKey.VOICE_SPEAK_PROMPT to mapOf(
            AppLanguage.ENGLISH to "Tap microphone to speak your symptom",
            AppLanguage.TELUGU to "మీ సమస్యను చెప్పడానికి మైక్రోఫోన్ నొక్కండి",
            AppLanguage.HINDI to "अपनी तकलीफ बताने के लिए माइक दबाएं",
            AppLanguage.TAMIL to "உங்கள் உடல்நிலையைச் சொல்ல மைக்கைத் தொடவும்",
            AppLanguage.KANNADA to "ನಿಮ್ಮ ತೊಂದರೆ ಹೇಳಲು ಮೈಕ್ರೊಫೋನ್ ಒತ್ತಿರಿ",
            AppLanguage.BENGALI to "সমস্যা বলতে মাইকে চাপ দিন",
            AppLanguage.MARATHI to "आपली अडचण सांगण्यासाठी माइक दाबा"
        ),

        StringKey.VOICE_LANGUAGE_SELECT to mapOf(
            AppLanguage.ENGLISH to "Choose Language / భాష ఎంచుకోండి",
            AppLanguage.TELUGU to "భాష ఎంచుకోండి / Choose Language",
            AppLanguage.HINDI to "भाषा चुनें / Choose Language",
            AppLanguage.TAMIL to "மொழியைத் தேர்ந்தெடுக்கவும்",
            AppLanguage.KANNADA to "ಭಾಷೆ ಆಯ್ಕೆಮಾಡಿ",
            AppLanguage.BENGALI to "ভাষা নির্বাচন করুন",
            AppLanguage.MARATHI to "भाषा निवडा"
        ),

        StringKey.EMERGENCY_SOS to mapOf(
            AppLanguage.ENGLISH to "Emergency SOS 108",
            AppLanguage.TELUGU to "అత్యవసర SOS 108",
            AppLanguage.HINDI to "आपातकालीन SOS 108",
            AppLanguage.TAMIL to "அவசர உதவி 108",
            AppLanguage.KANNADA to "ತುರ್ತು ಸೇವೆ 108",
            AppLanguage.BENGALI to "জরুরি অ্যাম্বুলেন্স 108",
            AppLanguage.MARATHI to "आपत्कालीन SOS 108"
        ),

        StringKey.EMERGENCY_SUBTITLE to mapOf(
            AppLanguage.ENGLISH to "Free Government Ambulance & Emergency Care",
            AppLanguage.TELUGU to "ఉచిత ప్రభుత్వ అంబులెన్స్ & అత్యవసర వైద్య సేవ",
            AppLanguage.HINDI to "मुफ्त सरकारी एम्बुलेंस और आपातकालीन सेवा",
            AppLanguage.TAMIL to "இலவச அரசு ஆம்புலன்ஸ் மற்றும் அவசர சிகிச்சை",
            AppLanguage.KANNADA to "ಉಚಿತ ಸರ್ಕಾರಿ ಆಂಬ್ಯುಲೆನ್ಸ್ ಮತ್ತು ತುರ್ತು ಚಿಕಿತ್ಸೆ",
            AppLanguage.BENGALI to "বিনামূল্যে সরকারি অ্যাম্বুলেন্স ও জরুরি সেবা",
            AppLanguage.MARATHI to "मोफत सरकारी रुग्णवाहिका आणि तातडीची सेवा"
        ),

        StringKey.EMERGENCY_SPOKEN to mapOf(
            AppLanguage.ENGLISH to "Attention! For severe chest pain, breathing trouble or accidents, call 108 emergency ambulance right now.",
            AppLanguage.TELUGU to "గమనిక! తీవ్రమైన గుండె నొప్పి, ఊపిరి ఆడకపోవడం లేదా ప్రమాదాలు జరిగితే వెంటనే 108 అంబులెన్స్ కు కాల్ చేయండి.",
            AppLanguage.HINDI to "सावधान! तेज सीने में दर्द, सांस लेने में तकलीफ या दुर्घटना के समय तुरंत 108 एम्बुलेंस पर कॉल करें।",
            AppLanguage.TAMIL to "கவனம்! கடுமையான நெஞ்சு வலி அல்லது அவசர நிலைக்கு உடனே 108 ஆம்புலன்ஸை அழைக்கவும்.",
            AppLanguage.KANNADA to "ಎಚ್ಚರಿಕೆ! ತೀವ್ರ ಎದೆನೋವು ಅಥವಾ ಉಸಿರಾಟದ ಸಮಸ್ಯೆಗೆ ತಕ್ಷಣ 108 ಆಂಬ್ಯುಲೆನ್ಸ್‌ಗೆ ಕರೆ ಮಾಡಿ.",
            AppLanguage.BENGALI to "সতর্কতা! তীব্র বুকে ব্যথা বা শ্বাসকষ্ট হলে অবিলম্বে 108 অ্যাম্বুলেন্সে কল করুন।",
            AppLanguage.MARATHI to "सावधान! तीव्र छातीत दुखणे किंवा श्वास घेण्यास त्रास झाल्यास त्वरित 108 रुग्णवाहिकेला कॉल करा."
        ),

        StringKey.MEDICATIONS_TITLE to mapOf(
            AppLanguage.ENGLISH to "Prescriptions & Med Alarms",
            AppLanguage.TELUGU to "ప్రిస్క్రిప్షన్ & మందుల అలారాలు",
            AppLanguage.HINDI to "दवाई पर्ची और अलार्म",
            AppLanguage.TAMIL to "மருந்து எச்சரிக்கை & குறிப்புகள்",
            AppLanguage.KANNADA to "ಔಷಧಿ ಎಚ್ಚರಿಕೆ ಮತ್ತು ಜ್ಞಾಪನೆ",
            AppLanguage.BENGALI to "প্রেসক্রিপশন ও ওষুধের অ্যালার্ম",
            AppLanguage.MARATHI to "औषधांचे अलार्म आणि सूचना"
        ),

        StringKey.MEDICATIONS_SUBTITLE to mapOf(
            AppLanguage.ENGLISH to "Voice reminders and exact timings for medicines",
            AppLanguage.TELUGU to "మందుల సరైన సమయాలు మరియు వాయిస్ రిమైండర్లు",
            AppLanguage.HINDI to "दवाइयों के सही समय और बोलकर याद दिलाने वाली सूचनाएं",
            AppLanguage.TAMIL to "மருந்துகளின் சரியான நேரம் மற்றும் குரல் நினைவூட்டல்",
            AppLanguage.KANNADA to "ಔಷಧಿಗಳ ಸರಿಯಾದ ಸಮಯ ಮತ್ತು ಧ್ವನಿ ಜ್ಞಾಪನೆಗಳು",
            AppLanguage.BENGALI to "ওষুধের সঠিক সময় এবং ভয়েস অনুস্মারক",
            AppLanguage.MARATHI to "औषधांची योग्य वेळ आणि व्हॉइस स्मरणपत्रे"
        ),

        StringKey.MEDICATIONS_NEXT_DOSE to mapOf(
            AppLanguage.ENGLISH to "Upcoming Medicine Dose",
            AppLanguage.TELUGU to "తదుపరి తీసుకోవలసిన మందు",
            AppLanguage.HINDI to "अगली दवाई की खुराक",
            AppLanguage.TAMIL to "அடுத்த மருந்து வேளை",
            AppLanguage.KANNADA to "ಮುಂದಿನ ಔಷಧಿಯ ಡೋಸ್",
            AppLanguage.BENGALI to "পরবর্তী ওষুধের মাত্রা",
            AppLanguage.MARATHI to "पुढील औषधाची मात्रा"
        ),

        StringKey.MEDICATIONS_TAKE_NOW to mapOf(
            AppLanguage.ENGLISH to "Mark Taken",
            AppLanguage.TELUGU to "తీసుకున్నాను",
            AppLanguage.HINDI to "दवाई ले ली",
            AppLanguage.TAMIL to "எடுத்துக்கொண்டேன்",
            AppLanguage.KANNADA to "ತೆಗೆದುಕೊಂಡೆ",
            AppLanguage.BENGALI to "খেয়েছি",
            AppLanguage.MARATHI to "औषध घेतले"
        ),

        StringKey.OP_TICKETS_TITLE to mapOf(
            AppLanguage.ENGLISH to "My Hospital OP Consultations",
            AppLanguage.TELUGU to "నా హాస్పిటల్ OP టోకెన్లు",
            AppLanguage.HINDI to "मेरे अस्पताल OP टोकन",
            AppLanguage.TAMIL to "என் மருத்துவமனை OP சீட்டுகள்",
            AppLanguage.KANNADA to "ನನ್ನ ಆಸ್ಪತ್ರೆ OP ಟೋಕನ್‌ಗಳು",
            AppLanguage.BENGALI to "আমার হাসপাতাল ওপি টোকেন",
            AppLanguage.MARATHI to "माझे रुग्णालय OP टोकन्स"
        ),

        StringKey.OP_WAIT_MINUTES to mapOf(
            AppLanguage.ENGLISH to "Est. Wait",
            AppLanguage.TELUGU to "వేచి ఉండే సమయం",
            AppLanguage.HINDI to "अनुमानित समय",
            AppLanguage.TAMIL to "காத்திருப்பு நேரம்",
            AppLanguage.KANNADA to "ಕಾಯುವ ಸಮಯ",
            AppLanguage.BENGALI to "অপেক্ষার সময়",
            AppLanguage.MARATHI to "प्रतीक्षा वेळ"
        ),

        StringKey.OP_ROOM to mapOf(
            AppLanguage.ENGLISH to "Room",
            AppLanguage.TELUGU to "గది",
            AppLanguage.HINDI to "कमरा",
            AppLanguage.TAMIL to "அறை",
            AppLanguage.KANNADA to "ಕೋಣೆ",
            AppLanguage.BENGALI to "রুম",
            AppLanguage.MARATHI to "खोली"
        ),

        StringKey.SERVICES_TITLE to mapOf(
            AppLanguage.ENGLISH to "Smart Medical Services",
            AppLanguage.TELUGU to "వైద్య సేవలు",
            AppLanguage.HINDI to "चिकित्सा सेवाएं",
            AppLanguage.TAMIL to "மருத்துவ சேவைகள்",
            AppLanguage.KANNADA to "ವೈದ್ಯಕೀಯ ಸೇವೆಗಳು",
            AppLanguage.BENGALI to "চিকিৎসা পরিষেবা",
            AppLanguage.MARATHI to "वैद्यकीय सेवा"
        ),

        StringKey.SERVICE_MED_ALARMS to mapOf(
            AppLanguage.ENGLISH to "Med Alarms",
            AppLanguage.TELUGU to "మందుల అలారం",
            AppLanguage.HINDI to "दवाई अलार्म",
            AppLanguage.TAMIL to "மருந்து அலாரம்",
            AppLanguage.KANNADA to "ಔಷಧಿ ಅಲಾರಂ",
            AppLanguage.BENGALI to "ওষুধের অ্যালার্ম",
            AppLanguage.MARATHI to "औषध अलार्म"
        ),

        StringKey.SERVICE_AI_BOT to mapOf(
            AppLanguage.ENGLISH to "AI Doctor Bot",
            AppLanguage.TELUGU to "AI డాక్టర్ సహాయం",
            AppLanguage.HINDI to "AI डॉक्टर बॉट",
            AppLanguage.TAMIL to "AI மருத்துவ உதவி",
            AppLanguage.KANNADA to "AI ವೈದ್ಯರ ನೆರವು",
            AppLanguage.BENGALI to "AI ডাক্তার বট",
            AppLanguage.MARATHI to "AI डॉक्टर बॉट"
        ),

        StringKey.SERVICE_SCAN_REPORTS to mapOf(
            AppLanguage.ENGLISH to "Scan Reports",
            AppLanguage.TELUGU to "రిపోర్టులు స్కాన్",
            AppLanguage.HINDI to "रिपोर्ट स्कैन करें",
            AppLanguage.TAMIL to "அறிக்கை ஸ்கேன்",
            AppLanguage.KANNADA to "ವರದಿ ಸ್ಕ್ಯಾನ್",
            AppLanguage.BENGALI to "রিপোর্ট স্ক্যান",
            AppLanguage.MARATHI to "अहवाल स्कॅन"
        ),

        StringKey.SERVICE_DOCTOR_SUMMARY to mapOf(
            AppLanguage.ENGLISH to "Doctor Summary",
            AppLanguage.TELUGU to "డాక్టర్ సారాంశం",
            AppLanguage.HINDI to "डॉक्टर सारांश",
            AppLanguage.TAMIL to "மருத்துவர் சுருக்கம்",
            AppLanguage.KANNADA to "ವೈದ್ಯರ ಸಾರಾಂಶ",
            AppLanguage.BENGALI to "ডাক্তারের সারাংশ",
            AppLanguage.MARATHI to "डॉक्टर सारांश"
        ),

        StringKey.SERVICE_HEALTH_TRENDS to mapOf(
            AppLanguage.ENGLISH to "Health Trends",
            AppLanguage.TELUGU to "ఆరోగ్య చార్టులు",
            AppLanguage.HINDI to "स्वास्थ्य चार्ट",
            AppLanguage.TAMIL to "உடல்நல வரைபடங்கள்",
            AppLanguage.KANNADA to "ಆರೋಗ್ಯ ನಕ್ಷೆ",
            AppLanguage.BENGALI to "স্বাস্থ্য চার্ট",
            AppLanguage.MARATHI to "आरोग्य आलेख"
        ),

        StringKey.HOSPITAL_DIRECTORY_TITLE to mapOf(
            AppLanguage.ENGLISH to "OP Consultation Registration",
            AppLanguage.TELUGU to "OP ఆసుపత్రి రిజిస్ట్రేషన్",
            AppLanguage.HINDI to "अस्पताल OP पंजीकरण",
            AppLanguage.TAMIL to "மருத்துவமனை OP பதிவு",
            AppLanguage.KANNADA to "ಆಸ್ಪತ್ರೆ OP ನೋಂದಣಿ",
            AppLanguage.BENGALI to "হাসপাতাল ওপি নিবন্ধন",
            AppLanguage.MARATHI to "रुग्णालय OP नोंदणी"
        ),

        StringKey.BOOK_OP_BUTTON to mapOf(
            AppLanguage.ENGLISH to "Book OP Token",
            AppLanguage.TELUGU to "OP టోకెన్ బుక్ చేయండి",
            AppLanguage.HINDI to "OP टोकन बुक करें",
            AppLanguage.TAMIL to "OP சீட்டு எடுக்கவும்",
            AppLanguage.KANNADA to "OP ಟೋಕನ್ ಕಾಯ್ದಿರಿಸಿ",
            AppLanguage.BENGALI to "ওপি টোকেন বুক করুন",
            AppLanguage.MARATHI to "OP टोकन बुक करा"
        ),

        StringKey.CHAT_INPUT_HINT to mapOf(
            AppLanguage.ENGLISH to "Ask about symptoms or tap mic to speak...",
            AppLanguage.TELUGU to "మీ సమస్యను చెప్పండి లేదా మాట్లాడండి...",
            AppLanguage.HINDI to "अपनी बीमारी लिखें या बोलने के लिए माइक दबाएं...",
            AppLanguage.TAMIL to "உங்கள் நோயைக் கூறவும் அல்லது பேசவும்...",
            AppLanguage.KANNADA to "ನಿಮ್ಮ ಸಮಸ್ಯೆ ಟೈಪ್ ಮಾಡಿ ಅಥವಾ ಮಾತನಾಡಿ...",
            AppLanguage.BENGALI to "সমস্যার কথা লিখুন অথবা মাইকে বলুন...",
            AppLanguage.MARATHI to "आपली समस्या लिहा किंवा बोलण्यासाठी माइक दाबा..."
        ),

        StringKey.CHAT_SPEAK_SYMPTOM to mapOf(
            AppLanguage.ENGLISH to "Tap Mic to Speak",
            AppLanguage.TELUGU to "మాట్లాడటానికి మైక్ నొక్కండి",
            AppLanguage.HINDI to "बोलने के लिए माइक दबाएं",
            AppLanguage.TAMIL to "பேச மைக் அழுத்தவும்",
            AppLanguage.KANNADA to "ಮಾತನಾಡಲು ಮೈಕ್ ಒತ್ತಿರಿ",
            AppLanguage.BENGALI to "কথা বলতে মাইক চাপুন",
            AppLanguage.MARATHI to "बोलण्यासाठी माइक दाबा"
        ),

        StringKey.CHAT_AI_LISTENING to mapOf(
            AppLanguage.ENGLISH to "Voice Mode Active: AI doctor will read replies aloud.",
            AppLanguage.TELUGU to "వాయిస్ మోడ్ ఆన్: డాక్టర్ సమాధానాలను బిగ్గరగా చదివి వినిపిస్తుంది.",
            AppLanguage.HINDI to "वॉयस मोड चालू: डॉक्टर जवाब बोलकर सुनाएंगे।",
            AppLanguage.TAMIL to "குரல் முறை இயங்குகிறது: பதில்கள் ஒலி வடிவில் கேட்கும்.",
            AppLanguage.KANNADA to "ಧ್ವನಿ ಮೋಡ್ ಸಕ್ರಿಯ: ವೈದ್ಯರ ಉತ್ತರಗಳನ್ನು ಓದಿ ಹೇಳಲಾಗುತ್ತದೆ.",
            AppLanguage.BENGALI to "ভয়েস মোড চালু: উত্তরের অডিও শোনা যাবে।",
            AppLanguage.MARATHI to "व्हॉइस मोड सुरू: उत्तरे आवाजात ऐकवली जातील."
        ),

        StringKey.SCAN_DOCUMENTS_TITLE to mapOf(
            AppLanguage.ENGLISH to "Medical Reports & Prescriptions",
            AppLanguage.TELUGU to "వైద్య నివేదికలు & ప్రిస్క్రిప్షన్లు",
            AppLanguage.HINDI to "जांच रिपोर्ट और दवाई पर्ची",
            AppLanguage.TAMIL to "மருத்துவ அறிக்கைகள்",
            AppLanguage.KANNADA to "ವೈದ್ಯಕೀಯ ವರದಿಗಳು",
            AppLanguage.BENGALI to "মেডিকেল রিপোর্ট",
            AppLanguage.MARATHI to "वैद्यकीय अहवाल"
        ),

        StringKey.SCAN_CAMERA_BUTTON to mapOf(
            AppLanguage.ENGLISH to "Take Photo of Paper",
            AppLanguage.TELUGU to "కాగితం ఫోటో తీయండి",
            AppLanguage.HINDI to "पर्चे की फोटो लें",
            AppLanguage.TAMIL to "புகைப்படம் எடுக்கவும்",
            AppLanguage.KANNADA to "ಫೋಟೋ ತೆಗೆಯಿರಿ",
            AppLanguage.BENGALI to "ছবি তুলুন",
            AppLanguage.MARATHI to "फोटो काढा"
        ),

        StringKey.SCAN_GALLERY_BUTTON to mapOf(
            AppLanguage.ENGLISH to "Pick from Phone",
            AppLanguage.TELUGU to "ఫోన్ నుండి ఎంచుకోండి",
            AppLanguage.HINDI to "गैलरी से चुनें",
            AppLanguage.TAMIL to "கேலரியில் இருந்து தேர்ந்தெடுக்கவும்",
            AppLanguage.KANNADA to "ಗ್ಯಾಲರಿಯಿಂದ ಆಯ್ಕೆಮಾಡಿ",
            AppLanguage.BENGALI to "গ্যালারি থেকে বেছে নিন",
            AppLanguage.MARATHI to "गॅलरीतून निवडा"
        ),

        StringKey.PROFILE_TITLE to mapOf(
            AppLanguage.ENGLISH to "Aadhaar Health Profile",
            AppLanguage.TELUGU to "ఆధార్ ఆరోగ్య ప్రొఫైల్",
            AppLanguage.HINDI to "आधार स्वास्थ्य प्रोफाइल",
            AppLanguage.TAMIL to "ஆதார் சுகாதார விவரம்",
            AppLanguage.KANNADA to "ಆಧಾರ್ ಆರೋಗ್ಯ ಪ್ರೊಫೈಲ್",
            AppLanguage.BENGALI to "আধার স্বাস্থ্য প্রোফাইল",
            AppLanguage.MARATHI to "आधार आरोग्य प्रोफाइल"
        ),

        StringKey.AADHAAR_VERIFIED to mapOf(
            AppLanguage.ENGLISH to "Official Aadhaar Verified",
            AppLanguage.TELUGU to "ప్రభుత్వ ఆధార్ ధృవీకరించబడింది",
            AppLanguage.HINDI to "सरकारी आधार सत्यापित",
            AppLanguage.TAMIL to "அரசு ஆதார் சரிபார்க்கப்பட்டது",
            AppLanguage.KANNADA to "ಸರ್ಕಾರಿ ಆಧಾರ್ ಪರಿಶೀಲಿಸಲಾಗಿದೆ",
            AppLanguage.BENGALI to "সরকারি আধার যাচাইকৃত",
            AppLanguage.MARATHI to "शासकीय आधार पडताळणी झाली"
        )
    )

    fun get(key: StringKey, language: AppLanguage): String {
        return translations[key]?.get(language)
            ?: translations[key]?.get(AppLanguage.ENGLISH)
            ?: ""
    }

    /**
     * Spoken narrations for illiterate patients for any screen
     */
    fun getScreenNarration(screenTag: String, language: AppLanguage, userName: String?): String {
        val patientName = userName?.takeIf { it.isNotBlank() } ?: "Patient"
        return when (screenTag) {
            "dashboard" -> when (language) {
                AppLanguage.TELUGU -> "నమస్కారం $patientName. ఇది మీ ఆరోగ్యకేర్ హోమ్ స్క్రీన్. ఇక్కడ మీ హాస్పిటల్ OP టోకెన్, తీసుకోవలసిన మందుల వివరాలు మరియు అత్యవసర 108 నంబర్ ఉన్నాయి. వినడానికి ఏదైనా కార్డుపై లేదా మైక్రోఫోన్ పై నొక్కండి."
                AppLanguage.HINDI -> "नमस्ते $patientName. यह आपका आरोग्यकेयर होम स्क्रीन है। यहाँ आपका अस्पताल OP टोकन, दवाई का समय और 108 इमरजेंसी नंबर है। सुनने के लिए किसी भी कार्ड को दबाएं।"
                AppLanguage.TAMIL -> "வணக்கம் $patientName. இது உங்கள் ஆரோக்கியகேர் முகப்பு பக்கம். உங்கள் மருத்துவமனை டோக்கன் மற்றும் மருந்து விவரங்கள் இங்கே உள்ளன."
                AppLanguage.KANNADA -> "ನಮಸ್ಕಾರ $patientName. ಇದು ನಿಮ್ಮ ಆರೋಗ್ಯಕೇರ್ ಮುಖಪುಟ. ನಿಮ್ಮ ಆಸ್ಪತ್ರೆ ಟೋಕನ್ ಮತ್ತು ಔಷಧಿಯ ವಿವರಗಳು ಇಲ್ಲಿವೆ."
                AppLanguage.BENGALI -> "নমস্কার $patientName. এটি আপনার আরোগ্যকেয়ার হোম স্ক্রিন। এখানে আপনার হাসপাতালের টোকেন ও ওষুধের সময়সূচি রয়েছে।"
                AppLanguage.MARATHI -> "नमस्कार $patientName. हे आपले आरोग्यकेअर मुख्यपृष्ठ आहे. येथे आपले रुग्णालय टोकन आणि औषधांचे वेळापत्रक आहे."
                else -> "Welcome $patientName. This is your AarogyaCare home screen. You can view your hospital OP tickets, upcoming medicines, and emergency 108 service here."
            }
            "medications" -> when (language) {
                AppLanguage.TELUGU -> "ఇది మీ మందుల సమయాల పేజీ. డాక్టర్ సూచించిన మాత్రలు ఎప్పుడు, ఎలా వేసుకోవాలో ఇక్కడ వినవచ్చు. మందు వేసుకున్న తర్వాత 'తీసుకున్నాను' బటన్ నొక్కండి."
                AppLanguage.HINDI -> "यह आपकी दवाईयों का पेज है। डॉक्टर द्वारा बताई गई दवाइयां कब और कैसे लेनी हैं, यहाँ सुन सकते हैं। दवाई लेने के बाद 'दवाई ले ली' बटन दबाएं।"
                AppLanguage.TAMIL -> "இது உங்கள் மருந்துகள் பக்கம். மருத்துவர் கூறிய மருந்துகளை எப்போது எடுத்துக்கொள்ள வேண்டும் என்பதை இங்கே கேட்கலாம்."
                AppLanguage.KANNADA -> "ಇದು ನಿಮ್ಮ ಔಷಧಿಗಳ ಪುಟ. ವೈದ್ಯರು ಸೂಚಿಸಿದ ಔಷಧಿಗಳನ್ನು ಯಾವಾಗ ತೆಗೆದುಕೊಳ್ಳಬೇಕೆಂದು ಇಲ್ಲಿ ಕೇಳಬಹುದು."
                AppLanguage.BENGALI -> "এটি আপনার ওষুধের তালিকা। ডাক্তার নির্দেশিত ওষুধ কখন খেতে হবে তা এখানে শুনতে পারেন।"
                AppLanguage.MARATHI -> "हे आपल्या औषधांचे पान आहे. डॉक्टरांनी सांगितलेली औषधे केव्हा घ्यायची ते येथे ऐकू शकता."
                else -> "This is your medication reminders screen. Listen to dosages and timings for your prescribed medicines, and tap mark taken when finished."
            }
            "chatbot" -> when (language) {
                AppLanguage.TELUGU -> "ఇది AI డాక్టర్ సహాయ కేంద్రం. మీకు ఉన్న ఆరోగ్య సమస్య లేదా నొప్పులను మైక్రోఫోన్ నొక్కి మాట్లాడండి. డాక్టర్ వెంటనే సహాయం చేస్తారు."
                AppLanguage.HINDI -> "यह AI डॉक्टर सहायता केंद्र है। अपनी बीमारी या परेशानी बताने के लिए माइक दबाकर बोलें। डॉक्टर तुरंत मदद करेंगे।"
                AppLanguage.TAMIL -> "இது AI மருத்துவ உதவி மையம். உங்கள் உடல் உபாதைகளை மைக் அழுத்தி பேசலாம்."
                AppLanguage.KANNADA -> "ಇದು AI ವೈದ್ಯರ ನೆರವು ಕೇಂದ್ರ. ನಿಮ್ಮ ಆರೋಗ್ಯದ ಸಮಸ್ಯೆಯನ್ನು ಮೈಕ್ ಒತ್ತಿ ಮಾತನಾಡಿ."
                AppLanguage.BENGALI -> "এটি এআই ডাক্তার সহায়তা কেন্দ্র। আপনার সমস্যার কথা বলতে মাইক চাপুন।"
                AppLanguage.MARATHI -> "हे AI डॉक्टर मदत केंद्र आहे. आपली समस्या सांगण्यासाठी माइक दाबून बोला."
                else -> "This is the AI Doctor triage center. Tap the microphone to speak your symptoms, and the assistant will guide you with advice."
            }
            "reports" -> when (language) {
                AppLanguage.TELUGU -> "ఇది మీ మెడికల్ రిపోర్టులు మరియు ప్రిస్క్రిప్షన్ పేజీ. కొత్త రిపోర్ట్ కాగితం ఉంటే కెమెరాతో ఫోటో తీయండి. దాని సారాంశం చదివి వినిపిస్తాము."
                AppLanguage.HINDI -> "यह आपकी जांच रिपोर्ट का पेज है। नई रिपोर्ट की फोटो लेने के लिए कैमरा बटन दबाएं। हम उसे पढ़कर सुनाएंगे।"
                AppLanguage.TAMIL -> "இது உங்கள் மருத்துவ அறிக்கை பக்கம். அறிக்கையை புகைப்படம் எடுக்க கேமரா பொத்தானை அழுத்தவும்."
                AppLanguage.KANNADA -> "ಇದು ನಿಮ್ಮ ವೈದ್ಯಕೀಯ ವರದಿಗಳ ಪುಟ. ವರದಿಯ ಫೋಟೋ ತೆಗೆಯಲು ಕ್ಯಾಮೆರಾ ಒತ್ತಿರಿ."
                AppLanguage.BENGALI -> "এটি আপনার মেডিকেল রিপোর্ট পেজ। রিপোর্টের ছবি তুলতে ক্যামেরা চাপুন।"
                AppLanguage.MARATHI -> "हे आपले वैद्यकीय अहवाल पान आहे. अहवालाचा फोटो काढण्यासाठी कॅमेरा दाबा."
                else -> "This is your medical reports screen. Take a photo of your test paper to hear an easy summary read aloud."
            }
            "analytics" -> when (language) {
                AppLanguage.TELUGU -> "ఇది మీ రక్తపోటు, షుగర్ మరియు ఆరోగ్య ట్రెండ్స్ చార్టులు. మీ ఆరోగ్యం బాగుందో లేదో ఇక్కడ సులభంగా తెలుసుకోవచ్చు."
                AppLanguage.HINDI -> "यह आपका ब्लड प्रेशर और शुगर चार्ट है। यहाँ आप अपने स्वास्थ्य का सुधार देख सकते हैं।"
                AppLanguage.TAMIL -> "இது உங்கள் இரத்த அழுத்தம் மற்றும் சர்க்கரை அளவு காட்டும் வரைபடம்."
                AppLanguage.KANNADA -> "ಇದು ನಿಮ್ಮ ರಕ್ತದೊತ್ತಡ ಮತ್ತು ಸಕ್ಕರೆ ಪ್ರಮಾಣದ ಚಾರ್ಟ್."
                AppLanguage.BENGALI -> "এটি আপনার রক্তচাপ এবং সুগারের চার্ট।"
                AppLanguage.MARATHI -> "हे आपले रक्तदाब आणि साखर तपासणी आलेख आहे."
                else -> "This is your health trends and vitals screen showing blood pressure and blood glucose history."
            }
            "summary" -> when (language) {
                AppLanguage.TELUGU -> "ఇది డాక్టర్ క్లినికల్ సారాంశం. హాస్పిటల్ లో డాక్టర్ కి చూపించడానికి సిద్ధం చేసిన వివరాలు ఇక్కడ ఉన్నాయి."
                AppLanguage.HINDI -> "यह डॉक्टर के लिए क्लिनिकल सारांश है। अस्पताल में डॉक्टर को दिखाने के लिए यह पर्चा तैयार है।"
                AppLanguage.TAMIL -> "இது மருத்துவருக்கான சுருக்க அறிக்கை."
                AppLanguage.KANNADA -> "ಇದು ವೈದ್ಯರ ಕ್ಲಿನಿಕಲ್ ಸಾರಾಂಶ."
                AppLanguage.BENGALI -> "এটি ডাক্তারের জন্য ক্লিনিকাল সারাংশ।"
                AppLanguage.MARATHI -> "हा डॉक्टरांसाठी वैद्यकीय सारांश आहे."
                else -> "This is your doctor clinical summary report prepared for hospital consultations."
            }
            "profile" -> when (language) {
                AppLanguage.TELUGU -> "ఇది మీ అధికారిక ఆధార్ మరియు ABHA డిజిటల్ హెల్త్ ఐడి ప్రొఫైల్ వివరాలు."
                AppLanguage.HINDI -> "यह आपका आधिकारिक आधार और आभा स्वास्थ्य पहचान पत्र है।"
                AppLanguage.TAMIL -> "இது உங்கள் அதிகாரப்பூர்வ ஆதார் மற்றும் ABHA விவரங்கள்."
                AppLanguage.KANNADA -> "ಇದು ನಿಮ್ಮ ಆಧಾರ್ ಮತ್ತು ABHA ವಿವರಗಳು."
                AppLanguage.BENGALI -> "এটি আপনার আধার ও এবিএইচএ কার্ডের তথ্য।"
                AppLanguage.MARATHI -> "हे आपले आधार आणि ABHA आरोग्य ओळखपत्र आहे."
                else -> "This is your official Aadhaar and ABHA Health ID profile."
            }
            else -> "AarogyaCare Healthcare Portal."
        }
    }

    /**
     * Spoken audio for an OP ticket
     */
    fun getOpTicketSpokenText(
        tokenNumber: Int,
        hospitalName: String,
        department: String,
        roomNumber: String,
        estimatedWaitTimeMinutes: Int,
        language: AppLanguage
    ): String {
        return when (language) {
            AppLanguage.TELUGU -> "మీ టోకెన్ నంబర్ $tokenNumber. $hospitalName లోని $department విభాగం, గది నంబర్ $roomNumber. వేచి ఉండే సమయం సుమారు $estimatedWaitTimeMinutes నిమిషాలు."
            AppLanguage.HINDI -> "आपका टोकन नंबर $tokenNumber है। $hospitalName के $department विभाग में, कमरा नंबर $roomNumber. अनुमानित प्रतीक्षा समय $estimatedWaitTimeMinutes मिनट है।"
            AppLanguage.TAMIL -> "உங்கள் டோக்கன் எண் $tokenNumber. $hospitalName, $department பிரிவு, அறை எண் $roomNumber. காத்திருப்பு நேரம் $estimatedWaitTimeMinutes நிமிடங்கள்."
            AppLanguage.KANNADA -> "ನಿಮ್ಮ ಟೋಕನ್ ಸಂಖ್ಯೆ $tokenNumber. $hospitalName ಆಸ್ಪತ್ರೆಯ $department ವಿಭಾಗ, ಕೊಠಡಿ $roomNumber. ಕಾಯುವ ಸಮಯ $estimatedWaitTimeMinutes ನಿಮಿಷಗಳು."
            AppLanguage.BENGALI -> "আপনার টোকেন নম্বর $tokenNumber. $hospitalName এর $department বিভাগে, রুম নম্বর $roomNumber. অপেক্ষার সময় $estimatedWaitTimeMinutes মিনিট।"
            AppLanguage.MARATHI -> "आपला टोकन क्रमांक $tokenNumber आहे. $hospitalName च्या $department विभागात, खोली क्रमांक $roomNumber. अंदाजे वेळ $estimatedWaitTimeMinutes मिनिटे."
            else -> "Your token number is $tokenNumber at $hospitalName, $department department, Room $roomNumber. Estimated wait time is $estimatedWaitTimeMinutes minutes."
        }
    }

    /**
     * Spoken audio for a medication reminder
     */
    fun getMedicationSpokenText(
        medicineName: String,
        dosage: String,
        instructions: String,
        time: String,
        language: AppLanguage
    ): String {
        return when (language) {
            AppLanguage.TELUGU -> "తీసుకోవలసిన మందు: $medicineName, మోతాదు $dosage. సమయం $time. సూచన: $instructions."
            AppLanguage.HINDI -> "दवाई का नाम: $medicineName, मात्रा $dosage. समय $time. सलाह: $instructions."
            AppLanguage.TAMIL -> "மருந்து: $medicineName, அளவு $dosage. நேரம் $time. வழிமுறை: $instructions."
            AppLanguage.KANNADA -> "ಔಷಧಿ: $medicineName, ಪ್ರಮಾಣ $dosage. ಸಮಯ $time. ಸೂಚನೆ: $instructions."
            AppLanguage.BENGALI -> "ওষুধ: $medicineName, মাত্রা $dosage. সময় $time. নির্দেশ: $instructions."
            AppLanguage.MARATHI -> "औषध: $medicineName, डोस $dosage. वेळ $time. सूचना: $instructions."
            else -> "Medicine to take: $medicineName, dosage $dosage at $time. Instructions: $instructions."
        }
    }
}

/**
 * Pre-defined symptom voice prompt options with icons for illiterate patients to tap or listen to
 */
data class QuickVoiceSymptom(
    val id: String,
    val iconEmoji: String,
    val queryKey: String,
    val titles: Map<AppLanguage, String>,
    val spokenPhrases: Map<AppLanguage, String>
)

object QuickVoiceSymptomsRepository {
    val symptoms = listOf(
        QuickVoiceSymptom(
            id = "chest_pain",
            iconEmoji = "❤️",
            queryKey = "Severe chest pain and sweating",
            titles = mapOf(
                AppLanguage.ENGLISH to "Chest Pain & Sweating",
                AppLanguage.TELUGU to "గుండెల్లో నొప్పి & చెమటలు",
                AppLanguage.HINDI to "सीने में दर्द और पसीना",
                AppLanguage.TAMIL to "நெஞ்சு வலி & வியர்வை",
                AppLanguage.KANNADA to "ಎದೆ ನೋವು & ಬೆವರು",
                AppLanguage.BENGALI to "বুকে ব্যথা ও ঘাম",
                AppLanguage.MARATHI to "छातीत दुखणे आणि घाम"
            ),
            spokenPhrases = mapOf(
                AppLanguage.ENGLISH to "I have severe chest pain and heavy sweating. What should I do immediately?",
                AppLanguage.TELUGU to "నాకు గుండెల్లో తీవ్రమైన నొప్పి వస్తోంది మరియు విపరీతంగా చెమటలు పడుతున్నాయి. నేను వెంటనే ఏమి చేయాలి?",
                AppLanguage.HINDI to "मुझे सीने में बहुत तेज दर्द हो रहा है और पसीना आ रहा है। मुझे तुरंत क्या करना चाहिए?",
                AppLanguage.TAMIL to "எனக்கு நெஞ்சு வலி அதிகமாக உள்ளது மற்றும் வியர்க்கிறது. நான் என்ன செய்ய வேண்டும்?",
                AppLanguage.KANNADA to "ನನಗೆ ಎದೆಯಲ್ಲಿ ತುಂಬಾ ನೋವಾಗುತ್ತಿದೆ ಮತ್ತು ಬೆವರು ಬರುತ್ತಿದೆ. ನಾನೀಗ ಏನು ಮಾಡಬೇಕು?",
                AppLanguage.BENGALI to "আমার বুকে খুব ব্যথা এবং প্রচুর ঘাম হচ্ছে। এখন আমার কী করা উচিত?",
                AppLanguage.MARATHI to "माझ्या छातीत खूप दुखत आहे आणि घाम येत आहे. मी तातडीने काय करावे?"
            )
        ),
        QuickVoiceSymptom(
            id = "fever_cough",
            iconEmoji = "🌡️",
            queryKey = "High fever with cough and throat pain",
            titles = mapOf(
                AppLanguage.ENGLISH to "High Fever & Cough",
                AppLanguage.TELUGU to "తీవ్రమైన జ్వరం & దగ్గు",
                AppLanguage.HINDI to "तेज बुखार और खांसी",
                AppLanguage.TAMIL to "அதிக காய்ச்சல் & இருமல்",
                AppLanguage.KANNADA to "ತೀವ್ರ ಜ್ವರ & ಕೆಮ್ಮು",
                AppLanguage.BENGALI to "তীব্র জ্বর ও কাশি",
                AppLanguage.MARATHI to "तीव्र ताप आणि खोकला"
            ),
            spokenPhrases = mapOf(
                AppLanguage.ENGLISH to "I have high fever for two days with severe cough and shivering.",
                AppLanguage.TELUGU to "నాకు రెండు రోజుల నుండి తీవ్రమైన జ్వరం, దగ్గు మరియు చలి వస్తున్నాయి.",
                AppLanguage.HINDI to "मुझे दो दिनों से तेज बुखार, खांसी और कंपकंपी हो रही है।",
                AppLanguage.TAMIL to "எனக்கு இரண்டு நாட்களாக அதிக காய்ச்சலும் இருமலும் உள்ளது.",
                AppLanguage.KANNADA to "ನನಗೆ ಎರಡು ದಿನಗಳಿಂದ ತೀವ್ರ ಜ್ವರ ಮತ್ತು ಕೆಮ್ಮು ಇದೆ.",
                AppLanguage.BENGALI to "আমার দুদিন ধরে খুব জ্বর এবং তীব্র কাশি হচ্ছে।",
                AppLanguage.MARATHI to "मला दोन दिवसांपासून खूप ताप आणि खोकला आहे."
            )
        ),
        QuickVoiceSymptom(
            id = "stomach_pain",
            iconEmoji = "🤢",
            queryKey = "Severe stomach pain and vomiting",
            titles = mapOf(
                AppLanguage.ENGLISH to "Stomach Pain & Vomiting",
                AppLanguage.TELUGU to "కడుపు నొప్పి & వాంతులు",
                AppLanguage.HINDI to "पेट दर्द और उल्टी",
                AppLanguage.TAMIL to "வயிற்று வலி & வாந்தி",
                AppLanguage.KANNADA to "ಹೊಟ್ಟೆ ನೋವು & ವಾಂತಿ",
                AppLanguage.BENGALI to "পেটে ব্যথা ও বমি",
                AppLanguage.MARATHI to "पोटदुखी आणि उलट्या"
            ),
            spokenPhrases = mapOf(
                AppLanguage.ENGLISH to "I have severe stomach pain, acidity and repeated vomiting since morning.",
                AppLanguage.TELUGU to "ఉదయం నుండి కడుపులో విపరీతమైన నొప్పి మరియు వాంతులు అవుతున్నాయి.",
                AppLanguage.HINDI to "सुबह से पेट में बहुत तेज दर्द और बार-बार उल्टी हो रही है।",
                AppLanguage.TAMIL to "காலையிலிருந்து கடுமையான வயிற்று வலியும் வாந்தியும் உள்ளது.",
                AppLanguage.KANNADA to "ಬೆಳಗಿನಿಂದ ಹೊಟ್ಟೆಯಲ್ಲಿ ವಿಪರೀತ ನೋವು ಮತ್ತು ವಾಂತಿಯಾಗುತ್ತಿದೆ.",
                AppLanguage.BENGALI to "সকাল থেকে পেটে তীব্র ব্যথা এবং বারবার বমি হচ্ছে।",
                AppLanguage.MARATHI to "सकाळपासून पोटात तीव्र वेदना आणि उलट्या होत आहेत."
            )
        ),
        QuickVoiceSymptom(
            id = "dizziness_sugar",
            iconEmoji = "💫",
            queryKey = "Dizziness weakness and diabetes check",
            titles = mapOf(
                AppLanguage.ENGLISH to "Dizziness & Weakness",
                AppLanguage.TELUGU to "కళ్ళు తిరగడం & నీరసం",
                AppLanguage.HINDI to "चक्कर आना और कमजोरी",
                AppLanguage.TAMIL to "தலைச்சுற்றல் & சோர்வு",
                AppLanguage.KANNADA to "ತಲೆತಿರುಗುವಿಕೆ & ಸುಸ್ತು",
                AppLanguage.BENGALI to "মাথা ঘোরা ও দুর্বলতা",
                AppLanguage.MARATHI to "चक्कर येणे आणि अशक्तपणा"
            ),
            spokenPhrases = mapOf(
                AppLanguage.ENGLISH to "I feel very dizzy, hands are trembling and feeling extremely weak.",
                AppLanguage.TELUGU to "నాకు కళ్ళు తిరుగుతున్నాయి, చేతులు వణుకుతున్నాయి మరియు విపరీతమైన నీరసంగా ఉంది.",
                AppLanguage.HINDI to "मुझे चक्कर आ रहे हैं, हाथ कांप रहे हैं और बहुत ज्यादा कमजोरी लग रही है।",
                AppLanguage.TAMIL to "எனக்கு தலை சுற்றுகிறது மற்றும் உடல் மிகவும் பலவீனமாக உள்ளது.",
                AppLanguage.KANNADA to "ನನಗೆ ತಲೆ ತಿರುಗುತ್ತಿದೆ, ಕೈ ನಡುಗುತ್ತಿದೆ ಮತ್ತು ವಿಪರೀತ ಸುಸ್ತಾಗಿದೆ.",
                AppLanguage.BENGALI to "আমার মাথা ঘুরছে এবং শরীর খুব দুর্বল লাগছে।",
                AppLanguage.MARATHI to "मला चक्कर येत आहे, हात थरथरत आहेत आणि खूप अशक्त वाटत आहे."
            )
        ),
        QuickVoiceSymptom(
            id = "med_question",
            iconEmoji = "💊",
            queryKey = "How to take my prescribed medicines?",
            titles = mapOf(
                AppLanguage.ENGLISH to "Medicine Questions",
                AppLanguage.TELUGU to "మందులు ఎలా వేసుకోవాలి?",
                AppLanguage.HINDI to "दवाई कैसे लेनी है?",
                AppLanguage.TAMIL to "மருந்து சாப்பிடும் முறை",
                AppLanguage.KANNADA to "ಔಷಧಿ ಹೇಗೆ ತೆಗೆದುಕೊಳ್ಳಬೇಕು?",
                AppLanguage.BENGALI to "ওষুধ কীভাবে খাব?",
                AppLanguage.MARATHI to "औषध कसे घ्यायचे?"
            ),
            spokenPhrases = mapOf(
                AppLanguage.ENGLISH to "Can you tell me how and at what times I should take my prescribed medicines?",
                AppLanguage.TELUGU to "నా డాక్టర్ ఇచ్చిన మందులను ఏ సమయానికి మరియు భోజనం ముందా లేదా తర్వాతా ఎలా వేసుకోవాలో చెప్పండి.",
                AppLanguage.HINDI to "कृपया बताएं कि मुझे अपनी दवाइयां किस समय और खाने से पहले या बाद में कैसे लेनी हैं?",
                AppLanguage.TAMIL to "என் மருந்துகளை எந்த நேரத்தில் எப்படி சாப்பிட வேண்டும் என்று சொல்லுங்கள்.",
                AppLanguage.KANNADA to "ನನ್ನ ಔಷಧಿಗಳನ್ನು ಯಾವ ಸಮಯದಲ್ಲಿ ಮತ್ತು ಹೇಗೆ ತೆಗೆದುಕೊಳ್ಳಬೇಕೆಂದು ತಿಳಿಸಿ.",
                AppLanguage.BENGALI to "আমার ওষুধগুলো কখন এবং কীভাবে খেতে হবে দয়া করে বলুন।",
                AppLanguage.MARATHI to "कृपया सांगा की माझी औषधे केव्हा आणि कशी घ्यायची?"
            )
        )
    )
}
