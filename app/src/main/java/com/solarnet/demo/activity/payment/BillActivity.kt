package com.solarnet.demo.activity.payment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.solarnet.demo.R
import com.solarnet.demo.adapter.BillProductListAdapter
import com.solarnet.demo.adapter.BillReviewListAdapter
import com.solarnet.demo.data.AppRoomDatabase
import com.solarnet.demo.data.bill.BillProduct
import com.solarnet.demo.data.product.Product
import com.solarnet.demo.data.product.ProductDao
import com.solarnet.demo.data.product.ProductRepository
import com.solarnet.demo.data.util.DecimalDigitFilter
import com.solarnet.demo.data.util.Utils

import kotlinx.android.synthetic.main.activity_bill.*

class BillActivity : BaseActivity() {
    lateinit var viewModel : AppViewModel
    class AppViewModel(application: Application) : AndroidViewModel(application) {
        private var mProducts : LiveData<List<Product>>
        var tax : Int = 0
        var subtotal : Int = 0
        var billProducts : List<BillProduct>? = null
        var qrCode : String? = null

        init {
            val db = AppRoomDatabase.getDatabase(application)
            var mProductDao = db!!.productDao()
            mProducts = mProductDao.getAll()
        }

        fun getProducts() : LiveData<List<Product>> {
            return mProducts
        }
    }

    override fun back() {
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment)
        when (fragment) {
            is ReviewFragment -> {
                supportFragmentManager.beginTransaction().apply{
                    replace(R.id.fragment, InputFragment())
                    commit()
                }
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun next() {
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment)
        when (fragment) {
            is InputFragment -> {
                viewModel.billProducts = (fragment).getBillProducts()
                supportFragmentManager.beginTransaction().apply{
                    replace(R.id.fragment, ReviewFragment())
                    commit()
                }
            }
            is ReviewFragment -> {
                if (viewModel.qrCode == null) {
                    val qrCode = "gp1234sd3"
                    fragment.mAdapter.setJsonQr(qrCode)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment, InputFragment())
            commit()
        }
    }

    class ReviewFragment : Fragment() {
        val parent: BillActivity get() = (activity as BillActivity)!!
        lateinit var mAdapter : BillReviewListAdapter
        private lateinit var recyclerView: RecyclerView
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_product, container, false)
            recyclerView = view.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(activity!!)
            mAdapter = BillReviewListAdapter(activity!!, parent.viewModel.billProducts!!,
                    parent.viewModel.subtotal, parent.viewModel.tax)
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
            recyclerView.adapter = mAdapter

            return view
        }
    }

    class InputFragment : Fragment() {
        val parent : BillActivity get() = (activity as BillActivity)!!
        private lateinit var mAdapter : BillProductListAdapter
        private lateinit var textSubtotal : TextView
        private lateinit var editTax : EditText
        private lateinit var textTax : TextView
        private lateinit var textTotal : TextView
        private lateinit var recyclerView: RecyclerView

        private fun calculateTotal(subtotal : Int) {
            parent.viewModel.subtotal = subtotal
            textSubtotal.text = Utils.currencyString(subtotal)
            var tax = 0
            if (parent.viewModel.tax > 0) {
                tax = subtotal * parent.viewModel.tax / 100
            }
            textTax.text = Utils.currencyString(tax)
            textTotal.text = Utils.currencyString(tax + subtotal)
            parent.menuNext?.isEnabled = subtotal > 0
        }

        fun getBillProducts() : List<BillProduct> {
            var res = ArrayList<BillProduct>()

            mAdapter.getData().forEach { product ->
                if (product.quantity > 0) {
                    res.add(product)
                }
            }

            return res
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_bill, container, false)
            textSubtotal = view.findViewById(R.id.textSubtotal)
            editTax = view.findViewById(R.id.editTax)
            editTax.setText("0")
            editTax.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    parent.viewModel.tax = 0
                    val newTax = s.toString().toIntOrNull()
                    if (newTax == null) {
                        parent.viewModel.tax = 0
                        editTax.setText("0")
                    } else {
                        parent.viewModel.tax = newTax
                    }
                    calculateTotal(parent.viewModel.subtotal)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            })
            textTax = view.findViewById(R.id.textTax)
            textTotal = view.findViewById(R.id.textTotal)

            recyclerView = view.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(activity!!)
            mAdapter = BillProductListAdapter(activity!!)

            recyclerView.adapter = mAdapter
            mAdapter.listener = object : BillProductListAdapter.OnSubtotalChangedListener {
                override fun onSubtotalChanged(subtotal: Int) {
                    calculateTotal(subtotal)
                }

            }

            parent.viewModel.getProducts().observe(parent, Observer<List<Product>> { products ->
                mAdapter.setData(products!!)
            })
            return view
        }
    }
}
