package com.example.pokemonapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(
    private val pokemonList: List<Pokemon>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]

        // En lugar de usar una imagen predeterminada, si la URL es null, solo no cargar nada
        val imageUrl = pokemon.sprites?.front_default

        if (imageUrl != null) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.imageView)
        } else {
            holder.imageView.setImageResource(0) // No carga imagen si no hay URL
        }
        // Asignar nombre y cargar imagen
        holder.nameTextView.text = pokemon.name.capitalize()
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.imageView)

        // Configurar la acción al hacer clic en el ítem
        holder.itemView.setOnClickListener {
            onItemClick(pokemon.name)
        }
    }

    override fun getItemCount(): Int = pokemonList.size

    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.pokemon_name)
        val imageView: ImageView = itemView.findViewById(R.id.pokemon_image)
    }
}
