package com.solarnet.demo.data.trx

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import android.os.AsyncTask
import com.solarnet.demo.data.converter.TimestampConverter
import java.util.*

@Database(entities = [(Trx::class)], version = 1)
@TypeConverters(TimestampConverter::class)
abstract class TrxRoomDatabase : RoomDatabase() {
    abstract fun trxDao() : TrxDao

    companion object {
        private var INSTANCE : TrxRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): TrxRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(TrxRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        // Create database here
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                TrxRoomDatabase::class.java!!, "trx_database")
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

        private class PopulateDbAsync internal constructor(db: TrxRoomDatabase) : AsyncTask<Void, Void, Void>() {

            private val mDao: TrxDao = db.trxDao()


            override fun doInBackground(vararg params: Void): Void? {
                mDao.deleteAll()
                mDao.insert(Trx(Trx.ICON_TOP_UP, "Top Up", 100000, "Top up berhasil!", Trx.STATUS_SUCCESS,
                        "1111", Calendar.getInstance().time))
                mDao.insert(Trx(Trx.ICON_TOP_UP, "Solarnet", 50000, "Pembayaran berhasil!", Trx.STATUS_SUCCESS,
                        "2222", Calendar.getInstance().time))
                mDao.insert(Trx(Trx.ICON_TOP_UP, "Solarnet", 60000, "Pembayaran berhasil!", Trx.STATUS_SUCCESS,
                        "2222", Calendar.getInstance().time))
                mDao.insert(Trx(Trx.ICON_TOP_UP, "Solarnet", 70000, "Pembayaran berhasil!", Trx.STATUS_SUCCESS,
                        "2222", Calendar.getInstance().time))
                mDao.insert(Trx(Trx.ICON_TOP_UP, "Solarnet", 80000, "Pembayaran berhasil!", Trx.STATUS_SUCCESS,
                        "2222", Calendar.getInstance().time))
                mDao.insert(Trx(Trx.ICON_TOP_UP, "Solarnet", 90000, "Pembayaran berhasil!", Trx.STATUS_SUCCESS,
                        "2222", Calendar.getInstance().time))

//
//                var word = Note("Hello", "Hello Text", Constants.NOTE_TYPE_TEXT, 0)
//                mDao.insert(word)
//                word = Note("World", "Hello World\nHello World 1\nHello World 2\nHello World 3",
//                        Constants.NOTE_TYPE_TEXT, 1)
//                mDao.insert(word)
                return null
            }
        }
    }
}