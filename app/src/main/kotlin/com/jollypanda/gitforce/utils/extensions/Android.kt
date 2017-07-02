package com.jollypanda.gitforce.utils.extensions

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.jollypanda.gitforce.R

/**
 * @author Yamushev Igor
 * @since  15.06.17
 */
fun AppCompatActivity.alert(context: Context, alert: String) {
    AlertDialog.Builder(this)
            .setMessage(alert)
            .setPositiveButton(context.getString(R.string.btn_close),
                               { dialog, _ ->
                                   dialog.dismiss()
                               })
            .create()
            .show()
}