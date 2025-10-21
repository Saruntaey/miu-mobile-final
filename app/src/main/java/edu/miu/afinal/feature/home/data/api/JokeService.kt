package edu.miu.afinal.feature.home.data.api

import edu.miu.afinal.feature.home.data.api.dto.JokeResponseDto
import retrofit2.http.GET

interface JokeService {
    @GET("random_joke")
    suspend fun getRandomJoke(): JokeResponseDto
}