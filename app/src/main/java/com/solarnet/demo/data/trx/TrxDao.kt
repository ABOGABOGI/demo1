package com.solarnet.demo.data.trx

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface TrxDao {
    @Insert
    fun insert(trx: Trx) : Long

    @Update
    fun updateNote(trx: Trx)

    @Query("DELETE FROM trx")
    fun deleteAll()

    @Query("SELECT * from trx ORDER BY created_date DESC")
    fun getAllTrx(): LiveData<List<Trx>>

    @Query("SELECT * from trx ORDER BY created_date DESC limit 0,:limit")
    fun getLatestTrx(limit : Int): LiveData<List<Trx>>

    @Query("DELETE from trx WHERE id = :id")
    fun deleteById(id : Long)

    @Query("SELECT * from trx WHERE id = :id")
    fun getTrxById(id : Long): LiveData<List<Trx>>
}