package com.app.rickandmortymvi.util

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.SystemClock
import android.view.View
import android.view.animation.LinearInterpolator


class SafeClickListener(
    private var defaultInterval: Int = 400,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        v.clickAnimation()
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun View.clickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun View.clickAnimation() {
    alpha = 0.1f
    animate().apply {
        interpolator = LinearInterpolator()
        duration = 100
        alpha(1f)
        startDelay = 200
        start()
    }
}

var mProgressDialog: ProgressDialog? = null
fun Context.showProgressDialog() {
    if (mProgressDialog == null) {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.setMessage("Loading...")
        mProgressDialog!!.isIndeterminate = true
    }
    mProgressDialog!!.show()
}


fun hideProgressDialog() {
    if (mProgressDialog != null && mProgressDialog!!.isShowing) {
        mProgressDialog!!.dismiss()
    }
}

fun alertBox(title: String?, message: String?, activiy: Activity?) {
    val alertDialog: AlertDialog = AlertDialog.Builder(activiy).create()
    alertDialog.setTitle(title)
    alertDialog.setMessage(message)
    alertDialog.setButton("TAMAM",
        DialogInterface.OnClickListener { dialog, which -> alertDialog.cancel() })
    alertDialog.show()
}