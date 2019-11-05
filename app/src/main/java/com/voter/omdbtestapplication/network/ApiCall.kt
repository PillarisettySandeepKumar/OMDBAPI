package com.voter.omdbtestapplication.network

import com.voter.omdbtestapplication.model.MovieObj
import retrofit2.Call
import retrofit2.http.*


interface ApiCall {


    @GET("?")
    fun getMovies(@QueryMap queryInputs:HashMap<String,String>): Call<MovieObj>
}