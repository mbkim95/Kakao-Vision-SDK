package com.example.vision.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class ImageChooseResult(val image: File) : Parcelable