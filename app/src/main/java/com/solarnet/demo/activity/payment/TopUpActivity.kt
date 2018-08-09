package com.solarnet.demo.activity.payment

import android.app.Application
import android.app.Dialog
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProviders
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
import android.view.*
import android.widget.*
import com.solarnet.demo.R
import com.solarnet.demo.adapter.TopUpListAdapter
import com.solarnet.demo.data.contact.Contact
import com.solarnet.demo.view.BorderFrameLayout

import kotlinx.android.synthetic.main.activity_top_up.*
import kotlinx.android.synthetic.main.fragment_bill.*
import com.solarnet.demo.R.mipmap.ic_launcher
import com.solarnet.demo.data.trx.TrxRepository
import com.solarnet.demo.data.util.Utils
import com.solarnet.demo.network.PostTrx
import okhttp3.Call
import java.text.SimpleDateFormat
import java.util.*


class TopUpActivity : BaseActivity(), TopUpListAdapter.OnClickListener {
    class AppViewModel(application: Application) : AndroidViewModel(application) {
        var mSelectedPosition : Int = 0
        val mRepository : TrxRepository = TrxRepository(application)
    }

    lateinit var mViewModel : AppViewModel
    companion object {
        val BANK_ICON = arrayOf(
                R.drawable.ic_bank_bca,
                R.drawable.ic_bank_mandiri,
                R.drawable.ic_bank_bni
        )

        fun getBankIcon(bankName : String) : Int {
            var loop = 0
            BANK_NAME.forEach {name ->
                if (name.compareTo(bankName) == 0) {
                    return BANK_ICON[loop]
                }
                loop++
            }
            return 0
        }

        val BANK_NAME = arrayOf(
                "Bank Central Asia",
                "Bank Mandiri",
                "BNI"
        )

        val BANK_CODE = arrayOf(
                "BBCA",
                "BMRI",
                "BBNI"
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
    }

    override fun getTrxRepository(): TrxRepository? {
        return mViewModel.mRepository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)
        setSupportActionBar(toolbar)
        mViewModel = ViewModelProviders.of(this).get(TopUpActivity.AppViewModel::class.java)
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment, BankListFragment())
            commit()
        }
    }

    private fun showManualAmountDialog(position : Int) {
        mViewModel.mSelectedPosition = position
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
            val amount = editAmount.text.toString().toIntOrNull()
            if (amount != null) {
                dialog.dismiss()
                showProgress(true)
                mPostTrx.postManualTopUp1(amount, BANK_CODE[mViewModel.mSelectedPosition],
                        BANK_NAME[mViewModel.mSelectedPosition])
            } else {
                showToast(getString(R.string.invalid_amount))
            }
//            startManualFragment(position, editAmount.text.toString().toIntOrNull())
//            dialog.dismiss()
        }

        dialog.show()
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

    override fun getProgressBar(): ProgressBar? {
        return progressBar
    }

    override fun getOverlay(): View? {
        return overlay
    }

    override fun next() {
        //do nothing
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.send_payment, menu)
        menu.removeItem(R.id.action_next)
        menuNext = null
        return true
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
            for (i in 0 until TopUpActivity.BANK_ICON.size) {
                items.add(TopUpListAdapter.Item(TopUpActivity.BANK_ICON[i], TopUpActivity.BANK_NAME[i],
                        TopUpActivity.BANK_TOPUP_TYPE[i]))
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
            const val ACCOUNT : String = "acc" //0: account number
            const val AMOUNT : String = "amount"
            const val UNIQUE : String = "unique"
            const val EXPIRED : String = "exp"

            fun newInstance(iconId : Int, bank : String,
                            bankAccount : String,
                            amount : Int, unique : Int,
                            expired : String) : ManualFragment {
                val f = ManualFragment()
                val args = Bundle().apply {
                    putInt(ICON, iconId)
                    putString(BANK, bank)
                    putString(ACCOUNT, bankAccount)
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
            view.findViewById<TextView>(R.id.textAmount).text = Utils.currencyString(amount)
            view.findViewById<TextView>(R.id.textBankAccount).text = arguments?.getString(ACCOUNT)
            return view
        }
    }
}
