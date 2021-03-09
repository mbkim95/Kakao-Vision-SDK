package com.example.kakaovisionsdk.thumbnail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kakaovisionsdk.R
import com.example.kakaovisionsdk.databinding.ActivityThumbnailCropBinding
import com.example.vision.VisionApiClient
import com.google.android.material.snackbar.Snackbar

class ThumbnailCropActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThumbnailCropBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThumbnailCropBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            createBtn.setOnClickListener {
                if (widthEt.text.isNullOrEmpty() || heightEt.text.isNullOrEmpty()) {
                    makeSnackbar("값을 입력해주세요")
                    return@setOnClickListener
                }
                VisionApiClient.instance.createThumbnailImage(
                    this@ThumbnailCropActivity,
                    widthEt.text.toString().toInt(),
                    heightEt.text.toString().toInt()
                ) { result, error ->
                    if (error != null) {
                        makeSnackbar("썸네일 생성 실패\n${error}")
                    } else {
                        makeSnackbar("${result?.thumbnailImageUrl}")
                        result?.let {
                            Glide.with(this@ThumbnailCropActivity)
                                .load(it.thumbnailImageUrl)
                                .error(R.drawable.ic_launcher_foreground)
                                .into(targetImg)
                        }
                    }
                }

            }
        }
    }

    private fun makeSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        Log.d("Kakao Vision", message)
    }
}