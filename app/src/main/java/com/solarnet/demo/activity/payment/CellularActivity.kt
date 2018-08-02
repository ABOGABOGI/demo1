package com.solarnet.demo.activity.payment

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import com.solarnet.demo.R

import kotlinx.android.synthetic.main.activity_celullar.*
import android.text.Spanned
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.content_celullar.*
import android.widget.Toast
import com.solarnet.demo.MainActivity
import co.ceryle.segmentedbutton.SegmentedButtonGroup
import com.solarnet.demo.MyApp
import com.solarnet.demo.R.id.segmentedButtonGroup
import com.solarnet.demo.data.util.Utils
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.CursorLoader
import android.content.DialogInterface
import android.widget.ProgressBar
import com.solarnet.demo.activity.TrxActivity
import com.solarnet.demo.data.trx.Trx
import com.solarnet.demo.data.trx.TrxRepository
import com.solarnet.demo.data.util.DecimalDigitFilter
import com.solarnet.demo.network.PostTrx
import okhttp3.Call


class CellularActivity : BaseActivity(), PostTrx.TrxListener {
    private val ACTIVITY_CONTACT = 120
    private lateinit var mViewModel : AppViewModel

    override fun getTrxRepository() : TrxRepository {
        return mViewModel.mRepository
    }

    override fun next() {
        var type : String = when(mViewModel.isTopUp) {
            true -> getString(R.string.pulsa)
            else -> getString(R.string.paket_data)
        }

         showProgress(true)
        mPostTrx.postCellular(editPhone.text.toString(), type, textHint.text.toString(),
                mViewModel.cellProduct.getProductCode(mViewModel.isTopUp,
                        mViewModel.indexProduct),
                mViewModel.cellProduct.getProductValue(mViewModel.isTopUp,
                        mViewModel.indexProduct))

    }

    fun checkMenuNext() {
        menuNext?.isEnabled = mViewModel.balance >= 0 &&
                mViewModel.indexProduct >=0 &&
                mViewModel.phoneNumber.length > 4
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_celullar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        mViewModel.initBalance = MyApp.instance.getBalance()
        mViewModel.balance = mViewModel.initBalance

        editPhone.filters = arrayOf(DecimalDigitFilter())
        editPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mViewModel.phoneNumber = s.toString()
                if (s != null) {
                    val prefix = CellProduct.parsePrefix(s.toString())
                    if (prefix.compareTo(mViewModel.cellProduct.prefix) != 0) {
                            //prefix updated
                        mViewModel.cellProduct = CellProduct(prefix)
                        notifyPrefixUpdated()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        segmentedButtonGroup.setOnPositionChangedListener {
            pos ->
            when (pos) {
                0 -> textHint.setText(R.string.hint_pulsa)
                1 -> textHint.setText(R.string.hint_data)
            }
            textHint.setTextColor(Color.GRAY)
            mViewModel.indexProduct = -1
            mViewModel.balance = mViewModel.initBalance
            mViewModel.isTopUp = pos == 0
            notifyBalanceUpdated()
        }

        buttonPhonebook.setOnClickListener{_ ->
            startActivityForResult(Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI).apply {
                type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            }, ACTIVITY_CONTACT)
        }

        cardProduct.setOnClickListener{_ ->
            if (mViewModel.isTopUp) {
                showAlertList(resources.getString(R.string.hint_pulsa),
                        mViewModel.cellProduct.topUpAmount)
            } else {
                showAlertList(resources.getString(R.string.hint_data),
                        mViewModel.cellProduct.dataPlans)
            }
        }
        notifyPrefixUpdated()
        notifyBalanceUpdated()
    }

    fun notifyProductUpdated() {
        if (mViewModel.isTopUp) {
            val str = mViewModel.cellProduct.topUpAmount[mViewModel.indexProduct]
            textHint.text = str
        } else {
            val str = mViewModel.cellProduct.dataPlans[mViewModel.indexProduct]
            textHint.text = str
        }

        textHint.setTextColor(Color.BLACK)
        mViewModel.balance = mViewModel.initBalance - mViewModel.cellProduct.getProductValue(
                mViewModel.isTopUp, mViewModel.indexProduct)
        notifyBalanceUpdated()
    }

    fun showAlertList(title : String, items : Array<String>) {
        var builder : AlertDialog.Builder = AlertDialog.Builder(this).apply {
            setTitle(title)
            setItems(items, DialogInterface.OnClickListener{
                dlg, item ->
                mViewModel.indexProduct = item
                notifyProductUpdated()
                dlg.dismiss()
            })
        }

        var alert = builder.create()
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_CONTACT) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val contactsData = data.data
                val loader = CursorLoader(this, contactsData, null, null, null, null)
                val c = loader.loadInBackground()
                if (c.moveToFirst()) {
                    var phone = c.getString(c.getColumnIndex(Phone.NUMBER))
                    phone = Utils.filterNonDigit(phone)
                    editPhone.setText(phone)
                }
            }
        }
    }

    private fun notifyBalanceUpdated() {
        textBalance.text = Utils.currencyString(mViewModel.balance)
        if (mViewModel.balance < 0) {
            textBalance.setTextColor(Color.RED)
        } else {
            textBalance.setTextColor(Color.BLACK)
        }

        checkMenuNext()
    }

    fun notifyPrefixUpdated() {
        if (mViewModel.cellProduct.product == CellProduct.PRODUCT_UNKNOWN) {
            cardProduct.visibility = View.INVISIBLE
            segmentedButtonGroup.visibility = View.INVISIBLE
            textBalance.visibility = View.INVISIBLE
            text_label1.visibility = View.INVISIBLE
            mViewModel.balance = mViewModel.initBalance
            menuNext?.isEnabled = false
        } else {
            cardProduct.visibility = View.VISIBLE
            segmentedButtonGroup.visibility = View.VISIBLE
            textBalance.visibility = View.VISIBLE
            text_label1.visibility = View.VISIBLE
            imageIcon.setImageResource(mViewModel.cellProduct.getIconResId()!!)
        }


        textHint.setTextColor(Color.GRAY)
        segmentedButtonGroup.position = 0
        textHint.text = resources.getText(R.string.hint_pulsa)
        mViewModel.balance = mViewModel.initBalance
        notifyBalanceUpdated()
    }


    class CellProduct(var prefix : String) {
        private var mProduct : Int = PRODUCT_UNKNOWN
        val product : Int get() = mProduct

        companion object {
            const val PRODUCT_UNKNOWN = -1
            const val PRODUCT_SMARTFREN = 0
            const val PRODUCT_ISAT_IM3 = 1
            const val PRODUCT_ISAT_MENTARI = 2
            const val PRODUCT_TSEL_SIMPATI = 3
            const val PRODUCT_TSEL_AS = 4
            const val PRODUCT_TRI = 5
            const val PRODUCT_XL = 6
            const val PRODUCT_AXIS = 7

            fun parsePrefix(msisdn : String) : String {
                if (msisdn.length < 4) return ""
                if (msisdn.substring(0,1).compareTo("0") == 0) {
                    return msisdn.substring(0,4)
                } else if (msisdn.substring(0,2).compareTo("62") == 0) {
                    if (msisdn.length < 5) return ""
                    return "0" + msisdn.substring(2,5)
                }
                return ""
            }
        }

        constructor() : this("")

        init {
            mProduct = when (prefix) {
                "0812", "0813", "0821", "0822" -> PRODUCT_TSEL_SIMPATI
                "0823", "0851", "0852", "0853" -> PRODUCT_TSEL_AS
                "0881", "0882", "0888", "0889" -> PRODUCT_SMARTFREN
                "0856", "0857" -> PRODUCT_ISAT_IM3
                "0815", "0816", "0858" -> PRODUCT_ISAT_MENTARI
                "0817", "0818", "0819", "0859", "0877", "0878" -> PRODUCT_XL
                "0838", "0831", "0832", "0833" -> PRODUCT_AXIS
                "0895", "0896", "0897", "0898", "0899" -> PRODUCT_TRI
                else -> PRODUCT_UNKNOWN
            }
        }

        fun getIconResId() : Int? {
            return when (mProduct) {
                PRODUCT_SMARTFREN -> R.drawable.ic_cell_smartfren
                PRODUCT_ISAT_IM3 -> R.drawable.ic_cell_im3
                PRODUCT_ISAT_MENTARI -> R.drawable.ic_cell_mentari
                PRODUCT_TSEL_SIMPATI -> R.drawable.ic_cell_simpati
                PRODUCT_TSEL_AS -> R.drawable.ic_cell_as
                PRODUCT_TRI -> R.drawable.ic_cell_tri
                PRODUCT_XL -> R.drawable.ic_cell_xl
                PRODUCT_AXIS -> R.drawable.ic_cell_axis
                else -> android.R.drawable.ic_menu_help
            }
        }
        val topUpAmount : Array<String>
            get() =  arrayOf("100ribu", "50ribu", "25ribu", "10ribu")

        fun getProductCode(isTopUp : Boolean, position : Int) : String {
            if (isTopUp) {
                return when (position) {
                    0 -> "100K"
                    1 -> "50K"
                    2 -> "25K"
                    3 -> "10K"
                    else -> ""
                }
            } else {
                return when (mProduct) {
                    PRODUCT_SMARTFREN -> {
                        when (position) {
                            0 -> "SPLAN30GB"
                            1 -> "SPLAN20GB"
                            2 -> "SPLAN10GB"
                            3 -> "SPLAN8GB"
                            4 -> "SPLAN6GB"
                            5 -> "SPLAN3GB"
                            6 -> "UL15"
                            7 -> "UL10"
                            8 -> "UL512"
                            else -> ""
                        }
                    }
                    PRODUCT_TSEL_AS, PRODUCT_TSEL_SIMPATI -> {
                        when (position) {
                            0 -> "DATA200K"
                            1 -> "DATA150K"
                            2 -> "DATA100K"
                            3 -> "DATA50K"
                            else -> ""
                        }
                    }
                    PRODUCT_ISAT_MENTARI, PRODUCT_ISAT_IM3 -> {
                        when (position) {
                            0 -> "UL10GB"
                            1 -> "UL20GB"
                            2 -> "FREE12GB"
                            3 -> "FREE41GB"
                            else -> ""
                        }
                    }
                    PRODUCT_AXIS, PRODUCT_XL -> {
                        when (position) {
                            0 -> "HR008"
                            1 -> "HR040"
                            2 -> "HR060"
                            else -> ""
                        }
                    }
                    PRODUCT_TRI -> {
                        when (position) {
                            0 -> "MIX10"
                            1 -> "MIX30"
                            else -> ""
                        }
                    }
                    else -> ""
                }
            }

            return ""
        }

        val dataPlans : Array<String>
        get() =
            when (mProduct) {
                PRODUCT_SMARTFREN ->
                        arrayOf("Smartplan 30GB - 200rb","Smartplan 20GB - 150rb","Smartplan 10GB - 100rb",
                            "Smartplan 8GB - 75rb", "Smartplan 6GB - 60rb", "Smartplan 3GB - 50rb",
                            "Unlimited 1,5GB Max Speed Bulanan - 100rb", "Unlimited 1GB Max Speed Bulanan - 60rb",
                            "Unlimited Bulanan 512KB - 50rb")
                PRODUCT_TSEL_AS, PRODUCT_TSEL_SIMPATI ->
                        arrayOf("200rb - 7GB s/d 9GB", "150rb - 4.5GB s/d 6.6GB", "100rb - 2.5GB s/d 4.5GB",
                                "50rb - 800MB s/d 1.5GB")
                PRODUCT_ISAT_MENTARI, PRODUCT_ISAT_IM3 ->
                        arrayOf("Unlimited 1GB - 20rb", "Unlimited 2GB - 33rb", "Freedom Combo M 12GB - 50rb", "Freedom Combo XL - 100rb")
                PRODUCT_XL, PRODUCT_AXIS ->
                        arrayOf("HotRod 800MB - 30rb", "HotRod 3GB - 60rb", "HotRod 6GB - 100rb")
                PRODUCT_TRI ->
                        arrayOf("Mix 10GB - 50rb", "Mix 30GB - 100rb")
                else -> arrayOf("")
            }


        fun getProductValue(isTopUp : Boolean, position : Int) : Int {
            if (isTopUp) {
                return when (position) {
                    0 -> 100000
                    1 -> 50000
                    2 -> 25000
                    3 -> 10000
                    else -> 0
                }
            } else {
                return when (mProduct) {
                    PRODUCT_SMARTFREN -> {
                        when (position) {
                            0 -> 200000
                            1 -> 150000
                            2 -> 100000
                            3 -> 75000
                            4 -> 60000
                            5 -> 50000
                            6 -> 100000
                            7 -> 60000
                            8 -> 50000
                            else -> 0
                        }
                    }
                    PRODUCT_TSEL_SIMPATI, PRODUCT_TSEL_AS -> {
                        when (position) {
                            0 -> 200000
                            1 -> 150000
                            2 -> 100000
                            3 -> 50000
                            else -> 0
                        }
                    }
                    PRODUCT_ISAT_IM3, PRODUCT_ISAT_MENTARI -> {
                        when (position) {
                            0 -> 20000
                            1 -> 33000
                            2 -> 50000
                            3 -> 100000
                            else -> 0
                        }
                    }
                    PRODUCT_XL, PRODUCT_AXIS -> {
                        when (position) {
                            0 -> 30000
                            1 -> 60000
                            2 -> 100000
                            else -> 0
                        }
                    }
                    PRODUCT_TRI -> {
                        when (position) {
                            0 -> 50000
                            1 -> 100000
                            else -> 0
                        }
                    }
                    else -> 0
                }
            }

            return 0
        }
    }

    class AppViewModel(application: Application) : AndroidViewModel(application) {
        var cellProduct : CellProduct = CellProduct()
        var initBalance : Int = 0
        var balance : Int = 0
        var phoneNumber : String = ""
        var isTopUp = true
        var indexProduct : Int = -1
        val mRepository : TrxRepository = TrxRepository(application)
    }
}
