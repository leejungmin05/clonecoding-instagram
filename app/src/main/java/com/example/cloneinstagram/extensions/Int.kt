package com.example.cloneinstagram.extensions

fun Int.likeUnit():String{
    return when {
        this>=1000 -> {
            "${this}K"
        }
        this>=1000000 -> {
            "${this}M"
        }
        else -> {
            "$this"
        }
    }
}