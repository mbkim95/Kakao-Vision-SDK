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
                    Log.d("Kakao Vision", "onCreate: $it")
                    extractedTv.text = it
                }
            }
        }
    }
}