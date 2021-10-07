package com.murdos.qrcodereader.data

import android.content.Context
import androidx.room.*
import com.murdos.qrcodereader.data.dao.ScannedQRDAO
import com.murdos.qrcodereader.data.entities.ScannedQR

@Database(entities = arrayOf(ScannedQR::class), version = 1, exportSchema = true )
@TypeConverters(Converters::class)
abstract class ApplicationDataBase : RoomDatabase() {
    abstract fun scannedQRDAO() : ScannedQRDAO
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ApplicationDataBase? = null

        fun getDatabase(context: Context): ApplicationDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            val converters = Converters()
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ApplicationDataBase::class.java,
                    "qr_reader_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}