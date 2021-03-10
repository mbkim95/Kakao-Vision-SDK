package com.example.kakaovisionsdk.ocr

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaovisionsdk.databinding.ActivityOcrBinding
import com.example.kakaovisionsdk.friend.FriendItem
import com.example.kakaovisionsdk.friend.FriendsActivity
import com.example.vision.VisionApiClient
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate

class OcrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOcrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOcrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            ocrBtn.setOnClickListener {
                VisionApiClient.instance.getOcrResult(this@OcrActivity) { result, error ->
                    if (error != null) {
                        makeSnackbar("OCR 실패\n${error}")
                    } else {
                        val sb = StringBuilder()
                        result?.ocrDetailResult?.forEach {
                            it.recognitionWords.forEach { word ->
                                sb.append("$word ")
                            }
                        }
                        resultTv.text = sb.toString()
                    }
                }
            }

            shareBtn.setOnClickListener {
                if (resultTv.text.isNullOrEmpty()) {
                    makeSnackbar("추출된 문장이 없습니다")
                    return@setOnClickListener
                }
                if(!AuthApiClient.instance.hasToken()){
                    makeSnackbar("로그인을 먼저 해주세요")
                    return@setOnClickListener
                }

                TalkApiClient.instance.friends { friends, error ->
                    if (error != null) {
                        makeSnackbar("친구 목록 가져오기 실패\n${error}")
                    } else {
                        if (friends?.elements.isNullOrEmpty()) {
                            makeSnackbar("서비스와 연결된 친구 목록이 없습니다")
                        } else {
                            FriendsActivity.startForResult(
                                this@OcrActivity,
                                friends!!.elements.map {
                                    FriendItem(
                                        it,
                                        false
                                    )
                                }) { selectedItems ->
                                val template = TextTemplate(resultTv.text.toString(), Link())

                                TalkApiClient.instance.sendDefaultMessage(
                                    selectedItems,
                                    template
                                ) { result, error ->
                                    if (error != null) {
                                        makeSnackbar("메시지 보내기 실패\n${error}")
                                    } else if (result != null) {
                                        makeSnackbar("메시지 보내기 성공 ${result.successfulReceiverUuids}")

                                        if (result.failureInfos != null) {
                                            makeSnackbar("메시지 보내기에 일부 성공했으나, 일부 대상에게는 실패 \n${result.failureInfos}")
                                        }
                                    }
                                }
                            }
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