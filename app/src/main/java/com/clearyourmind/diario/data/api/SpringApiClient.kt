package com.clearyourmind.diario.data.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Modelo de datos para enviar sugerencias al microservicio
data class Suggestion(
    val id: Long? = null,
    val content: String,
    val author: String
)

interface SpringApiService {
    @POST("api/suggestions")
    fun sendSuggestion(@Body suggestion: Suggestion): Call<Suggestion>
}

object SpringRetrofitClient {
    // 10.0.2.2 es la dirección especial del Emulador para acceder al "localhost" de tu PC
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val instance: SpringApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpringApiService::class.java)
    }
}