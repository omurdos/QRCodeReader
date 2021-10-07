package com.murdos.qrcodereader.ui.history

import android.app.SearchManager
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.murdos.qrcodereader.R
import com.murdos.qrcodereader.adapters.ScannedQRAdapter
import com.murdos.qrcodereader.data.ApplicationDataBase
import com.murdos.qrcodereader.data.entities.ScannedQR
import com.murdos.qrcodereader.databinding.ActivityHistoryBinding
import com.murdos.qrcodereader.dialogs.AlertDialogFragment
import com.murdos.qrcodereader.listeners.AlertDialogClickListener
import com.murdos.qrcodereader.listeners.OnScannedQRClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity(), OnScannedQRClickListener, AlertDialogClickListener {
    private lateinit var binding: ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.history)
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.history_option_menu, menu)
//// Get the SearchView and set the searchable configuration
////        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
////        (menu.findItem(R.id.search).actionView as SearchView).apply {
////            // Assumes current activity is the searchable activity
////            setSearchableInfo(searchManager.getSearchableInfo(componentName))
////            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
////        }
//
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        super.onOptionsItemSelected(item)
//        when(item.itemId){
//            (R.id.searchAction) ->{
//                onSearchRequested()
//            }
//        }
//        return true
//    }



    private fun loadData() {
        val db = ApplicationDataBase.getDatabase(this)
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val data = db.scannedQRDAO().getAll()
                withContext(Dispatchers.Main) {
                    binding.scannedQRRecycler.layoutManager =
                        LinearLayoutManager(this@HistoryActivity)
                    binding.scannedQRRecycler.adapter = ScannedQRAdapter(data, this@HistoryActivity)
                }
            }
        }
    }

    override fun onItemClickedListener(position: Int) {
        Log.d("History Activity", "onClickListener: $position")

    }

    override fun onCopyClickedListener(scannedQR: ScannedQR) {
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(scannedQR.result, scannedQR.result)
        clipboard.setPrimaryClip(clip)
        Snackbar.make(binding.root, R.string.textCopied, Snackbar.LENGTH_SHORT).show()
    }

    override fun onShareClickedListener(scannedQR: ScannedQR) {
        val sendIntent: Intent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, scannedQR.result)
        sendIntent.type = "text/plain"
        val shareIntent = Intent.createChooser(sendIntent, scannedQR.result)
        startActivity(shareIntent)
    }

    override fun onRemoveClickedListener(scannedQR: ScannedQR) {
        AlertDialogFragment(scannedQR, this).show(supportFragmentManager, "Confirm")

    }

    override fun remove(scannedQR: ScannedQR) {
        val db = ApplicationDataBase.getDatabase(this)
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                db.scannedQRDAO().remove(scannedQR)
            }
            withContext(Dispatchers.Main) {
                loadData()
            }
        }
    }


}