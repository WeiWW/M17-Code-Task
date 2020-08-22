package com.ann.m17test.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun hideSoftKeyboard(mActivity: Activity) {
    try {
        val view = mActivity.currentFocus
        if (view != null) {
            val inputManager: InputMethodManager =
                mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}