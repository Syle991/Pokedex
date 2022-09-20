package com.pokemon.pokedex.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pokemon.pokedex.R
import com.pokemon.pokedex.constants.PokemonConstants
import com.pokemon.pokedex.data.InfoPokemonJson
import org.json.JSONException
import org.json.JSONObject
import java.util.*


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var infoPokemonList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.insetsController?.hide(WindowInsets.Type.statusBars())

        //Create backgroundImage from SplashScreenImage
        val backgroundImage: ImageView = findViewById(R.id.SplashScreenImage)
        //Create animation
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)

        //Start Animation
        backgroundImage.startAnimation(slideAnimation)

        //Get List of the Pokemon
        getListPokemon()
    }

    private fun getListPokemon() {
        val queue = Volley.newRequestQueue(this)
        var outputJson: String
        val jsonObjectRequest =
            JsonObjectRequest(
                Request.Method.GET, PokemonConstants.ACCESS.INITIAL_LIST, null,
                { res ->
                    val limit: Int = res.getJSONArray("results").length()
                    try {
                        for (i in 0 until limit) {
                            val urlValue: String =
                                res.getJSONArray("results").getJSONObject(i).getString("url")

                            val infoPokeRequest = JsonObjectRequest(Request.Method.GET,
                                urlValue,
                                null,
                                { response ->
                                    val infoPokemonInitial: InfoPokemonJson = Gson().fromJson(
                                        response.toString(),
                                        InfoPokemonJson::class.java
                                    )

                                    outputJson = Gson().toJson(infoPokemonInitial)
                                    infoPokemonList.add(outputJson)

                                    if (i == limit - 1) {
                                        goToMain(res, infoPokemonList)
                                    }
                                },
                                {
                                    errorHandler()
                                })

                            //add queue infoPokeRequest
                            queue.add(infoPokeRequest)

                        }
                    } catch (e: JSONException) {
                        //In case of fail of getting response send to ErrorActivity
                        errorHandler()
                    }
                },
                {
                    //In case of fail of getting response send to ErrorActivity
                    errorHandler()
                }
            )

        //add queue jsonObjectRequest
        jsonObjectRequest.setShouldCache(false)
        queue.add(jsonObjectRequest)
    }


    private fun goToMain(jsonObject: JSONObject, infoPokemon: ArrayList<String>) {

        intent = Intent(this, MainActivity::class.java)

        Handler(Looper.getMainLooper()).postDelayed({
            intent.putExtra("count", jsonObject.getString("count"))
            intent.putExtra("next", jsonObject.getString("next"))
            intent.putExtra("previous", jsonObject.getString("previous"))
            intent.putExtra("results", jsonObject.getJSONArray("results").toString())
            intent.putExtra("infoPokemon", infoPokemon.toString())
            startActivity(intent)
            finish()
        }, PokemonConstants.TIME.SPLASH_TIME_OUT)
    }

    private fun errorHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ErrorLoadingActivity::class.java)
            startActivity(intent)
            finish()
        }, PokemonConstants.TIME.SPLASH_TIME_OUT)
    }

}




