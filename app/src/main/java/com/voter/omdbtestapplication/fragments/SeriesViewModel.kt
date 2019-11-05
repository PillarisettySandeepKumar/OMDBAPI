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

class SeriesViewModel : ViewModel() {
    var seriesResponseObj: MutableLiveData<MovieObj>? = null
    var progressDialog: SingleLiveEvent<Boolean>? = null
    var apiSeriesResponseListner: APISeriesResponseListner?=null


    init {
        seriesResponseObj= MutableLiveData();
        progressDialog= SingleLiveEvent()
    }


    fun  getSeries( seriesQueryMap: HashMap<String,String>){
        progressDialog?.value=true

        WebServiceClient.client.create(ApiCall::class.java).getMovies(seriesQueryMap).enqueue(object :
            Callback<MovieObj> {
            override fun onFailure(call: Call<MovieObj>?, t: Throwable?) {
                progressDialog?.value=false
                apiSeriesResponseListner?.seriesResponseFailure(t?.message!!)

                Log.d("TAG","seriesResponseObjFail response ${t?.message}")


            }

            override fun onResponse(call: Call<MovieObj>?, response: Response<MovieObj>?) {
                progressDialog?.value=false

                seriesResponseObj?.value=response?.body()
                Log.d("TAG","seriesResponseObjSuccess response ${response?.body()}")
            }
        })

    }

    fun setListner( apiSeriesResponseListner: APISeriesResponseListner){
        this.apiSeriesResponseListner=apiSeriesResponseListner
    }


    interface APISeriesResponseListner{


        fun seriesResponseFailure( seriesResponseFailure: String)

    }
}
