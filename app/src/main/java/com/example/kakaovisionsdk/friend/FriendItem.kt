package com.example.kakaovisionsdk.friend

import android.os.Parcelable
import com.kakao.sdk.talk.model.Friend
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriendItem(val friendInfo: Friend, var isChecked: Boolean) : Parcelable