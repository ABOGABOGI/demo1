package com.solarnet.demo.activity.payment

import android.app.Dialog
import android.content.DialogInterface
import android.icu.text.UnicodeSetSpanner
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.solarnet.demo.R
import com.solarnet.demo.adapter.TopUpListAdapter
import com.solarnet.demo.data.contact.Contact
import com.solarnet.demo.view.BorderFrameLayout

import kotlinx.android.synthetic.main.activity_top_up.*
import kotlinx.android.synthetic.main.fragment_bill.*
import com.solarnet.demo.R.mipmap.ic_launcher
import android.view.Gravity
import android.widget.EditText
import com.solarnet.demo.data.util.Utils
import java.text.SimpleDateFormat
import java.util.*


class TopUpActivity : AppCompatActivity(), TopUpListAdapter.OnClickListener {
    val BANK_ICON = arrayOf(
            R.drawable.ic_bank_bca,
            R.drawable.ic_bank_mandiri,
            R.drawable.ic_bank_bni
    )

    val BANK_NAME = arrayOf(
            "Bank Central Asia",
            "Bank Mandiri",
            "BNI"
    )

    val BANK_TOPUP_TYPE = arrayOf(
            TopUpListAdapter.TYPE_MANUAL_TRANSFER,
            TopUpListAdapter.TYPE_VIRTUAL_ACCOUNT,
            TopUpListAdapter.TYPE_VIRTUAL_ACCOUNT
    )

    val BANK_ACCOUNT = arrayOf(
            R.string.hint_bank_acc_bca,
            R.string.hint_bank_acc_dummy,
            R.string.hint_bank_acc_dummy
    )

    val HINT_ATM = arrayListOf<Int>(
            -1,
            R.string.hint_atm1_mandiri,
            R.string.hint_atm1_bni
    )
    val HINT_MOBILE = arrayListOf<Int>(
            -1,
            R.string.hint_atm1_mandiri,
            R.string.hint_atm1_bni
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)
        setSupportActionBar(toolbar)

        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment, BankListFragment())
            commit()
        }
    }

    private fun showManualAmountDialog(position : Int) {
        val dialog = Dialog(this, R.style.Dialog)
        dialog.setContentView(R.layout.dialog_input_amount)
        val window = dialog.window
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)

        dialog.setTitle(getString(R.string.manual_transfer))
        val editAmount : EditText = dialog.findViewById(R.id.editAmount)
        dialog.findViewById<Button>(R.id.buttonCancel).setOnClickListener { _ ->
            dialog.dismiss()
        }

        val buttonOk : Button = dialog.findViewById(R.id.buttonOk)
        // if button is clicked, close the custom dialog
        buttonOk.setOnClickListener {_ ->
            startManualFragment(position, editAmount.text.toString().toIntOrNull())
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun calculateExpired() : String {
        var c = Calendar.getInstance()

        c.add(Calendar.DATE, 3);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        val sdf1 = SimpleDateFormat("dd-MM-yyyy")
        val output = sdf1.format(c.time)
        return "$output 23:59 WIB"
    }

    private fun startManualFragment(position : Int, amount : Int?) {
        var amt = 0
        if (amount != null) amt = amount
        val unique  = Random().nextInt(100)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, ManualFragment.newInstance(
                BANK_ICON[position],
                BANK_NAME[position],
                arrayListOf<Int>(BANK_ACCOUNT[position]), amt, unique,
                    calculateExpired()))
                commit()
            }
    }
    override fun onClick(position : Int) {
        when (BANK_TOPUP_TYPE[position]) {
            TopUpListAdapter.TYPE_VIRTUAL_ACCOUNT -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment, VirtualFragment.newInstance(
                            BANK_ICON[position],
                            BANK_NAME[position],
                            arrayListOf<Int>(HINT_ATM[position], HINT_MOBILE[position])))
                    commit()
                }
            }
            TopUpListAdapter.TYPE_MANUAL_TRANSFER -> {
//                supportFragmentManager.beginTransaction().apply {
//                    replace(R.id.fragment, ManualFragment.newInstance(
//                            BANK_ICON[position],
//                            BANK_NAME[position],
//                            arrayListOf<Int>(BANK_ACCOUNT[position])))
//                    commit()
                showManualAmountDialog(position)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    class BankListFragment : Fragment() {
        private val parent : TopUpActivity get() = (activity as TopUpActivity)
        private lateinit var recyclerView : RecyclerView
        private lateinit var mAdapter : TopUpListAdapter
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_topup, container, false)
            recyclerView = view.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)
            val items = ArrayList<TopUpListAdapter.Item>()
            for (i in 0 until parent.BANK_ICON.size) {
                items.add(TopUpListAdapter.Item(parent.BANK_ICON[i], parent.BANK_NAME[i],
                        parent.BANK_TOPUP_TYPE[i]))
            }
            mAdapter = TopUpListAdapter(context!!, items)
            mAdapter.listener = parent
            recyclerView.adapter = mAdapter

            return view
        }
    }

    class VirtualFragment : Fragment() {
        companion object {
            const val BANK : String = "bank"
            const val ICON : String = "icon"
            const val HINTS : String = "hints" //0 for ATM, 1 for Mobile

            fun newInstance(iconId : Int, bank : String,
                            hintId : ArrayList<Int>) : VirtualFragment {
                val f = VirtualFragment()
                val args = Bundle().apply {
                    putInt(ICON, iconId)
                    putString(BANK, bank)
                    putIntegerArrayList(HINTS, hintId)
                }
                f.arguments = args
                return f
            }
        }

        private lateinit var atmContent : BorderFrameLayout
        private lateinit var imageAtm : ImageView
        private lateinit var mobileContent : BorderFrameLayout
        private lateinit var imageMobile : ImageView

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_topup1, container, false)
            val textTitle : TextView = view.findViewById(R.id.textTitle)
            val bank =  arguments?.getString(BANK)
            textTitle.text = bank + " " +
                    context!!.resources.getString(R.string.virtual_account)
            view.findViewById<ImageView>(R.id.imageIcon).setImageResource(
                    arguments?.getInt(ICON, R.drawable.ic_bank)!!)
            view.findViewById<TextView>(R.id.textAtm).text = "ATM $bank"
            view.findViewById<TextView>(R.id.textMobile).text = "$bank Mobile Banking"

            val hints = arguments?.getIntegerArrayList(HINTS)
            view.findViewById<TextView>(R.id.textAtmContent).text = Html.fromHtml(
                    context!!.resources.getString(hints!![0])
            )
            view.findViewById<TextView>(R.id.textMobileContent).text = Html.fromHtml(
                    context!!.resources.getString(hints!![1])
            )

            atmContent = view.findViewById(R.id.layoutAtmContent)
            imageAtm = view.findViewById(R.id.imageAtm)
            view.findViewById<CardView>(R.id.cardAtm).setOnClickListener {
                if (atmContent.visibility != View.VISIBLE) {
                    imageAtm.setImageResource(R.drawable.ic_expand_less)
                    atmContent.visibility = View.VISIBLE
                } else {
                    imageAtm.setImageResource(R.drawable.ic_expand_more)
                    atmContent.visibility = View.GONE
                }
            }

            mobileContent = view.findViewById(R.id.layoutMobileContent)
            imageMobile = view.findViewById(R.id.imageMobile)
            view.findViewById<CardView>(R.id.cardMobile).setOnClickListener {
                if (mobileContent.visibility != View.VISIBLE) {
                    imageMobile.setImageResource(R.drawable.ic_expand_less)
                    mobileContent.visibility = View.VISIBLE
                } else {
                    imageMobile.setImageResource(R.drawable.ic_expand_more)
                    mobileContent.visibility = View.GONE
                }
            }
            return view
        }
    }

    class ManualFragment : Fragment() {
        private var amount : Int = 0
        private var unique : Int = 0
        companion object {
            const val BANK : String = "bank"
            const val ICON : String = "icon"
            const val HINTS : String = "hints" //0: account number
            const val AMOUNT : String = "amount"
            const val UNIQUE : String = "unique"
            const val EXPIRED : String = "exp"

            fun newInstance(iconId : Int, bank : String,
                            hintId : ArrayList<Int>,
                            amount : Int, unique : Int,
                            expired : String) : ManualFragment {
                val f = ManualFragment()
                val args = Bundle().apply {
                    putInt(ICON, iconId)
                    putString(BANK, bank)
                    putIntegerArrayList(HINTS, hintId)
                    putInt(AMOUNT, amount)
                    putInt(UNIQUE, unique)
                    putString(EXPIRED, expired)
                }
                f.arguments = args
                return f
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_topup2, container, false)
            val textTitle : TextView = view.findViewById(R.id.textTitle)
            val bank =  arguments?.getString(VirtualFragment.BANK)
            amount = arguments?.getInt(AMOUNT, 0)!!
            unique = arguments?.getInt(UNIQUE, 0)!!

            textTitle.text = context!!.resources.getString(R.string.manual_transfer) + " " +
                    bank
            view.findViewById<ImageView>(R.id.imageIcon).setImageResource(
                    arguments?.getInt(VirtualFragment.ICON, R.drawable.ic_bank)!!)
            var manual1 = context!!.getString(R.string.hint_manual1)
            manual1 = manual1.replace("%var1%", Utils.currencyString(unique))
            view.findViewById<TextView>(R.id.textHint1).text = Html.fromHtml(
                    manual1)
            view.findViewById<TextView>(R.id.textHint2).text = Html.fromHtml(
                    context!!.getString(R.string.hint_manual2))
            view.findViewById<TextView>(R.id.textExpired).text =
                    context!!.resources.getString(R.string.before) +
                    " : " + arguments?.getString(EXPIRED)
            view.findViewById<TextView>(R.id.textAmount).text = Utils.currencyString(amount + unique)
            return view
        }
    }
}
