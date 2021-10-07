package com.murdos.qrcodereader.listeners

import com.murdos.qrcodereader.data.entities.ScannedQR

interface OnScannedQRClickListener {
    fun onItemClickedListener(position: Int)
    fun onCopyClickedListener(scannedQR: ScannedQR)
    fun onShareClickedListener(scannedQR: ScannedQR)
    fun onRemoveClickedListener(scannedQR: ScannedQR)
}