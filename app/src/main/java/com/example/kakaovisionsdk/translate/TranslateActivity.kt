package com.example.kakaovisionsdk.translate

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaovisionsdk.databinding.ActivityTranslateBinding
import com.example.vision.VisionApiClient
import com.example.vision.model.translate.Language
import com.google.android.material.snackbar.Snackbar

class TranslateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTranslateBinding
    private val langList = listOf(
        "Korean",
        "English",
        "Japanese",
        "Chinese",
        "Vietnamese",
        "Bahasa Indonesia",
        "Arabic",
        "Banglaore",
        "German",
        "Spanish",
        "French",
        "Hindi",
        "Italian",
        "Bahasa Malaysia",
        "Frisian",
        "Portuguese",
        "Russian",
        "Thai",
        "Turkish"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTranslateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            sourceLang.adapter = ArrayAdapter(
                this@TranslateActivity,
                android.R.layout.simple_spinner_item,
                langList
            )
            targetLang.adapter = ArrayAdapter(
                this@TranslateActivity,
                android.R.layout.simple_spinner_item,
                langList
            )
            targetLang.setSelection(1)

            translateBtn.setOnClickListener {
                VisionApiClient.instance.translateSentence(
                    sourceEt.text.toString(),
                    sourceLang.selectedItem.toString().convertToLanguage(),
                    targetLang.selectedItem.toString().convertToLanguage()
                ) { result, error ->
                    if (error != null) {
                        Snackbar.make(binding.root, "번역에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
                    } else {
                        val sb = StringBuilder()
                        result?.translatedText?.forEach { list ->
                            list.forEach {
                                sb.append("$it ")
                            }
                        }
                        targetTv.text = sb.toString()
                    }
                }
            }
        }
    }

    private fun String.convertToLanguage(): Language {
        return when (this) {
            "Korean" -> Language.KOREAN
            "English" -> Language.ENGLISH
            "Japanese" -> Language.JAPANESE
            "Chinese" -> Language.CHINESE
            "Vietnamese" -> Language.VIETNAMESE
            "Bahasa Indonesia" -> Language.BAHASA_INDONESIA
            "Arabic" -> Language.ARABIC
            "Banglaore" -> Language.BANGALORE
            "German" -> Language.GERMAN
            "Spanish" -> Language.SPANISH
            "French" -> Language.FRENCH
            "Hindi" -> Language.HINDI
            "Italian" -> Language.ITALIAN
            "Bahasa Malaysia" -> Language.BAHASA_MALAYSIA
            "Frisian" -> Language.FRISIAN
            "Portuguese" -> Language.PORTUGUESE
            "Russian" -> Language.RUSSIAN
            "Thai" -> Language.THAI
            "Turkish" -> Language.TURKISH
            else -> Language.KOREAN
        }
    }
}