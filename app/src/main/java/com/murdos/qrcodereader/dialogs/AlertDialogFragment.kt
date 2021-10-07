package com.murdos.qrcodereader.dialogs


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.murdos.qrcodereader.R
import com.murdos.qrcodereader.data.entities.ScannedQR
import com.murdos.qrcodereader.listeners.AlertDialogClickListener

class AlertDialogFragment(scannedQR: ScannedQR, alertDialogClickListener: AlertDialogClickListener) :
    DialogFragment() {
    private val mDialogInterfaceClickListener = alertDialogClickListener
    private val mScannedQR = scannedQR
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.delete)
            builder.setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.ok, DialogInterface.OnClickListener{
                    dialogInterface, i ->
                    mDialogInterfaceClickListener.remove(mScannedQR)
                    dialogInterface.dismiss()
                })
                .setNegativeButton(
                    R.string.cancel,
                    DialogInterface.OnClickListener{
                            dialogInterface, i ->dialogInterface.dismiss()
                    }
                )
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}