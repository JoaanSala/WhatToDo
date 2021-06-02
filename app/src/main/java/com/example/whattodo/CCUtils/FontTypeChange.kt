package com.example.whattodo.CCUtils

import android.content.Context
import android.graphics.Typeface

class FontTypeChange     // TODO Auto-generated constructor stub
(private val c: Context?) {
    fun get_fontface(n: Int): Typeface {
        val tf: Typeface
        tf = if (n == 1) Typeface.createFromAsset(c!!.assets, "fonts/kreditback.ttf") else if (n == 2) Typeface.createFromAsset(c!!.assets, "fonts/kreditfront.ttf") else if (n == 3) Typeface.createFromAsset(c!!.assets, "fonts/ocramedium.ttf") else Typeface.createFromAsset(c!!.assets, "fonts/kreditfront.ttf")
        return tf
    }
}