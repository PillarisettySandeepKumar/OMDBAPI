package com.voter.omdbtestapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.edison.Util.loadImgfromUrl
import com.voter.omdbtestapplication.model.MovieObj
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val omdbObj=  intent.getParcelableExtra<MovieObj>("OMDBObj")
        with(omdbObj){

            loadImgfromUrl(Poster!!,toolbarImage)
            collapsingToolbar.title=Title
            releaseTv.text=Released
            runTimetv.text=Runtime
            actorTv.text=Actors
            awardsTv.text=Awards
            genreTv.text=Genre
            plotTv.text=Plot
            ratingTv.text=imdbRating
        }



    }

}
