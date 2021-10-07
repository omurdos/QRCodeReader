package com.murdos.qrcodereader.data.dao

import androidx.room.*
import com.murdos.qrcodereader.data.entities.ScannedQR
import kotlinx.coroutines.flow.Flow

@Dao
interface ScannedQRDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(scannedQR: ScannedQR)
    @Query("SELECT * FROM scanned_codes ORDER BY creation_date DESC")
    fun getAll() : List<ScannedQR>
    @Delete
    suspend fun remove(scannedQR: ScannedQR)
}