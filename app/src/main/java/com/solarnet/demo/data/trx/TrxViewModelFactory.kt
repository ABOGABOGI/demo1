package com.solarnet.demo.data.trx

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModel



class TrxViewModelFactory(private var mApplication: Application,
                          private var mLimit : Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrxViewModel(mApplication, mLimit) as T
    }
}