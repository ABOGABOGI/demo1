package com.solarnet.demo.data.product

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Delete
import android.os.AsyncTask
import com.solarnet.demo.data.AppRoomDatabase
import com.solarnet.demo.data.trx.TrxViewModel

class ProductRepository(application : Application) {
    private var mProductDao : ProductDao
    private var mProductAll : LiveData<List<Product>>
    interface OnInsertListener {
        fun insertCompleted(product : Product)
    }
    var listenerInsert : OnInsertListener? = null

    init {
        val db = AppRoomDatabase.getDatabase(application)
        mProductDao = db!!.productDao()
        mProductAll = mProductDao.getAll()
    }

    fun getAll() : LiveData<List<Product>> {
        return mProductAll
    }

    fun insert(product: Product) {
        InsertAsyncTask(mProductDao, listenerInsert).execute(product)
    }

    fun delete(id : Long) {
        DeleteAsyncTask(mProductDao).execute(id)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: ProductDao,
                                                       private val listenerInsert : OnInsertListener? = null) :
            AsyncTask<Product, Void, Product>() {

        override fun doInBackground(vararg params: Product): Product {
            val id = mAsyncTaskDao.insert(params[0])
            params[0].id = id
            return params[0]
        }

        override fun onPostExecute(result: Product) {
            listenerInsert?.insertCompleted(result)
        }
    }

    private class DeleteAsyncTask internal constructor(private val mAsyncTaskDao: ProductDao) :
            AsyncTask<Long, Void, Void>() {
        override fun doInBackground(vararg params: Long?): Void? {
            mAsyncTaskDao.delete(params[0]!!)
            return null
        }

    }
}