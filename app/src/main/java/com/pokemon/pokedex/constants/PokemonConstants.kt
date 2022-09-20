package com.pokemon.pokedex.constants

class PokemonConstants {

    object TIME {
        const val SPLASH_TIME_OUT: Long = 3000
        const val FREZZE: Long = 5000
        const val LOADING: Long = 5000
    }

    object ACCESS {
        const val INITIAL_LIST: String = "https://pokeapi.co/api/v2/pokemon?limit=3&offset=0"
    }
}