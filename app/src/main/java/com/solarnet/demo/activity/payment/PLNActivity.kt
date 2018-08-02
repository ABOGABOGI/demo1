package com.solarnet.demo.activity.payment

import android.app.AlertDialog
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.TextView
import com.solarnet.demo.MyApp
import com.solarnet.demo.R
import com.solarnet.demo.data.trx.TrxRepository
import com.solarnet.demo.data.util.DecimalDigitFilter
import com.solarnet.demo.data.util.Utils
import com.solarnet.demo.network.PostTrx

import kotlinx.android.synthetic.main.activity_pln.*
import kotlinx.android.synthetic.main.fragment_pln.*
import kotlinx.android.synthetic.main.fragment_pln1.*

class PLNActivity : BaseActivity() {
    var mViewModel : AppViewModel? = null
    val PLN_PRODUCT_STRING: Array<String>
        get() = arrayOf("20 ribu", "50 ribu", "100 ribu", "200 ribu",
                "500 ribu", "1 juta", "2 juta")
    val PLN_PRODUCT: Array<Int>
        get() = arrayOf(20000, 50000, 100000, 200000, 500000, 1000000, 2000000)

    class AppViewModel(application : Application) : AndroidViewModel(application) {
        var idPln : String = ""
        var initBalance : Int = 0
        var balance : Int = 0
        var indexProduct : Int = -1

        val mRepository : TrxRepository = TrxRepository(application)

        fun resetBalance() {
            balance = initBalance
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pln)
        setSupportActionBar(toolbar)

        mViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        mViewModel?.initBalance = MyApp.instance.getBalance()
        mViewModel?.resetBalance()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment, InputFragment())
        fragmentTransaction.commit()
    }

    override fun back() {
        if (supportFragmentManager.findFragmentById(R.id.fragment) is InputFragment) {
            super.onBackPressed()
        } else {
            when (supportFragmentManager.findFragmentById(R.id.fragment)) {
                is SelectFragment -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment, InputFragment())
                        commit()
                    }
                }
            }
        }
    }

    override fun getTrxRepository() : TrxRepository {
        return mViewModel!!.mRepository
    }

    override fun next() {
        when (supportFragmentManager.findFragmentById(R.id.fragment)) {
            is InputFragment -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment, SelectFragment())
                    commit()
                }
                menuNext?.isEnabled = true
            }
            is SelectFragment -> {
                showProgress(true)
                mPostTrx.postPln(textMeterNo.text.toString(),
                        PLN_PRODUCT[mViewModel!!.indexProduct])
            }
        }
    }


    class SelectFragment : Fragment() {
        private val plnActivity : PLNActivity get() = (activity as PLNActivity)
        private lateinit var textBalance : TextView
        private lateinit var textProduct : TextView

        private fun notifyProductUpdated() {
            val cost = plnActivity.PLN_PRODUCT[plnActivity.mViewModel!!.indexProduct]
            plnActivity.mViewModel!!.balance = plnActivity.mViewModel!!.initBalance - cost
            textBalance.text = Utils.currencyString(plnActivity.mViewModel!!.balance)
            plnActivity.menuNext?.isEnabled =
                    plnActivity.mViewModel!!.balance >= 0
            if (plnActivity.mViewModel!!.balance < 0) {
                textBalance.setTextColor(Color.RED)
            } else {
                textBalance.setTextColor(Color.BLACK)
            }
            textProduct.text = plnActivity.PLN_PRODUCT_STRING[
                    plnActivity.mViewModel!!.indexProduct]
            textProduct.setTextColor(Color.BLACK)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_pln1, container, false)
            plnActivity.menuNext?.isEnabled = false
            val textMeterNo : TextView = view.findViewById(R.id.textMeterNo)
            textMeterNo.text = plnActivity.mViewModel!!.idPln

            textBalance = view.findViewById(R.id.textBalance)
            textBalance.text = Utils.currencyString(plnActivity.mViewModel!!.balance)

            textProduct = view.findViewById(R.id.textHint)

            val cardProduct : CardView = view.findViewById(R.id.cardProduct)
            cardProduct.setOnClickListener { _ ->
                showAlertList(getString(R.string.hint_pulsa), plnActivity.PLN_PRODUCT_STRING)
            }

            return view
        }

        fun showAlertList(title : String, items : Array<String>) {
            var builder : AlertDialog.Builder = AlertDialog.Builder(activity).apply {
                setTitle(title)
                setItems(items, DialogInterface.OnClickListener{
                    dlg, item ->
                    plnActivity.mViewModel!!.indexProduct = item
                    notifyProductUpdated()
                    dlg.dismiss()
                })
            }

            var alert = builder.create()
            alert.show()
        }

    }

    class InputFragment : Fragment() {
        val plnActivity : PLNActivity get() = (activity as PLNActivity)

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_pln, container, false)
            var editIdPln : EditText = view.findViewById(R.id.editIdPln)
            editIdPln.setText(plnActivity.mViewModel!!.idPln)
            editIdPln.filters = arrayOf(DecimalDigitFilter(), InputFilter.LengthFilter(11))
            editIdPln.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (plnActivity.mViewModel != null) {
                        if (s != null) {
                            plnActivity.mViewModel!!.idPln = s.toString()
                        } else {
                            plnActivity.mViewModel!!.idPln = ""
                        }
                        plnActivity.menuNext?.isEnabled =
                                plnActivity.mViewModel!!.idPln!!.length >= 11
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            })
            return view
        }
    }

}
