package com.pokemon.pokedex.data

import org.json.JSONArray

data class InitialListData(
    var count: String = "",
    var next: String = "",
    var previous: String = "",
    var results: JSONArray = JSONArray()
)


annotation class SerializedName(val value: String)
data class InfoPokemonJson(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("base_experience") val base_experience: String,
    @SerializedName("weight") val weight: String,
    @SerializedName("height") val height: String,
    @SerializedName("stats") val stats: List<Any>,
    @SerializedName("types") val types: List<Any>,
    @SerializedName("sprites") val sprites: Any)


data class PokemonInfoData(
    var baseExperience: String = "",
    var weight: String,
    var height: String = "",
    var id: String = "",
    var name: String = "",
    var sprites:String = "",
    var hp:String = "",
    var defense: String = "",
    var attack: String = "",
    var types:String = "",
    var specialAttack:String = "",
    var specialDefense:String = "",
    var speed:String = ""
)