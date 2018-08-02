package com.solarnet.demo.activity.payment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.solarnet.demo.MyApp
import com.solarnet.demo.R
import com.solarnet.demo.adapter.IconListAdapter
import com.solarnet.demo.data.trx.TrxRepository
import com.solarnet.demo.data.util.DecimalDigitFilter
import com.solarnet.demo.data.util.Utils

import kotlinx.android.synthetic.main.activity_withdraw.*
import kotlinx.android.synthetic.main.fragment_withdraw.*

class WithdrawActivity : BaseActivity() {
    var BANKS : Array<IconListAdapter.Item> =
            arrayOf(
                    IconListAdapter.Item(R.drawable.ic_bank_bca, "Bank Central Asia", "BBCA"),
                    IconListAdapter.Item(R.drawable.ic_bank_mandiri, "Bank Mandiri", "BMRI"),
                    IconListAdapter.Item(R.drawable.ic_bank_bca, "Bank Negara Indonesia", "BBNI")
            )
    lateinit var mBankAdapter : IconListAdapter<IconListAdapter.Item>
    lateinit var mViewModel : AppViewModel

    class AppViewModel(application: Application) : AndroidViewModel(application) {
        var indexBank : Int = 0
        var amount : Int = 0
        var accNumber : String = ""
        var message : String = ""
        val initBalance : Int = MyApp.instance.getBalance()
        var balance : Int = 0
        val mRepository : TrxRepository = TrxRepository(application)
        fun resetBalance() {
            balance = initBalance
        }
    }

    override fun getTrxRepository(): TrxRepository? {
        return mViewModel?.mRepository
    }

    override fun next() {
        showProgress(true)
        val banks = BANKS[mViewModel.indexBank]
        mPostTrx.postWithdraw(banks.text, banks.code, editAccNumber.text.toString(),
                mViewModel.amount)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw)
        setSupportActionBar(toolbar)
        mViewModel = ViewModelProviders.of(this).get(WithdrawActivity.AppViewModel::class.java)
        mViewModel.resetBalance()
        mBankAdapter = IconListAdapter(this, BANKS)

        supportFragmentManager?.beginTransaction()!!.apply {
            add(R.id.fragment, InputFragment())
            commit()
        }

    }


    class InputFragment : Fragment() {
        val withdrawActivity : WithdrawActivity get() = (activity as WithdrawActivity)!!

        private lateinit var textBalance : TextView
        private lateinit var textProduct : TextView
        private lateinit var imageIcon : ImageView

        fun updateBalance() {
            withdrawActivity.mViewModel.balance = withdrawActivity.mViewModel.initBalance -
                    withdrawActivity.mViewModel.amount
            textBalance.text = Utils.currencyString(withdrawActivity.mViewModel.balance)
            if (withdrawActivity.mViewModel.balance < 0) {
                textBalance.setTextColor(Color.RED)
            } else {
                textBalance.setTextColor(Color.BLACK)
            }
        }

        fun validateNext() {
            //check balance first
            updateBalance()
            if (withdrawActivity.menuNext != null) {
                withdrawActivity.menuNext!!.isEnabled = withdrawActivity.mViewModel.accNumber.isNotEmpty() &&
                        withdrawActivity.mViewModel.amount > 0 &&
                        withdrawActivity.mViewModel.balance >= 0
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_withdraw, container, false)
            textProduct = view.findViewById(R.id.textBank)
            imageIcon = view.findViewById(R.id.imageIcon)
            textBalance = view.findViewById(R.id.textBalance)
            val cardBank : CardView = view.findViewById(R.id.cardBank)
            cardBank.setOnClickListener { _ ->
                showBankList()
            }

            val editAccNumber : EditText = view.findViewById(R.id.editAccNumber)
            editAccNumber.filters = arrayOf(DecimalDigitFilter())
            editAccNumber.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    withdrawActivity.mViewModel.accNumber = s.toString()
                    validateNext()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            })

            val editAmount : EditText = view.findViewById(R.id.editAmount)
            editAmount.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    var amount : Int? = Utils.fromCurrencyString(editAmount.text.toString())
                    if (amount == null) amount = 0
                    if (withdrawActivity.mViewModel.amount != amount) { //prevent endless loop
                        withdrawActivity.mViewModel.amount = amount
                        val str = Utils.currencyString(withdrawActivity.mViewModel.amount)
                        editAmount.setText(str)
                        editAmount.setSelection(str.length)
                        validateNext()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            })
            updateBalance()
            notifyProductUpdated()
            return view
        }


        private fun showBankList() {
            var builder = AlertDialog.Builder(activity!!)
            builder.setTitle(resources.getString(R.string.select_bank))
            builder.setAdapter(withdrawActivity.mBankAdapter,
                    DialogInterface.OnClickListener {
                        dialog, item ->

                        withdrawActivity.mViewModel.indexBank = item
                        notifyProductUpdated()
                    })
            builder.show()
        }


        private fun notifyProductUpdated() {
            val index = withdrawActivity.mViewModel.indexBank
            var item = withdrawActivity.BANKS[index]
            textProduct.text = item.text
            imageIcon.setImageResource(item.iconRes)
        }
    }

}
