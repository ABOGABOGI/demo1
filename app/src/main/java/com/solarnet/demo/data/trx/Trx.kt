package com.solarnet.demo.data.trx
import android.arch.persistence.room.*
import java.util.Date
import com.solarnet.demo.data.converter.TimestampConverter

@Entity(tableName = "trx")
class Trx (
    @ColumnInfo(name = "iconId") var iconId: Int = ICON_TOP_UP,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "amount") var amount: Int,
    @ColumnInfo(name = "subtitle1") var subtitle1 : String,
    @ColumnInfo(name = "subtitle2") var subtitle2 : String,
    @ColumnInfo(name = "message") var message : String,
    @ColumnInfo(name = "status") var status : Int,
    @ColumnInfo(name = "transaction_id") var transactionId : String,
    @ColumnInfo(name = "created_date")  @TypeConverters(TimestampConverter::class) var createdDate: Date
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    companion object {
        const val STATUS_SUCCESS: Int = 1

        const val ICON_TOP_UP: Int = 0
        const val ICON_SEND_MONEY: Int = 1
        const val ICON_CELULLAR: Int = 2
        const val ICON_PLN: Int = 3
        const val ICON_INVOICE: Int = 4
        const val ICON_TO_BANK: Int = 5
        const val ICON_QR_PAY: Int = 6
    }

    @Ignore
    constructor(iconId: Int, title : String, amount : Int, message : String, status : Int,
                transactionId : String, createdDate : Date) :
            this(iconId, title, amount,"", "", message,
                    status, transactionId, createdDate)


}