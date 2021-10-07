package com.murdos.qrcodereader.listeners

import android.content.DialogInterface
import com.murdos.qrcodereader.data.entities.ScannedQR

interface AlertDialogClickListener {
    fun remove(scannedQR: ScannedQR)
}