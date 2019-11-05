package com.example.edison.Util

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_fragment.*

fun showSnackbar(context: Context,view: View,msg:String){
    hideKeyboard(context,view)
    Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        .show()
}

fun hideKeyboard(context: Context,view: View){
    val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun loadImgfromUrl(poster:String,imageView:ImageView){

    Picasso.get().load(poster).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);

}