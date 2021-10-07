package com.murdos.qrcodereader.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.sql.Date
import java.util.*
@Entity(tableName = "scanned_codes")
data class ScannedQR(@ColumnInfo(name = "result") val result: String, @ColumnInfo(name = "creation_date")  val creationDate: Date){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
