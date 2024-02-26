package com.example.fortunetellingbymemes.model

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val success: Boolean,
    val data: Memes,
)

@Serializable
data class Memes(
    val memes: List<Meme>
)
