package com.solarnet.demo.data.product

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface ProductDao {
    @Insert
    fun insert(product: Product) : Long

    @Query("DELETE FROM products")
    fun deleteAll()

    @Query("DELETE FROM products WHERE id = :id")
    fun delete(id : Long)

    @Query("SELECT * from products ORDER BY updated_at DESC")
    fun getAll(): LiveData<List<Product>>

    @Update
    fun update(product: Product)

}