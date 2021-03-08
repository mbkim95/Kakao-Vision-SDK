package com.example.kakaovisionsdk

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.kakaovisionsdk.databinding.ActivityMainBinding
import com.example.vision.VisionApiClient
import com.example.vision.model.translate.Language


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ApiAdapter
    private lateinit var sentence: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ApiAdapter(
            listOf(
                Item.Header("Kakao AI API"),
                Item.ApiItem("OCR - using device image") {
                    VisionApiClient.instance.getOcrResult(this) {
                        val sb = StringBuilder()
                        it.ocrDetailResult.forEach { result ->
                            result.recognitionWords.forEach { text ->
                                sb.append(text)
                            }
                        }
                        sentence = sb.toString()
                        makeToast(sentence)
                    }
                },
                Item.ApiItem("Translate - English to Korean") {
                    if (this::sentence.isInitialized) {
                        VisionApiClient.instance.translateSentence(
                            sentence,
                            Language.ENGLISH,
                            Language.KOREAN
                        ) { result ->
                            val sb = StringBuilder()
                            result.translatedText.forEach { list ->
                                list.forEach {
                                    sb.append("$it ")
                                }
                            }
                            makeToast(sb.toString())
                        }
                    } else {
                        makeToast("OCR을 먼저 실행해주세요")
                    }
                },
                Item.ApiItem("Create Thumbnail - using device image") {
                    VisionApiClient.instance.createThumbnailImage(this, 400, 400) {
                        makeToast(it.thumbnailImageUrl)
                    }
                },
                Item.ApiItem("Create Thumbnail - using web image url") {
                    VisionApiClient.instance.createThumbnailImage(RYAN_IMAGE, 400, 400) {
                        makeToast(it.thumbnailImageUrl)
                    }
                },
                Item.ApiItem("Detect Thumbnail - using device image") {
                    VisionApiClient.instance.detectThumbnailImage(this, 400, 400) {
                        makeToast("$it")
                    }
                },
                Item.ApiItem("Detect Thumbnail - using web image url") {
                    VisionApiClient.instance.detectThumbnailImage(RYAN_IMAGE, 400, 400) {
                        makeToast("$it")
                    }
                },
                Item.ApiItem("Detect Face - using device image") {
                    VisionApiClient.instance.detectFace(this) {
                        makeToast("$it")
                    }
                },
                Item.ApiItem("Detect Face - using web image url") {
                    VisionApiClient.instance.detectFace(FACE_SAMPLE_IMAGE, 0.4F) {
                        makeToast("$it")
                    }
                }
            )
        )

        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = this@MainActivity.adapter
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d("Kakao Vision", message)
    }

    companion object {
        const val RYAN_IMAGE =
            "https://img1.daumcdn.net/thumb/R720x0/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fliveboard%2Fbloter%2F74b9579a3b584e06ad39cc706d25daee.jpg"
        const val FACE_SAMPLE_IMAGE =
            "https://previews.123rf.com/images/tomwang/tomwang1412/tomwang141200083/34415958-%ED%9D%B0%EC%83%89%EC%97%90-%EA%B3%A0%EB%A6%BD-%EB%90%9C-%EA%B7%BC%EC%A0%91-%EC%B4%AC%EC%98%81-%EC%A0%8A%EC%9D%80-%EC%82%AC%EB%9E%8C-%EC%96%BC%EA%B5%B4.jpg"
    }
}