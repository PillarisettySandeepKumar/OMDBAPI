package com.voter.omdbtestapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize data class MovieObj(var Response:String?,var Error:String?,var Title:String?,var Year:String?,var Rated:String?,var Released:String?,var Runtime:String?,var Production:String?,var Poster:String?,var Actors:String?,var Plot:String?,var Awards:String?,var Genre:String?,var imdbRating:String?):Parcelable

