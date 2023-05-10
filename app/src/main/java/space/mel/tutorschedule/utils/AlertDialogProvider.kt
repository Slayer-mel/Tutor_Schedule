package space.mel.tutorschedule.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.TextView
import space.mel.tutorschedule.R

object AlertDialogProvider{

    fun createAlertInstance(
        context:Context,
        layout: Int,
        onDelete: () -> Unit,
    ) : AlertDialog {
        val dialog = AlertDialog.Builder(context).create()
        val viewAlert = View.inflate(
            context,
            layout,
            null)
        val btnCancel = viewAlert.findViewById<TextView>(R.id.btnCancel)
        val btnDelete = viewAlert.findViewById<TextView>(R.id.btnDelete)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnDelete.setOnClickListener {
            onDelete()
            dialog.dismiss()
        }
        with(dialog) {
            setView(viewAlert)
            setCancelable(false)
        }
        return dialog
    }
}