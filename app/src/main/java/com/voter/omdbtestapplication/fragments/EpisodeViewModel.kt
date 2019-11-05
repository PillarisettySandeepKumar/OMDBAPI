package com.voter.omdbtestapplication.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.voter.omdbtestapplication.model.MovieObj
import com.voter.omdbtestapplication.network.ApiCall
import com.voter.omdbtestapplication.network.WebServiceClient
import com.voter.omdbtestapplication.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EpisodeViewModel : ViewModel() {

    var episodeResponseObj: MutableLiveData<MovieObj>? = null
    var progressDialog: SingleLiveEvent<Boolean>? = null
    var apiEpisodeResponseListner: APIEpisodeResponseListner?=null
    init {
        episodeResponseObj= MutableLiveData();
        progressDialog= SingleLiveEvent()
    }


    fun  getEpisode( episodeQueryMap: HashMap<String,String>){
        progressDialog?.value=true

        WebServiceClient.client.create(ApiCall::class.java).getMovies(episodeQueryMap).enqueue(object :
            Callback<MovieObj> {
            override fun onFailure(call: Call<MovieObj>?, t: Throwable?) {
                progressDialog?.value=false
                apiEpisodeResponseListner?.episodeResponseFailure(t?.message!!)

                Log.d("TAG","episodeResponseObjFail response ${t?.message}")


            }

            override fun onResponse(call: Call<MovieObj>?, response: Response<MovieObj>?) {
                progressDialog?.value=false
                episodeResponseObj?.value=response?.body()

                Log.d("TAG","episodeResponseObjSuccess response ${response?.body()}")
            }
        })

    }


    fun setListner( apiSeriesResponseListner: APIEpisodeResponseListner){
        this.apiEpisodeResponseListner=apiSeriesResponseListner
    }


    interface APIEpisodeResponseListner{

        fun episodeResponseFailure( episodeResponseFailure: String)

    }
}
