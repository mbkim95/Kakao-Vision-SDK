package com.example.kakaovisionsdk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaovisionsdk.databinding.ActivityMainBinding
import com.example.vision.VisionApiClient


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
//                VisionApiClient.instance.translateSentence(
//                    extractedTv.text.toString(),
//                    Language.ENGLISH,
//                    Language.KOREAN
//                ) {
//                    resultTv.text = it
//                }
//                VisionApiClient.instance.getThumbnailImage(
//                    "https://img1.daumcdn.net/thumb/R720x0/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fliveboard%2Fbloter%2F74b9579a3b584e06ad39cc706d25daee.jpg",
//                    300,
//                    300
//                )
                VisionApiClient.instance.getThumbnailImage(this@MainActivity, 300, 300) {
                    Log.d("Kakao Vision", "onCreate: $it")
                    resultTv.text = it
                }
            }
        }
    }
}