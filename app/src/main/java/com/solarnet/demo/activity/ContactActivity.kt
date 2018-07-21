package com.solarnet.demo.activity

import android.animation.LayoutTransition
import android.app.Activity
import android.arch.lifecycle.AndroidViewModel
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.solarnet.demo.R

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import kotlinx.android.synthetic.main.activity_contact.*
import android.text.TextUtils
import android.support.v4.view.MenuItemCompat.getActionView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.google.gson.Gson
import com.solarnet.demo.adapter.ContactListAdapter
import com.solarnet.demo.data.contact.Contact
import com.solarnet.demo.data.contact.ContactRepository
import com.solarnet.demo.data.trx.TrxRepository
import kotlinx.android.synthetic.main.content_contact.*
import java.util.ArrayList


class ContactActivity : AppCompatActivity(), ContactListAdapter.OnClickListener {
    override fun onClick(contact: Contact) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(EXTRA_CONTACT, Gson().toJson(contact))
        })
        finish()
    }

    private lateinit var mViewModel : AppViewModel
    private lateinit var mAdapter : ContactListAdapter
    companion object {
        const val EXTRA_STRING_TITLE = "TITLE"
        const val EXTRA_CONTACT = "CONTACT"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(EXTRA_STRING_TITLE)) {
            supportActionBar?.title = intent.getStringExtra(EXTRA_STRING_TITLE)
        }

        mAdapter = ContactListAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
        mAdapter.listener = this
        mViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        mViewModel.getContacts().observe(this, Observer { contacts ->
            if (contacts != null) {
                mAdapter.setData(contacts as ArrayList<Contact>)
            } else {
                mAdapter.removeAll()
            }
            mAdapter.notifyDataSetChanged()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.contact_list, menu)

        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView
        val searchBar : LinearLayout = searchView.findViewById(R.id.search_bar)
        searchBar.layoutTransition = LayoutTransition()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    mAdapter.filter("")
                } else {
                    mAdapter.filter(newText)
                }
                return true
            }
        })

        return true
    }

    class AppViewModel(application : Application) : AndroidViewModel(application) {
        private val mRepository = ContactRepository(application)
        private var mContacts : LiveData<List<Contact>>
        init {
            mContacts = mRepository.getContacts()
        }

        fun getContacts() : LiveData<List<Contact>> {
            return mContacts
        }
    }
}
