package com.solarnet.demo.data.trx
import android.arch.persistence.room.*
import java.util.Date
import com.solarnet.demo.data.converter.TimestampConverter
import com.solarnet.demo.R

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
    @ColumnInfo(name = "created_date")  @TypeConverters(TimestampConverter::class) var createdDate: Date,
    @ColumnInfo(name = "expired_date")  @TypeConverters(TimestampConverter::class) var expiredDate: Date?,
    @ColumnInfo(name = "data") var data: String?
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    companion object {
        const val STATUS_FAILED: Int = 0
        const val STATUS_SUCCESS: Int = 1
        const val STATUS_WAITING_FOR_TRANSFER: Int = 2

        const val ICON_TOP_UP: Int = 0
        const val ICON_SEND_MONEY: Int = 1
        const val ICON_CELULLAR: Int = 2
        const val ICON_PLN: Int = 3
        const val ICON_INVOICE: Int = 4
        const val ICON_TO_BANK: Int = 5
        const val ICON_QR_PAY: Int = 6
    }

    fun getIconResource() : Int {
        return when (iconId) {
            ICON_TOP_UP -> R.drawable.ic_top_up
            ICON_SEND_MONEY -> R.drawable.ic_send_money
            ICON_CELULLAR -> R.drawable.ic_cellular
            ICON_PLN -> R.drawable.ic_pln
            ICON_INVOICE -> R.drawable.ic_invoice
            ICON_TO_BANK -> R.drawable.ic_bank
            ICON_QR_PAY -> R.drawable.ic_qr
            else -> R.drawable.ic_launcher_foreground
        }
    }

    @Ignore
    constructor(iconId: Int, title : String, amount : Int, message : String, status : Int,
                transactionId : String, createdDate : Date) :
            this(iconId, title, amount,"", "", message,
                    status, transactionId, createdDate, null, null)

}