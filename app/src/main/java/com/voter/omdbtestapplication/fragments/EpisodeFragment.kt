package com.voter.omdbtestapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import kotlinx.android.synthetic.main.episode_fragment.*
import kotlinx.android.synthetic.main.movie_fragment.*
import kotlinx.android.synthetic.main.series_fragment.*
import kotlinx.android.synthetic.main.series_fragment.bannerValue
import kotlinx.android.synthetic.main.series_fragment.posterImg
import kotlinx.android.synthetic.main.series_fragment.releaseYearValue

class EpisodeFragment : Fragment() ,EpisodeViewModel.APIEpisodeResponseListner{


    private lateinit var episodeViewModel: EpisodeViewModel
    var episodeQueryMap: HashMap<String, String>? = null
    var customProgressDialog: CustomProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.episode_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        episodeQueryMap= HashMap()
        episodeViewModel = ViewModelProviders.of(this).get(EpisodeViewModel::class.java)

        titleEpisodeSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(activity!!,episodeParentll)

                    if (titleEpisodeSearch.text!!.isNotEmpty()) {
                        episodeQueryMap?.clear()
                        episodeQueryMap?.put("i",Constants.i.value)
                        episodeQueryMap?.put("apikey",Constants.apikey.value)
                        episodeQueryMap?.put("type", "episode")
                        episodeQueryMap?.put("t", titleEpisodeSearch.text.toString())
                        episodeViewModel.getEpisode(episodeQueryMap!!)


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
        if (isResumed && isVisibleToUser && episodeQueryMap!!.isEmpty()) {

            loadEpisodeDataViews()
        }else{

        }

    }


    fun loadEpisodeDataViews(){
        customProgressDialog = CustomProgressDialog(activity!!)

        episodeViewModel = ViewModelProviders.of(this).get(EpisodeViewModel::class.java)
        episodeViewModel.setListner(this)
        episodeQueryMap?.put("apikey", Constants.apikey.value)
        episodeQueryMap?.put("type", "episode")
        episodeQueryMap?.put("t", "game")
        episodeViewModel.getEpisode(episodeQueryMap!!)

        episodeViewModel.progressDialog?.observe(activity!!, Observer {

            if (it)customProgressDialog?.show()else customProgressDialog?.dismiss()

        })

        episodeViewModel.episodeResponseObj?.observe(activity!!, Observer { episodeObj ->
                 with(episodeObj){
                     if (Response.equals("True", true)) {
                         episode_card_view.visibility=View.VISIBLE

                         episodeNameValue.text = Title
                         releaseYearValue.text = Released
                         bannerValue.text = Production
                         loadImgfromUrl(Poster!!,posterImg)

                         episode_card_view.setOnClickListener {
                             startActivity(Intent(activity!!, DetailActivity::class.java).apply {
                                 putExtra("OMDBObj",
                                     MovieObj(Response,Error,Title,Year,Rated,Released,Runtime,Production,Poster,Actors,Plot,Awards,Genre,imdbRating)
                                 )
                             })
                         }
                         Log.d("TAG", "episodeObj ${Title}")
                     } else {
                         episode_card_view.visibility=View.GONE
                         showSnackbar(activity!!, episodeParentll, Error!!)
                     }

                 }


        })
    }

    override fun episodeResponseFailure(episodeResponseFailure: String) {
        showSnackbar(activity!!, episodeParentll, episodeResponseFailure)

    }
}
