package com.murdos.qrcodereader

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.zxing.Result
import com.murdos.qrcodereader.data.ApplicationDataBase
import com.murdos.qrcodereader.data.entities.ScannedQR
import com.murdos.qrcodereader.databinding.ActivityMainBinding
import com.murdos.qrcodereader.ui.history.HistoryActivity
import com.murdos.qrcodereader.ui.result.ResultActivity
import kotlinx.coroutines.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.sql.Date
import java.util.*

class MainActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private val TAG = "MainActivity"
    private lateinit var mScannerView: ZXingScannerView
    private lateinit var binding: ActivityMainBinding
    private var isFlashOn: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ApplicationDataBase.getDatabase(this)
        mScannerView = ZXingScannerView(this)
        binding.contentFrame.addView(mScannerView)
        binding.flashButton.setOnClickListener {
            when (isFlashOn) {
                true -> {
                    isFlashOn = false
                    mScannerView.flash = false
                    binding.flashButton.setImageResource(R.drawable.ic_baseline_flash_off_24)
                }
                false -> {
                    isFlashOn = true
                    mScannerView.flash = true
                    binding.flashButton.setImageResource(R.drawable.ic_baseline_flash_on_24)
                }
            }


        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_menu -> {
                val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    override fun onResume() {
        super.onResume()
        requestPermission(binding.mainlayout)

    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera() // Stop camera on pause
    }

    override fun onDestroy() {
        super.onDestroy()
        mScannerView.stopCamera()
    }

    private fun startQRReader() {
        mScannerView.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView.startCamera() // Start camera on resume
    }

    override fun handleResult(rawResult: Result?) {
        // Do something with the result here
        Log.v(TAG, rawResult!!.text); // Prints scan results
        Log.v(
            TAG,
            rawResult.barcodeFormat.toString()
        ); // Prints the scan format (qrcode, pdf417 etc.)
        GlobalScope.launch {
            withContext(Dispatchers.IO) {

                insert(rawResult.text)
                withContext(Dispatchers.Main) {
                    val intent = Intent(this@MainActivity, HistoryActivity::class.java)

                    startActivity(intent)
                }
            }

        }
        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }

    private suspend fun insert(result: String) {
        val db = ApplicationDataBase.getDatabase(this)
        val utilDate = Date()
        val sqlDate = Date(utilDate.time)
        db.scannedQRDAO().insert(ScannedQR(result, sqlDate))
        Log.d("MainActivity", "${db.scannedQRDAO().getAll().toList().size}")
    }

    private fun requestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startQRReader()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                Log.d("MainActivity", "Hey we really want you to give us the permission")
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }
}