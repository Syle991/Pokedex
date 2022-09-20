package com.pokemon.pokedex.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pokemon.pokedex.R

class ErrorLoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_loading)
    }
}