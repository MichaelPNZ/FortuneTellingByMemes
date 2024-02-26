package com.example.fortunetellingbymemes.model

import kotlinx.serialization.Serializable

@Serializable
data class Meme(
    val id: String,
    val name: String,
    val url: String,
    val width: Int,
    val height: Int,
    val box_count: Int,
    val captions: Int,
)