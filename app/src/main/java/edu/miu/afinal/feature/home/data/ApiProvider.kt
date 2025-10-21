package edu.miu.afinal.feature.home.data

import edu.miu.afinal.feature.home.data.api.JokeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

object ApiProvider {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://official-joke-api.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val jokeService: JokeService by lazy {
        retrofit.create(JokeService::class.java)
    }
}