package com.example.kakaovisionsdk.face

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kakaovisionsdk.databinding.ActivityFaceDetectBinding
import com.example.vision.VisionApiClient
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt

class FaceDetectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaceDetectBinding
    private lateinit var drawCanvas: DrawCanvas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawCanvas = DrawCanvas(this)

        binding.apply {
            createBtn.setOnClickListener {
                Glide.with(this@FaceDetectActivity).load(urlEt.text.toString()).into(targetImg)

                VisionApiClient.instance.detectFace(urlEt.text.toString()) { result, error ->
                    if (error != null) {
                        makeSnackbar("얼굴 검출 실패\n${error}")
                    } else {
                        result?.let {
                            ageTv.text =
                                "${it.detectedFace.faces[0].facialAttributes.age.roundToInt()} 세"
                            genderTv.text =
                                if (it.detectedFace.faces[0].facialAttributes.gender.female < it.detectedFace.faces[0].facialAttributes.gender.male) "남성" else "여성"
                            makeSnackbar("${it.detectedFace}")
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

class DrawCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var loadDrawImage: Bitmap? = null

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)

        if (loadDrawImage != null) {
            canvas.drawBitmap(loadDrawImage!!, 0F, 0F, null)
        }
    }

    fun getCurrentCanvas(): Bitmap {
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.draw(canvas)
        return bitmap
    }
}