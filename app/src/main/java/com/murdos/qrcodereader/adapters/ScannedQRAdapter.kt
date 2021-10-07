package com.murdos.qrcodereader.adapters

import android.R.attr
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.murdos.qrcodereader.R
import com.murdos.qrcodereader.data.entities.ScannedQR
import android.app.Dialog
import android.content.*

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.murdos.qrcodereader.data.ApplicationDataBase
import com.murdos.qrcodereader.dialogs.AlertDialogFragment
import com.murdos.qrcodereader.listeners.OnScannedQRClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Duration


class ScannedQRAdapter(
    val data: List<ScannedQR>,
    scannedQRClickListener: OnScannedQRClickListener
) :
    RecyclerView.Adapter<ScannedQRAdapter.ViewHolder>() {
    private val mScannedQRClickListener = scannedQRClickListener

    class ViewHolder(view: View, scannedQRClickListener: OnScannedQRClickListener) :
        RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.text)
        val copyButton: Button = view.findViewById(R.id.copyButton)
        val shareButton: Button = view.findViewById(R.id.shareButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.scanned_qr_item, parent, false)
        return ViewHolder(view, mScannedQRClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = this.data[position].result


        holder.itemView.setOnClickListener {
            mScannedQRClickListener.onItemClickedListener(position)
        }

        holder.copyButton.setOnClickListener {
            mScannedQRClickListener.onCopyClickedListener(data[position])
        }
        holder.shareButton.setOnClickListener {
            mScannedQRClickListener.onShareClickedListener(data[position])
        }
        holder.deleteButton.setOnClickListener {
            mScannedQRClickListener.onRemoveClickedListener(data[position])

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
//
//    fun confirm(context: Context, scannedQR: ScannedQR): AlertDialog {
//        val db = ApplicationDataBase.getDatabase(context)
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle(R.string.delete)
//        builder.setMessage("Would you like to remove ${scannedQR.result}?")
//        builder.setOnDismissListener {
//
//        }
//        builder.apply {
//            setPositiveButton(R.string.ok,
//                DialogInterface.OnClickListener { dialog, id ->
//                    // User clicked OK button
//                    GlobalScope.launch {
//                        with(Dispatchers.IO){
//                            db.scannedQRDAO().remove(scannedQR)
//                        }
//                    }
//                    dialog.dismiss()
//                })
//            setNegativeButton(R.string.cancel,
//                DialogInterface.OnClickListener { dialog, id ->
//                    // User cancelled the dialog
//                    dialog.dismiss()
//                })
//        }
//        return builder.create()
//    }

}