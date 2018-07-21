package com.solarnet.demo.data.contact

import android.app.Application
import android.arch.lifecycle.LiveData
import com.solarnet.demo.data.AppRoomDatabase
import com.solarnet.demo.data.trx.Trx
import com.solarnet.demo.data.trx.TrxDao
import com.solarnet.demo.data.trx.TrxViewModel

class ContactRepository(application: Application) {
    private var mContactDao : ContactDao
    private var mContacts : LiveData<List<Contact>>

    init {
        val db = AppRoomDatabase.getDatabase(application)
        mContactDao = db!!.contactDao()
        //mAllNotes.value = mNoteDao.getAllNotes()
        mContacts = mContactDao.getAllContacts()
    }

    fun getContacts() : LiveData<List<Contact>> {
        return mContacts
    }
}