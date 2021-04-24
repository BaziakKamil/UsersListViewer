package pl.kamilbaziak.userslistviewer.utils

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings


class Utils {
    companion object {
        const val serverName = "https://api.github.com/"

        //function returning dialog when there is no netwok
        fun showNoNetworkAvailableDialog(context: Context) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setMessage("No network connection, check if it is enabled or enable it")
            builder.setTitle("No netwok connection")
            builder.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, which ->
                    context.startActivity(
                        Intent(
                            Settings.ACTION_WIRELESS_SETTINGS
                        )
                    )
                })

            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which -> return@OnClickListener })

            builder.setOnCancelListener(DialogInterface.OnCancelListener { return@OnCancelListener })

            builder.show()
        }
    }
}