package pl.kamilbaziak.userslistviewer.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import pl.kamilbaziak.userslistviewer.R


object Utils {
    const val serverName = "https://api.github.com/"

    //function returning dialog when there is no netwok
    fun showNoNetworkAvailableDialog(context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setMessage(context.getString(R.string.noNetworkConnectionDialogMessage))
        builder.setTitle(context.getString(R.string.noNetworkConnectionDIalogTitle))
        builder.setPositiveButton(context.getString(R.string.ok_text),
            DialogInterface.OnClickListener { dialog, which ->
                context.startActivity(
                    Intent(
                        Settings.ACTION_WIRELESS_SETTINGS
                    )
                )
            })

        builder.setNegativeButton(context.getString(R.string.cancel_text),
            DialogInterface.OnClickListener { dialog, which -> return@OnClickListener })

        builder.setOnCancelListener(DialogInterface.OnCancelListener { return@OnCancelListener })

        builder.show()
    }
}