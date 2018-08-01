package com.solarnet.demo.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import android.os.AsyncTask
import com.solarnet.demo.data.contact.Contact
import com.solarnet.demo.data.contact.ContactDao
import com.solarnet.demo.data.converter.TimestampConverter
import com.solarnet.demo.data.product.Product
import com.solarnet.demo.data.product.ProductDao
import com.solarnet.demo.data.trx.Trx
import com.solarnet.demo.data.trx.TrxDao
import java.util.*

@Database(entities = [(Trx::class),(Contact::class), (Product::class)], version = 1)
@TypeConverters(TimestampConverter::class)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun trxDao() : TrxDao
    abstract fun contactDao() : ContactDao
    abstract fun productDao() : ProductDao

    companion object {
        private var INSTANCE : AppRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): AppRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(AppRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        // Create database here
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                AppRoomDatabase::class.java!!, "trx_database")
                                .addCallback(sRoomDatabaseCallback)
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                if (INSTANCE != null) {
                    PopulateDbAsync(INSTANCE!!).execute()
                }
            }
        }

        private class PopulateDbAsync internal constructor(db: AppRoomDatabase) : AsyncTask<Void, Void, Void>() {

            private val mTrxDao: TrxDao = db.trxDao()
            private val mContactDao : ContactDao = db.contactDao()
            private val mProductDao : ProductDao = db.productDao()

            override fun doInBackground(vararg params: Void): Void? {
//                mTrxDao.deleteAll()
//                mTrxDao.insert(Trx(Trx.ICON_TOP_UP, "Top Up", 100000, "Top up berhasil!", Trx.STATUS_SUCCESS,
//                        "1111", Calendar.getInstance().time))
//                mTrxDao.insert(Trx(Trx.ICON_TOP_UP, "Solarnet", 50000, "Pembayaran berhasil!", Trx.STATUS_SUCCESS,
//                        "2222", Calendar.getInstance().time))
//                mTrxDao.insert(Trx(Trx.ICON_TOP_UP, "Solarnet", 60000, "Pembayaran berhasil!", Trx.STATUS_SUCCESS,
//                        "2222", Calendar.getInstance().time))
//                mTrxDao.insert(Trx(Trx.ICON_TOP_UP, "Solarnet", 70000, "Pembayaran berhasil!", Trx.STATUS_SUCCESS,
//                        "2222", Calendar.getInstance().time))
//                mTrxDao.insert(Trx(Trx.ICON_TOP_UP, "Solarnet", 80000, "Pembayaran berhasil!", Trx.STATUS_SUCCESS,
//                        "2222", Calendar.getInstance().time))
//                mTrxDao.insert(Trx(Trx.ICON_TOP_UP, "Solarnet", 90000, "Pembayaran berhasil!", Trx.STATUS_SUCCESS,
//                        "2222", Calendar.getInstance().time))


                mContactDao.deleteAll()
                mContactDao.insert(Contact(null, "Nugroho Priambodo", "NUG01"))
                mContactDao.insert(Contact(null, "Crayon Sinchan", "CRA01"))
                mContactDao.insert(Contact(null, "Contact Name 1", "CON01"))
                mContactDao.insert(Contact(null, "Contact Name 2", "CON02"))

                mProductDao.deleteAll()
                mProductDao.insert(Product("BEK001", "Nasi Goreng Ayam", 20000,
                        System.currentTimeMillis()))
                mProductDao.insert(Product("BEK002", "Nasi Goreng Kambing", 25000,
                        System.currentTimeMillis()))
                mProductDao.insert(Product("BEK003", "Nasi Goreng Seafood", 25000,
                        System.currentTimeMillis()))
                mProductDao.insert(Product("BEK004", "Soto Ayam", 18000,
                        System.currentTimeMillis()))
                mProductDao.insert(Product("BEK005", "Soto Daging", 22000,
                        System.currentTimeMillis()))

                return null
            }
        }
    }
}