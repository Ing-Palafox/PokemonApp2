package com.example.pokemonapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL base de la PokeAPI
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    // Instancia de Retrofit que se usará para las solicitudes
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Establecemos la URL base
            .addConverterFactory(GsonConverterFactory.create()) // Convertimos JSON automáticamente a objetos de Kotlin
            .build()
    }
}
