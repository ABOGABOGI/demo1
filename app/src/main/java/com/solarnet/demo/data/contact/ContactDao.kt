package com.solarnet.demo.data.contact

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.solarnet.demo.data.trx.Trx

@Dao
interface ContactDao {
    @Insert
    fun insert(contact : Contact)

    @Query("DELETE FROM contacts")
    fun deleteAll()

    @Update
    fun updateContact(contact : Contact)

//    @Query("SELECT * from contacts WHERE name LIKE :name ORDER BY name")
//    fun getContact(name : String): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts ORDER BY name")
    fun getAllContacts() : LiveData<List<Contact>>

    @Query("DELETE from contacts WHERE id = :id")
    fun deleteById(id : Long)


}