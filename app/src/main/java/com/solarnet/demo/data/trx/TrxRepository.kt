package com.solarnet.demo.data.trx

import android.app.Application
import android.arch.lifecycle.LiveData
import com.solarnet.demo.data.AppRoomDatabase

class TrxRepository(application: Application, limit : Int) {
    private var mTrxDao : TrxDao
    private var mAllTrx : LiveData<List<Trx>>

    init {
        val db = AppRoomDatabase.getDatabase(application)
        mTrxDao = db!!.trxDao()
        //mAllNotes.value = mNoteDao.getAllNotes()
        if (limit == TrxViewModel.ALL_TRX) {
            mAllTrx = mTrxDao.getAllTrx()
        } else {
            mAllTrx = mTrxDao.getLatestTrx(limit)
        }
    }

    fun getAllTrx() : LiveData<List<Trx>> {
        return mAllTrx
    }

}