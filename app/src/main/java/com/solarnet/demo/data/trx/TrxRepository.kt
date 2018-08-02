package com.solarnet.demo.data.trx

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.solarnet.demo.data.AppRoomDatabase

class TrxRepository(application: Application, limit : Int = TrxViewModel.ALL_TRX) {
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

    fun getTrx(id : Long) : LiveData<List<Trx>> {
        return mTrxDao.getTrxById(id)
    }


    fun insert(trx: Trx, listener : OnInsertListener? = null) {
        InsertAsyncTask(mTrxDao, listener).execute(trx)
    }

    interface OnInsertListener {
        fun onInsert(id : Long)
    }
    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: TrxDao,
                                                       private val listener : OnInsertListener?)
        : AsyncTask<Trx, Void, Long>() {

        override fun doInBackground(vararg params: Trx): Long {
            return mAsyncTaskDao.insert(params[0])
        }

        override fun onPostExecute(result: Long) {
            listener?.onInsert(result)
        }
    }
}