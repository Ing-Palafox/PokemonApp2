package com.example.pokemonapp

// Esta clase es la respuesta que contiene una lista de Pokémon
data class PokemonListResponse(
    val results: List<Pokemon>  // Lista de Pokémon obtenidos
)