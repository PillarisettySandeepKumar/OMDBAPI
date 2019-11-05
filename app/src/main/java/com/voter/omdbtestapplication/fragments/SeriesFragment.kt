package com.voter.omdbtestapplication.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.edison.Util.CustomProgressDialog
import com.example.edison.Util.hideKeyboard
import com.example.edison.Util.loadImgfromUrl
import com.example.edison.Util.showSnackbar
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.voter.omdbtestapplication.DetailActivity

import com.voter.omdbtestapplication.R
import com.voter.omdbtestapplication.model.MovieObj
import com.voter.omdbtestapplication.utils.Constants
import kotlinx.android.synthetic.main.movie_fragment.*
import kotlinx.android.synthetic.main.movie_fragment.bannerValue
import kotlinx.android.synthetic.main.movie_fragment.posterImg
import kotlinx.android.synthetic.main.movie_fragment.releaseYearValue
import kotlinx.android.synthetic.main.series_fragment.*

class SeriesFragment : Fragment(),SeriesViewModel.APISeriesResponseListner {

    private lateinit var seriesViewModel: SeriesViewModel
    var seriesQueryMap: HashMap<String, String>? = null
    var customProgressDialog: CustomProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.series_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        seriesQueryMap = HashMap()

        titleSeriesSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(activity!!,seriesParentll)

                    if (titleSeriesSearch.text!!.isNotEmpty()) {
                        seriesQueryMap?.clear()
                        seriesQueryMap?.put("i",Constants.i.value)
                        seriesQueryMap?.put("apikey",Constants.apikey.value)
                        seriesQueryMap?.put("type", "series")
                        seriesQueryMap?.put("t", titleSeriesSearch.text.toString())
                        seriesViewModel.getSeries(seriesQueryMap!!)


                        return true
                    }else{
                        //   showSnackbar(activity!!, movieParentll, "Please Enter Text")

                    }
                }

                return false
            }
        })

    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)


        if (isResumed && isVisibleToUser && seriesQueryMap!!.isEmpty()) {
            loadSeriesDataViews()

        }
    }

    fun loadSeriesDataViews(){
        customProgressDialog = CustomProgressDialog(activity!!)

        seriesViewModel = ViewModelProviders.of(this).get(SeriesViewModel::class.java)
        seriesViewModel.setListner(this)
        seriesQueryMap?.put("apikey", Constants.apikey.value)
        seriesQueryMap?.put("type", "series")
        seriesQueryMap?.put("t", "game")
        seriesViewModel.getSeries(seriesQueryMap!!)

        seriesViewModel.progressDialog?.observe(activity!!, Observer {

            if (it)customProgressDialog?.show()else customProgressDialog?.dismiss()

        })

        seriesViewModel.seriesResponseObj?.observe(activity!!, Observer { seriesObj ->

            with(seriesObj){
                if (Response.equals("True", true)) {
                    series_card_view.visibility=View.VISIBLE

                    seriesNameValue.text = Title
                    releaseYearValue.text = Released
                    bannerValue.text = Production
                    loadImgfromUrl(Poster!!,posterImg)

                    series_card_view.setOnClickListener {
                        startActivity(Intent(activity!!, DetailActivity::class.java).apply {
                            putExtra("OMDBObj",
                                MovieObj(Response,Error,Title,Year,Rated,Released,Runtime,Production,Poster,Actors,Plot,Awards,Genre,imdbRating)
                            )
                        })
                    }

                    Log.d("TAG", "movieObj ${Title}")
                } else {
                    series_card_view.visibility=View.GONE
                    showSnackbar(activity!!, seriesParentll, Error!!)
                }
            }



        })
    }

    override fun seriesResponseFailure(seriesResponseFailure: String) {
        showSnackbar(activity!!, seriesParentll, seriesResponseFailure)

    }

}
