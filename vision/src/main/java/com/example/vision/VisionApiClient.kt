package com.example.vision

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver

class VisionApiClient {

    fun selectImage(context: Context, callback: () -> Unit) {
        context.startActivity(Intent(context, ImageSelectActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(IntentFactory.KEY_BUNDLE, Bundle().apply {
                putParcelable(IntentFactory.IMAGE_SELECTOR, resultReceiver(callback))
            })
        })
    }

    @JvmSynthetic
    internal fun resultReceiver(callback: () -> Unit): ResultReceiver {
        return object : ResultReceiver(Handler(Looper.getMainLooper())) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                if (resultCode == Activity.RESULT_OK) {
                    // api 호출 결과 처리
                }
            }
        }
    }

    companion object {
        @JvmStatic
        val instance by lazy { VisionApiClient() }
    }
}