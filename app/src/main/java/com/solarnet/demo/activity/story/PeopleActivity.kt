package com.solarnet.demo.activity.story

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.solarnet.demo.R
import com.solarnet.demo.adapter.StoryList2Adapter
import com.solarnet.demo.data.story.Story
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager
import kotlinx.android.synthetic.main.activity_people.*
import kotlinx.android.synthetic.main.content_people.*
import java.util.*

class PeopleActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USERNAME = "user"
        const val EXTRA_NAME = "title"
        const val EXTRA_PICTURE = "picture"
    }

    private val mVideoPlayerManager = SingleVideoPlayerManager(PlayerItemChangeListener { })
    private var mMenu : Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)
        setSupportActionBar(toolbar)

        val picture = intent.getStringExtra(EXTRA_PICTURE)
        val name = intent.getStringExtra(EXTRA_NAME)
        toolbar_layout.title = name
        Glide.with(this).load(picture).into(peopleImage)


        app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange;
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    showOption(R.id.action_follow)
                } else if (isShow) {
                    isShow = false
                    hideOption(R.id.action_follow)
                }
            }

        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StoryList2Adapter(this, mVideoPlayerManager,
                getStories())
        fab.setOnClickListener { view ->
            follow()
        }
    }

    private fun follow() {
        Toast.makeText(this, "You have followed this user!", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.action_follow -> follow()
        }

        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu = menu
        menuInflater.inflate(R.menu.menu_people, menu)
        hideOption(R.id.action_follow)
        return true
    }

    private fun hideOption(id: Int) {
        if (mMenu != null) {
            val item = mMenu!!.findItem(id)
            item.isVisible = false
        }
    }

    private fun showOption(id: Int) {
        if (mMenu != null) {
            val item = mMenu!!.findItem(id)
            item.isVisible = true
        }
    }

    private fun getStories() : List<Story> {
        val list = ArrayList<Story>()
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR, -1)
        list.add(Story(null, "Good morning friends ... ", Story.MEDIA_TYPE_VIDEO,
                "https://www.sample-videos.com/video/mp4/360/big_buck_bunny_360p_1mb.mp4",
                "http://demo.sistemonline.biz.id/public/stories/images/thumb2.jpg",
                cal.time))
        cal.add(Calendar.DATE, -7)
        list.add(Story(null, "", Story.MEDIA_TYPE_PICTURE,
                null,
                "http://demo.sistemonline.biz.id/public/stories/images/landscape1.jpg",
                cal.time))

        return list
    }
}
