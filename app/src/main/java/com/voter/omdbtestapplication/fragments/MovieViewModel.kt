package com.voter.omdbtestapplication.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voter.omdbtestapplication.model.MovieObj
import com.voter.omdbtestapplication.network.ApiCall
import com.voter.omdbtestapplication.network.WebServiceClient
import com.voter.omdbtestapplication.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {
    var moviesResponseObj: MutableLiveData<MovieObj>? = null
    var progressDialog: SingleLiveEvent<Boolean>? = null
    var apiMoviesResponseListner: APIMoviesResponseListner?=null

    init {
        moviesResponseObj= MutableLiveData();
        progressDialog= SingleLiveEvent()
    }


    fun  getMovies( moviesQueryMap: HashMap<String,String>){
        progressDialog?.value=true

        WebServiceClient.client.create(ApiCall::class.java).getMovies(moviesQueryMap).enqueue(object :
            Callback<MovieObj> {
            override fun onFailure(call: Call<MovieObj>?, t: Throwable?) {
                progressDialog?.value=false
                apiMoviesResponseListner?.movieResponseFailure(t?.message!!)
                Log.d("TAG","moviesFail response ${t?.message}")


            }

            override fun onResponse(call: Call<MovieObj>?, response: Response<MovieObj>?) {
                progressDialog?.value=false

                moviesResponseObj?.value=response?.body()
                Log.d("TAG","moviesSuccess response ${response?.body()}")
            }
        })

    }
    fun setListner( apiMoviesResponseListner: APIMoviesResponseListner){
        this.apiMoviesResponseListner=apiMoviesResponseListner
    }

    interface APIMoviesResponseListner{


        fun movieResponseFailure( movieResponseFailure: String)

    }
}
