package com.example.fortunetellingbymemes.data


import com.example.fortunetellingbymemes.model.Data
import retrofit2.Call
import retrofit2.http.GET

interface MemesApiService {

    @GET("get_memes")
    fun getMemes(): Call<Data>

}