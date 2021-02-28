package com.example.cloneinstagram.extensions

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cloneinstagram.R
import com.example.cloneinstagram.model.AlarmDTO
import com.example.cloneinstagram.model.AlarmKind
import com.example.cloneinstagram.model.FirebaseRepository

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ImageView.getProfileImageByUid(uid: String) {
    FirebaseRepository.getProfileUrl(uid) { url ->
        Glide.with(context).load(url).apply(RequestOptions().circleCrop())
            .into(this)
    }
}


fun AlarmDTO.getSentence(context: Context): String {
    return when (kind as AlarmKind) {
        AlarmKind.LIKE -> {
            context.resources.getString(
                R.string.alarm_favorite,
                userId
            )
        }
        AlarmKind.COMMENT -> {
            context.resources.getString(
                R.string.alarm_comment,
                userId
            )
        }
        AlarmKind.FOLLOW -> {
            context.resources.getString(
                R.string.alarm_follow,
                userId
            )
        }
    }
}

fun ImageView.getGridImage(imageUrl: String) {
    Glide.with(context).load(imageUrl).apply(RequestOptions().centerCrop())
        .into(this)
}