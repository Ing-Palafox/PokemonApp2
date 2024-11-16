package com.example.pokemonapp

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Interfaz para acceder a los endpoints de la API
interface PokemonApiService {

    // Endpoint para obtener un Pokémon por su nombre
    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): Pokemon

    // Endpoint para obtener una lista de Pokémon
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,  // Número máximo de Pokémon a recuperar
        @Query("offset") offset: Int = 0   // Desplazamiento para la paginación
    ): PokemonListResponse
}
