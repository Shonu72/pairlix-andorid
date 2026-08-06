package com.gravito.waiter_.Localization

import android.content.Context
import android.widget.Toast

object Const {
    var lang = "en"
    var countryCode=""
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}