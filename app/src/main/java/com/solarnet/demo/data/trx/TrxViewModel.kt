package com.solarnet.demo.data.trx

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

class TrxViewModel(application : Application, limit : Int = ALL_TRX) : AndroidViewModel(application) {
    private val mApplication: Application = application
    private val mRepository : TrxRepository = TrxRepository(application, limit)
    private val mAllTrx : LiveData<List<Trx>>

    companion object {
        const val ALL_TRX = 0
        const val LATEST = 1
    }
    init {
       mAllTrx = mRepository.getAllTrx()
    }

    fun getAllTransactions() : LiveData<List<Trx>> {
        return mAllTrx
    }
}