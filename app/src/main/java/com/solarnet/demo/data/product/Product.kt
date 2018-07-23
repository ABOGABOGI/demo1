package com.solarnet.demo.data.product

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "products")
class Product(
        @ColumnInfo(name = "code") var code : String,
        @ColumnInfo(name = "description") var description : String,
        @ColumnInfo(name = "price") var price : Int,
        @ColumnInfo(name = "updated_at") var updatedAt : Long
){
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0

    @Ignore
    constructor(description: String, price : Int) : this("", description, price,
            System.currentTimeMillis())
}