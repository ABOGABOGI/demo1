package com.solarnet.demo.data.contact

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName =  "contacts")
class Contact(
        @ColumnInfo(name = "image") var image: String? = null,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "token") var token: String
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}