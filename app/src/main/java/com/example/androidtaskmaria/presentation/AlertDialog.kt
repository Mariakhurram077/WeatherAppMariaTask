package com.example.androidtaskmaria.presentation

import android.app.AlertDialog
import android.content.Context
import com.example.androidtaskmaria.R

object AlertDialog {
     fun showAlertDialog(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.alert))
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}