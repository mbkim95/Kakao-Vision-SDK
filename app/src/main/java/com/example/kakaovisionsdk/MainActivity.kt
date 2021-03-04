package com.example.kakaovisionsdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaovisionsdk.databinding.ActivityMainBinding
import com.example.vision.VisionApiClient
import com.example.vision.model.Language


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            selectBtn.setOnClickListener {
                VisionApiClient.instance.getOcrResult(applicationContext, lineBreak = false) {
                    extractedTv.text = it
                }
            }
            translateBtn.setOnClickListener {
                VisionApiClient.instance.translateSentence(
                    extractedTv.text.toString(),
                    Language.ENGLISH,
                    Language.KOREAN
                ) {
                    resultTv.text = it
                }
            }
        }
    }
}