package com.example.pokemonapp

data class Pokemon(
    val name: String,          // Nombre del Pokémon
    val id: Int,               // ID del Pokémon
    val sprites: Sprites       // Sprites (imágenes del Pokémon)
)

data class Sprites(
    val front_default: String  // URL de la imagen del Pokémon (vista frontal)
)
