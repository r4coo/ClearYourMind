package com.clearyourmind.diario.data.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Modelo de datos para la respuesta de la API (Formato de 'zenquotes.io' o similar)
data class Quote(
    val q: String, // Texto de la cita
    val a: String  // Autor
)

interface QuotesApiService {
    @GET("api/random")
    fun getRandomQuote(): Call<List<Quote>>
}

object RetrofitClient {
    private const val BASE_URL = "https://zenquotes.io/"

    val instance: QuotesApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(QuotesApiService::class.java)
    }
}