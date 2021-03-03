package com.example.vision

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.vision.IntentFactory.IMAGE_SELECTOR
import com.example.vision.IntentFactory.KEY_BUNDLE

class ImageSelectActivity : AppCompatActivity() {
    private lateinit var resultReceiver: ResultReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_select)

        try {
            intent.extras?.getBundle(KEY_BUNDLE)?.run {
                resultReceiver = getParcelable<ResultReceiver>(IMAGE_SELECTOR) as ResultReceiver
            }
        } catch (e: Throwable) {
            Log.e("Kakao Vision", "$e")
        }
        startActivityForResult(IntentFactory.imageSelect(), IMAGE_SELECT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_SELECT && resultCode == RESULT_OK) {
            data?.let {
                val uri = data.data
                if (uri.toString().contains("image")) {
                    uri?.let { uri ->
                        contentResolver.openInputStream(uri).use {
                            val bitmap = BitmapFactory.decodeStream(it)
                            // 이미지 전송

                            // 비동기 처리 후에 resultReceiver로 전달
                            finish()
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val IMAGE_SELECT = 1234
    }
}