package com.example.edison.Util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.voter.omdbtestapplication.R

class CustomProgressDialog(context: Context?) : Dialog(context!!) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_progress)

        setCancelable(false)
        setTitle(null)
        setCanceledOnTouchOutside(false)

    }


}