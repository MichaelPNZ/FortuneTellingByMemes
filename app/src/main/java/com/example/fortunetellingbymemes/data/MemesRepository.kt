package com.example.fortunetellingbymemes.data

import android.util.Log
import com.example.fortunetellingbymemes.model.Data
import com.example.fortunetellingbymemes.model.Memes
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class MemesRepository {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val memesApiService: MemesApiService by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgflip.com/")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(client)
            .build()

        retrofit.create(MemesApiService::class.java)
    }

    suspend fun getMemes(): Data? {
        return withContext(ioDispatcher) {
            try {
                val memes = memesApiService.getMemes().execute().body()
                memes
            } catch (e: Exception) {
                Log.e("!!!", "Error fetching memes", e)
                null
            }
        }
    }
}