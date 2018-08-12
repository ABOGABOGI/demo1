package com.solarnet.demo.activity.payment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.solarnet.demo.R

import kotlinx.android.synthetic.main.activity_invoice.*
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.solarnet.demo.adapter.IconListAdapter
import com.solarnet.demo.data.trx.TrxRepository
import com.solarnet.demo.data.util.DecimalDigitFilter
import com.solarnet.demo.data.util.Utils
import kotlinx.android.synthetic.main.fragment_invoice.*
import java.util.*


class InvoiceActivity : BaseActivity() {
    var BANKS : Array<IconListAdapter.Item> =
            arrayOf(
                    IconListAdapter.Item(R.drawable.ic_bank_bca, "Bank Central Asia", "BBCA"),
                    IconListAdapter.Item(R.drawable.ic_bank_mandiri, "Bank Mandiri", "BMRI"),
                    IconListAdapter.Item(R.drawable.ic_bank_bni, "BNI", "BBNI")
            )
    lateinit var mBankAdapter : IconListAdapter<IconListAdapter.Item>
    lateinit var mViewModel : AppViewModel

    class AppViewModel(application : Application) : AndroidViewModel(application) {
        var indexBank : Int = 0
        var amount : Int = 0
        var accNumber : String = ""
        var message : String = ""
        var mTrxRepository = TrxRepository(application)
    }

    override fun getTrxRepository(): TrxRepository? {
        return mViewModel.mTrxRepository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.invoice)
        mViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        mBankAdapter = IconListAdapter(this, BANKS)
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment, InputFragment())
            commit()
        }

    }

    override fun next() {
        showProgress(true)
        mPostTrx.postInvoice(Utils.fromCurrencyString(editAmount.text.toString())!!,
                textBank.text.toString(), editAccNumber.text.toString(),
                editMessage.text.toString())
    }

    class InputFragment : Fragment() {
        val invoiceActivity : InvoiceActivity get() = (activity as InvoiceActivity)!!

        private lateinit var textProduct : TextView
        private lateinit var imageIcon : ImageView

        fun validateNext() {
            if (invoiceActivity.menuNext != null) {
                invoiceActivity.menuNext!!.isEnabled = invoiceActivity.mViewModel.accNumber.isNotEmpty() &&
                        invoiceActivity.mViewModel.amount > 0
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_invoice, container, false)
            textProduct = view.findViewById(R.id.textBank)
            imageIcon = view.findViewById(R.id.imageIcon)

            val cardBank : CardView = view.findViewById(R.id.cardBank)
            cardBank.setOnClickListener { _ ->
                showBankList()
            }
            view.findViewById<EditText>(R.id.editMessage).apply {
                setText(invoiceActivity.mViewModel.message)
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        invoiceActivity.mViewModel.message = s.toString()
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                })
            }

            val editAccNumber : EditText = view.findViewById(R.id.editAccNumber)
            editAccNumber.filters = arrayOf(DecimalDigitFilter())
            editAccNumber.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    invoiceActivity.mViewModel.accNumber = s.toString()
                    validateNext()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            }
            )

            val editAmount : EditText = view.findViewById(R.id.editAmount)
            editAmount.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    var amount : Int? = Utils.fromCurrencyString(editAmount.text.toString())
                    if (amount == null) amount = 0
                    if (invoiceActivity.mViewModel.amount != amount) { //prevent endless loop
                        invoiceActivity.mViewModel.amount = amount
                        val str = Utils.currencyString(invoiceActivity.mViewModel.amount)
                        editAmount.setText(str)
                        editAmount.setSelection(str.length)
                        validateNext()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            })

            notifyProductUpdated()
            return view
        }


        private fun showBankList() {
            var builder = AlertDialog.Builder(activity!!)
            builder.setTitle(resources.getString(R.string.select_bank))
            builder.setAdapter(invoiceActivity.mBankAdapter,
                DialogInterface.OnClickListener {
                    dialog, item ->

                    invoiceActivity.mViewModel.indexBank = item
                    notifyProductUpdated()
                })
            builder.show()
        }


        private fun notifyProductUpdated() {
            val index = invoiceActivity.mViewModel.indexBank
            var item = invoiceActivity.BANKS[index]
            textProduct.text = item.text
            imageIcon.setImageResource(item.iconRes)
        }
    }
}
