package com.pokemon.pokedex.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pokemon.pokedex.R
import com.pokemon.pokedex.data.InfoPokemonJson
import com.pokemon.pokedex.data.InitialListData
import com.pokemon.pokedex.data.PokemonInfoData
import org.json.JSONArray
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    private lateinit var pokemonList: ArrayList<PokemonInfoData>

    //Views
    private lateinit var nestedSV: NestedScrollView
    private lateinit var recyclerview: RecyclerView
    private lateinit var loadingPB: ProgressBar
    private lateinit var pokemon: InitialListData
    private lateinit var count:TextView

    //ArrayList
    private lateinit var jsonArray: JSONArray

    //ListAdapter
    private lateinit var adapter: ListAdapter

    private var isLoading:Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pokemonList = ArrayList()
        pokemon = InitialListData()

        //Get Initial List
        getInitialList(pokemon)

        //Loading Progress
        loadingPB = findViewById(R.id.idPBLoading)

        //NestedScrollView
        nestedSV = findViewById(R.id.idNestedSV)

        //Recycler View
        recyclerview = findViewById(R.id.recyclerview)
        recyclerview.layoutManager = GridLayoutManager(this, 1)

        //Total TextView
        count = findViewById(R.id.total)
        count.text = "Total: " + pokemon.count + " Pokemon's"

        //Get Info List Pokemon
        populateListPokemon(jsonArray)

        //Scroll Listener
        nestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v != null) {
                loadingPB.visibility = View.VISIBLE
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    if(!isLoading){
                        getNextData()
                    }
                }
            }
        })

        adapter = ListAdapter(pokemonList)
        recyclerview.adapter = adapter
    }

    private fun getNextData() {
        var outputJson: String
        val attributesPokemonList = ArrayList<Any>()

        isLoading = true

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, pokemon.next, null,
            { res ->
                val limit: Int = res.getJSONArray("results").length()
                try {
                    pokemon.next = res.getString("next")
                    for (i in 0 until 3) {
                        val urlValue:String = res.getJSONArray("results").getJSONObject(i).getString("url")
                        val infoPokeRequest = JsonObjectRequest(Request.Method.GET, urlValue, null,
                            { response ->
                                 val infoPokemonJson: InfoPokemonJson = Gson().fromJson(
                                      response.toString(),
                                      InfoPokemonJson::class.java
                                  )
                                  outputJson = Gson().toJson(infoPokemonJson)
                                  attributesPokemonList.add(outputJson)

                                  if (i == limit - 1) {
                                      populateListPokemon(JSONArray(attributesPokemonList.toString()))
                                      recyclerview.adapter = adapter
                                      isLoading = false
                                      loadingPB.visibility = View.GONE
                                  }
                            },
                            {
                                Toast.makeText(this, "Error getting data...", Toast.LENGTH_LONG)
                                    .show()
                            })

                        //add queue infoPokeRequest
                        infoPokeRequest.setShouldCache(false)
                        Volley.newRequestQueue(this).add(infoPokeRequest)
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "Error getting data...", Toast.LENGTH_LONG).show()
                }
            },
            { error -> Toast.makeText(this, "Error getting data...", Toast.LENGTH_SHORT).show() })


        jsonObjectRequest.setShouldCache(false)
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    private fun populateListPokemon(jsonArray: JSONArray) {

        var type = ""
        for (poke in 0 until jsonArray.length()) {
            val spritesUrl: String =
                jsonArray.getJSONObject(poke).getJSONObject("sprites").getJSONObject("other")
                    .getJSONObject("official-artwork").getString("front_default")

            val id: String = " " + jsonArray.getJSONObject(poke).getString("id")
            val name: String = " " + jsonArray.getJSONObject(poke).getString("name")
                .replaceFirstChar { it.uppercase() }
            val baseExperience: String = " " +
                    jsonArray.getJSONObject(poke).getString("base_experience")

            val height: String = " " + convertHeight(jsonArray.getJSONObject(poke).getString("height").toInt()) + " m"
            val weight: String = " " + convertWeight(jsonArray.getJSONObject(poke).getString("weight").toInt()) + " kg"

            //Types
            for (typeIterator in 0 until jsonArray.getJSONObject(poke).getJSONArray("types")
                .length()) {

                type = " " + jsonArray.getJSONObject(poke)
                    .getJSONArray("types")
                    .getJSONObject(typeIterator)
                    .getJSONObject("type")
                    .getString("name")
                    .toString()
                    .replaceFirstChar { it.uppercase() }
            }

            //HP
            val hp: String = " " + jsonArray.getJSONObject(poke)
                .getJSONArray("stats")
                .getJSONObject(0)
                .getString("base_stat")
                .toString()

            //Attack
            val attack: String = " " + jsonArray.getJSONObject(poke)
                .getJSONArray("stats")
                .getJSONObject(1)
                .getString("base_stat")
                .toString()

            //Defense
            val defense: String = " " + jsonArray.getJSONObject(poke)
                .getJSONArray("stats")
                .getJSONObject(2)
                .getString("base_stat")
                .toString()

            val specialAttack: String = " " + jsonArray.getJSONObject(poke)
                .getJSONArray("stats")
                .getJSONObject(3)
                .getString("base_stat")
                .toString()

            val specialDefense: String = " " + jsonArray.getJSONObject(poke)
                .getJSONArray("stats")
                .getJSONObject(4)
                .getString("base_stat")
                .toString()

            val speed: String = " " + jsonArray.getJSONObject(poke)
                .getJSONArray("stats")
                .getJSONObject(5)
                .getString("base_stat")
                .toString()

            pokemonList.add(
                PokemonInfoData(
                    sprites = spritesUrl,
                    id = id,
                    name = name,
                    types = type,
                    baseExperience = "$baseExperience XP",
                    height = height,
                    weight = weight,
                    hp = hp,
                    attack = "$attack ",
                    defense = defense,
                    specialAttack = "$specialAttack ",
                    specialDefense = specialDefense,
                    speed = speed
                )
            )

        }

    }


    private fun convertWeight(value: Int):String {
        return (value*0.1).toString().substring(0,2).replace(".","")
    }

    private fun convertHeight(value: Int):String {
        return (value*0.1).toString().substring(0,3)
    }

    //Get Initial List and other info from Intent Extra
    private fun getInitialList(pokemon: InitialListData) {

        //Count
        pokemon.count = if (intent.hasExtra("count")) {
            intent.getStringExtra("count").toString()
        } else {
            ""
        }

        //Next
        pokemon.next = if (intent.hasExtra("next")) {
            intent.getStringExtra("next").toString()
        } else {
            ""
        }

        //Previous
        pokemon.previous = if (intent.hasExtra("previous")) {
            intent.getStringExtra("previous").toString()
        } else {
            ""
        }

        //Results
        pokemon.results = if (intent.hasExtra("results")) {
            JSONArray(intent.getStringExtra("results"))
        } else {
            JSONArray()
        }

        //Info each Pokemon
        jsonArray = if (intent.hasExtra("infoPokemon")) {
            JSONArray(intent.getStringExtra("infoPokemon"))
        } else {
            JSONArray()
        }

    }


}