package com.pokemon.pokedex.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.pokedex.R
import com.pokemon.pokedex.data.PokemonInfoData
import com.squareup.picasso.Picasso

class ListAdapter(private val mList: List<PokemonInfoData>) :
    RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val listData = mList[position]

        Picasso.get().load(listData.sprites).into(holder.image)
        holder.id.text = listData.id
        holder.nameText.text = listData.name
        holder.baseExperience.text = listData.baseExperience
        holder.height.text = listData.height
        holder.weight.text = listData.weight
        holder.hp.text = listData.hp
        holder.attack.text = listData.attack
        holder.defense.text = listData.defense
        holder.type.text = listData.types
        holder.specialAttack.text = listData.specialAttack
        holder.specialDefense.text = listData.specialDefense
        holder.speed.text = listData.speed
    }


    override fun getItemCount(): Int {
        return mList.size
    }

    open class ItemViewHolder(ItemView: View) :
        RecyclerView.ViewHolder(ItemView) {

        var image: ImageView
        val id: TextView = itemView.findViewById(R.id.idText)
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val baseExperience: TextView = itemView.findViewById(R.id.baseExperience)
        val height: TextView = itemView.findViewById(R.id.height)
        var weight: TextView = itemView.findViewById(R.id.weight)
        var hp: TextView = itemView.findViewById(R.id.hp)
        var attack: TextView = itemView.findViewById(R.id.attack)
        var defense: TextView = itemView.findViewById(R.id.defense)
        var type: TextView = itemView.findViewById(R.id.type)
        var specialAttack:TextView = itemView.findViewById(R.id.specialattack)
        var specialDefense:TextView = itemView.findViewById(R.id.specialdefense)
        var speed:TextView = itemView.findViewById(R.id.speed)

        init {
            image = ItemView.findViewById<View>(R.id.img_pokemon) as ImageView
        }
    }


}


