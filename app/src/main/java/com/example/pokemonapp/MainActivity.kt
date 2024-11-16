package com.example.pokemonapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var pokemonListAdapter: PokemonAdapter
    private val pokemonList = mutableListOf<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuración del Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)  // Establece el Toolbar como la ActionBar

        // Referencias a los elementos de la interfaz
        val searchButton: Button = findViewById(R.id.search_button)
        val pokemonNameInput: EditText = findViewById(R.id.pokemon_name_input)
        val pokemonNameText: TextView = findViewById(R.id.pokemon_name)
        val pokemonImageView: ImageView = findViewById(R.id.pokemon_image)
        val pokemonListRecyclerView: RecyclerView = findViewById(R.id.pokemon_list)

        // Configuración del RecyclerView
        pokemonListRecyclerView.layoutManager = LinearLayoutManager(this)
        pokemonListAdapter = PokemonAdapter(pokemonList) { pokemonName ->
            // Acción cuando se hace clic en un Pokémon de la lista
            fetchPokemonDetails(pokemonName)
        }
        pokemonListRecyclerView.adapter = pokemonListAdapter

        // Inicializar el cliente Retrofit y la interfaz
        val apiService = RetrofitClient.instance.create(PokemonApiService::class.java)

        // Configurar el evento del botón de búsqueda
        searchButton.setOnClickListener {
            val pokemonName = pokemonNameInput.text.toString().lowercase()

            if (pokemonName.isNotEmpty()) {
                // Usamos CoroutineScope para hacer la llamada en un hilo de fondo
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Llamada a la API para obtener un Pokémon específico
                        val response = apiService.getPokemon(pokemonName)

                        // Verificamos si la respuesta es válida
                        if (response != null && response.sprites != null) {
                            // Actualizar la UI en el hilo principal
                            withContext(Dispatchers.Main) {
                                pokemonNameText.text = response.name.capitalize()
                                Glide.with(this@MainActivity)
                                    .load(response.sprites.front_default)
                                    .into(pokemonImageView)
                            }
                        } else {
                            // Si el Pokémon no se encuentra o no tiene imagen
                            withContext(Dispatchers.Main) {
                                pokemonNameText.text = "Pokémon no encontrado"
                                pokemonImageView.setImageResource(0) // Limpia la imagen si hay error
                            }
                        }
                    } catch (e: Exception) {
                        // Manejo de errores en la UI
                        withContext(Dispatchers.Main) {
                            pokemonNameText.text = "Error: Pokémon no encontrado"
                            pokemonImageView.setImageResource(0) // Limpia la imagen si hay error
                        }
                    }
                }
            } else {
                // Si no hay un nombre ingresado
                pokemonNameText.text = "Por favor ingrese un nombre"
            }
        }

        // Cargar la lista de Pokémon
        loadPokemonList(apiService)
    }

    // Función para obtener los detalles del Pokémon cuando se selecciona de la lista
    private fun fetchPokemonDetails(pokemonName: String) {
        // Mostrar el nombre y la imagen en la parte superior de la pantalla
        val pokemonNameText: TextView = findViewById(R.id.pokemon_name)
        val pokemonImageView: ImageView = findViewById(R.id.pokemon_image)

        // Inicializar el cliente Retrofit y la interfaz
        val apiService = RetrofitClient.instance.create(PokemonApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Llamada a la API para obtener los detalles de un Pokémon
                val response = apiService.getPokemon(pokemonName)

                // Verificamos si la respuesta es válida
                if (response != null && response.sprites != null) {
                    // Actualizar la UI en el hilo principal
                    withContext(Dispatchers.Main) {
                        pokemonNameText.text = response.name.capitalize()  // Mostrar el nombre del Pokémon
                        Glide.with(this@MainActivity)
                            .load(response.sprites.front_default)  // Cargar la imagen del Pokémon
                            .into(pokemonImageView)
                    }
                } else {
                    // Si no se encuentra o no tiene imagen
                    withContext(Dispatchers.Main) {
                        pokemonNameText.text = "Pokémon no encontrado"
                        pokemonImageView.setImageResource(0) // Limpiar la imagen si hay error
                    }
                }
            } catch (e: Exception) {
                // Manejo de errores
                withContext(Dispatchers.Main) {
                    pokemonNameText.text = "Error: Pokémon no encontrado"
                    pokemonImageView.setImageResource(0) // Limpiar la imagen si hay error
                }
            }
        }
    }

    // Función para cargar la lista de Pokémon en el RecyclerView
    private fun loadPokemonList(apiService: PokemonApiService) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Llamada para obtener la lista de Pokémon
                val response = apiService.getPokemonList()

                // Verificamos la respuesta y actualizamos la lista en el hilo principal
                withContext(Dispatchers.Main) {
                    if (response != null) {
                        pokemonList.clear()
                        pokemonList.addAll(response.results)
                        pokemonListAdapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                // Manejo de errores en la UI
            }
        }
    }
}
