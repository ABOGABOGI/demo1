package com.solarnet.demo.activity.payment

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v7.widget.RecyclerView

import com.solarnet.demo.R
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.fragment_product.view.*
import com.solarnet.demo.R.id.recyclerView
import android.R.attr.data
import android.app.Dialog
import android.arch.lifecycle.AndroidViewModel
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import com.solarnet.demo.adapter.ProductListAdapter
import com.solarnet.demo.data.product.Product
import com.solarnet.demo.R.mipmap.ic_launcher
import android.widget.TextView
import android.widget.Toast
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.solarnet.demo.data.product.ProductRepository
import com.solarnet.demo.data.util.Utils
import org.json.JSONObject

class ProductActivity : AppCompatActivity() {
    class AppViewModel(application : Application) : AndroidViewModel(application) {
        private var mProductRepository : ProductRepository = ProductRepository(application)
        private var mAllProducts : LiveData<List<Product>>

        init {
            mAllProducts = mProductRepository.getAll()
        }

        fun getProducts() : LiveData<List<Product>> {
            return mAllProducts
        }

        fun countProducts() : Int {
            if (mAllProducts.value == null) return 0

            return mAllProducts.value!!.size
        }

        fun getRepository() : ProductRepository {
            return mProductRepository
        }

    }
    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    lateinit var mViewModel : AppViewModel
    val mListenerInsert : ProductRepository.OnInsertListener = object : ProductRepository.OnInsertListener {
        override fun insertCompleted(product: Product) {
            startProductQRActivity(product)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        setSupportActionBar(toolbar)

        mViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        mViewModel.getRepository().listenerInsert = mListenerInsert
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        tabLayout.setupWithViewPager(container)

        fab.setOnClickListener { view ->
            when (container.currentItem) {
                0 -> showAddDialog()
                1 -> startBillActivity()
            }
        }
    }

    private fun startBillActivity() {
        startActivity(Intent(this, BillActivity::class.java))
    }

    private fun showAddDialog() {
        val dialog = Dialog(this, R.style.Dialog)
        dialog.setContentView(R.layout.dialog_add_product)
        dialog.setTitle(getString(R.string.add_new_product))

        val editItemName = dialog.findViewById<EditText>(R.id.editItemName)
        val editItemPrice = dialog.findViewById<EditText>(R.id.editItemPrice)

        dialog.findViewById<Button>(R.id.buttonCancel).setOnClickListener{dialog.dismiss()}
        val buttonOk : Button = dialog.findViewById(R.id.buttonOk)
        // if button is clicked, close the custom dialog
        buttonOk.setOnClickListener {
            val name = editItemName.text.toString()
            val price = editItemPrice.text.toString().toIntOrNull()
            Log.i("Test", "name: $name , price: $price")
            if (name.length < 0 || price == null) {
                Toast.makeText(this@ProductActivity,
                        this@ProductActivity.getString(R.string.invalid_product),
                        Toast.LENGTH_SHORT).show()
            } else {
                createProduct(name, price)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    fun startProductQRActivity(product : Product) {
        startActivity(Intent(this, ProductQrActivity::class.java).apply {
            putExtra(ProductQrActivity.EXTRA_JSON, Utils.createProductJsonQR(product))
            putExtra(ProductQrActivity.EXTRA_NAME, product.description)
            putExtra(ProductQrActivity.EXTRA_PRICE, product.price)
            putExtra(ProductQrActivity.EXTRA_ID, product.id)
        })
    }

    private fun createProduct(name : String, price : Int) {
        val code = String.format("BEK%03X", (mViewModel.countProducts() + 1))

        val json = "{\"code\":\"$code\",\"desc\":\"$name\"}"
        Log.i("Test", "JSON : $json")
        val product = Product(code, name, price, System.currentTimeMillis())
        mViewModel.getRepository().insert(product)
        //startProductQRActivity(product)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_product, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.home) {
            super.onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private var titleResId = arrayOf(R.string.product_list,
                R.string.bill_list)
        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return when (position) {
                0 -> PlaceholderFragment.newInstance()
                else -> PlaceholderFragment2.newInstance()
            }
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return titleResId.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return getString(titleResId[position])
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment(), ProductListAdapter.OnClickListener {
        override fun onClick(product: Product) {
            parent.startProductQRActivity(product)
        }

        private val parent : ProductActivity get() = activity as ProductActivity
        private lateinit var recyclerView : RecyclerView
        private lateinit var mAdapter : ProductListAdapter

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_product, container, false)

            recyclerView = rootView.findViewById(R.id.recyclerView)

            val linearLayoutManager = LinearLayoutManager(activity!!.baseContext)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.setHasFixedSize(true)
            mAdapter = ProductListAdapter(parent)
            mAdapter.listener = this
            recyclerView.adapter = mAdapter

            parent.mViewModel.getProducts().observe(parent, Observer<List<Product>> {products ->
                if (products != null) {
                    mAdapter.setData(products)
                }
            })

            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(): PlaceholderFragment {
                return PlaceholderFragment()
            }
        }
    }

    class PlaceholderFragment2 : Fragment() {

        private val parent : ProductActivity get() = activity as ProductActivity
        private lateinit var recyclerView : RecyclerView
        private lateinit var mAdapter : ProductListAdapter

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_product, container, false)

            recyclerView = rootView.findViewById(R.id.recyclerView)

            val linearLayoutManager = LinearLayoutManager(activity!!.baseContext)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.setHasFixedSize(true)

            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(): PlaceholderFragment2 {
                return PlaceholderFragment2()
            }
        }
    }
}
