package com.example.kakaovisionsdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.kakaovisionsdk.databinding.ActivityMainBinding
import com.example.kakaovisionsdk.face.FaceDetectActivity
import com.example.kakaovisionsdk.ocr.OcrActivity
import com.example.kakaovisionsdk.thumbnail.ThumbnailCropActivity
import com.example.kakaovisionsdk.thumbnail.ThumbnailCropUrlActivity
import com.example.kakaovisionsdk.translate.TranslateActivity
import com.example.vision.VisionApiClient
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.user.UserApiClient


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
                Item.Header("User API"),
                Item.ApiItem("KakaoTalk Login") {
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                            if (error != null) {
                                makeSnackbar("로그인 실패", error)
                            } else {
                                makeSnackbar("로그인 성공\n${token}")
                            }
                        }
                    } else {
                        makeSnackbar("카카오톡을 설치해주세요")
                    }
                },
                Item.ApiItem("Kakao Account Login") {
                    UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                        if (error != null) {
                            makeSnackbar("로그인 실패", error)
                        } else {
                            makeSnackbar("로그인 성공\n${token}")
                        }
                    }
                },
                Item.ApiItem("Logout") {
                    UserApiClient.instance.logout { error ->
                        if (error != null) {
                            makeSnackbar("로그아웃 실패", error)
                        } else {
                            makeSnackbar("로그아웃 성공")
                        }
                    }
                },
                Item.ApiItem("unlink") {
                    UserApiClient.instance.unlink { error ->
                        if (error != null) {
                            makeSnackbar("연결 끊기 실패", error)
                        } else {
                            makeSnackbar("연결 끊기 성공")
                        }
                    }
                },
                Item.Header("Kakao Vision API"),
                Item.ApiItem("OCR - using device image") {
                    startActivity(Intent(this, OcrActivity::class.java))
                },
                Item.ApiItem("Translate") {
                    startActivity(Intent(this, TranslateActivity::class.java))
                },
                Item.ApiItem("Create Thumbnail - using device image") {
                    startActivity(Intent(this, ThumbnailCropActivity::class.java))
                },
                Item.ApiItem("Create Thumbnail - using web image url") {
                    startActivity(Intent(this, ThumbnailCropUrlActivity::class.java))
                },
                Item.ApiItem("Detect Thumbnail - using device image") {
                    VisionApiClient.instance.detectThumbnailImage(
                        this,
                        400,
                        400
                    ) { result, error ->
                        if (error != null) {
                            makeSnackbar(error.message.toString(), error)
                        } else if (result != null) {
                            makeSnackbar("$result")
                        }
                    }
                },
                Item.ApiItem("Detect Thumbnail - using web image url") {
                    VisionApiClient.instance.detectThumbnailImage(
                        RYAN_IMAGE,
                        400,
                        400
                    ) { result, error ->
                        if (error != null) {
                            makeSnackbar(error.message.toString(), error)
                        } else if (result != null) {
                            makeSnackbar("$result")
                        }
                    }
                },
                Item.ApiItem("Detect Face - using device image") {
                    VisionApiClient.instance.detectFace(this) { result, error ->
                        if (error != null) {
                            makeSnackbar(error.message.toString(), error)
                        } else if (result != null) {
                            makeSnackbar("$result")
                        }
                    }
                },
                Item.ApiItem("Detect Face - using web image url") {
                    startActivity(Intent(this, FaceDetectActivity::class.java))
                },
            )
        )

        binding.recyclerView.apply {
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = this@MainActivity.adapter
        }
    }

    private fun makeSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        Log.d("Kakao Vision", message)
    }

    private fun makeSnackbar(message: String, error: Throwable) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        Log.e("Kakao Vision", message, error)
    }

    companion object {
        const val RYAN_IMAGE =
            "https://img1.daumcdn.net/thumb/R720x0/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fliveboard%2Fbloter%2F74b9579a3b584e06ad39cc706d25daee.jpg"
        const val FACE_SAMPLE_IMAGE =
            "https://previews.123rf.com/images/tomwang/tomwang1412/tomwang141200083/34415958-%ED%9D%B0%EC%83%89%EC%97%90-%EA%B3%A0%EB%A6%BD-%EB%90%9C-%EA%B7%BC%EC%A0%91-%EC%B4%AC%EC%98%81-%EC%A0%8A%EC%9D%80-%EC%82%AC%EB%9E%8C-%EC%96%BC%EA%B5%B4.jpg"
    }
}