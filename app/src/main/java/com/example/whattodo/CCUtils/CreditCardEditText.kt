package com.example.whattodo.CCUtils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText

/**
 * Created by Jaison on 29/05/17.
 */
class CreditCardEditText : AppCompatEditText {
    var cc_context: Context = getContext()
    private lateinit var backButtonListener: BackButtonListener

    constructor(context: Context) : super(context) {
        this.cc_context = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.cc_context = context
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.cc_context = context
    }

    fun setOnBackButtonListener(listener: BackButtonListener) {
        backButtonListener = listener
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            // User has pressed Back key. So hide the keyboard
            Log.d("ET", "onKeyPreIme: ")
            if (backButtonListener != null) backButtonListener.onBackClick()
            return true
        }
        return false
    }

    interface BackButtonListener {
        fun onBackClick()
    }
}