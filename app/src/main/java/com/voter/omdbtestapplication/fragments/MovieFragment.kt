package com.voter.omdbtestapplication.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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

class MovieFragment : Fragment(),MovieViewModel.APIMoviesResponseListner {


    private lateinit var movieViewModel: MovieViewModel
    var movieQueryMap: HashMap<String, String>? = null
    var customProgressDialog: CustomProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieQueryMap = HashMap()
        customProgressDialog = CustomProgressDialog(activity)

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        movieViewModel.setListner(this)
        movieQueryMap?.put("i", Constants.i.value)
        movieQueryMap?.put("apikey", Constants.apikey.value)
        movieQueryMap?.put("type", "movie")
        movieQueryMap?.put("t", "game")
        movieViewModel.getMovies(movieQueryMap!!)

        movieViewModel.progressDialog?.observe(activity!!, Observer {

            if (it)customProgressDialog?.show()else customProgressDialog?.dismiss()

        })

        movieViewModel.moviesResponseObj?.observe(activity!!, Observer { movieObj ->
            with(movieObj){
                if (Response.equals("True", true)) {
                    movie_card_view.visibility=View.VISIBLE

                    movieNameValue.text =Title
                    releaseYearValue.text = Released
                    bannerValue.text = Production
                    loadImgfromUrl(Poster!!,posterImg)
                    Log.d("TAG", "movieObj ${Title}")
                    movie_card_view.setOnClickListener {

                        startActivity(Intent(activity!!,DetailActivity::class.java).apply {
                            putExtra("OMDBObj",MovieObj(Response,Error,Title,Year,Rated,Released,Runtime,Production,Poster,Actors,Plot,Awards,Genre,imdbRating))
                        })
                    }

                } else {
                    movie_card_view.visibility=View.GONE
                    showSnackbar(activity!!, movieParentll, Error!!)
                }
            }



        })
        titleSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(activity!!,movieParentll)

                    if (titleSearch.text!!.isNotEmpty()) {
                        movieQueryMap?.clear()
                        movieQueryMap?.put("i",Constants.i.value)
                        movieQueryMap?.put("apikey",Constants.apikey.value)
                        movieQueryMap?.put("type", "movie")
                        movieQueryMap?.put("t", titleSearch.text.toString())
                        movieViewModel.getMovies(movieQueryMap!!)


                        return true
                    }else{
                     //   showSnackbar(activity!!, movieParentll, "Please Enter Text")

                    }
                }

                return false
            }
        })


    }
    override fun movieResponseFailure(movieResponseFailure: String) {
        showSnackbar(activity!!, movieParentll, movieResponseFailure)

    }

}
