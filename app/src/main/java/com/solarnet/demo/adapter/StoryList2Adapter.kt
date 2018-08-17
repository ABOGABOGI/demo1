package com.solarnet.demo.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.solarnet.demo.R
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager
import kotlinx.android.synthetic.main.item_story.view.*
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.solarnet.demo.data.story.Story
import com.volokh.danylo.video_player_manager.ui.MediaPlayerWrapper
import android.widget.LinearLayout
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import com.solarnet.demo.data.story.People

class StoryList2Adapter(
        val mContext : Context,
        val mVideoPlayerManager: VideoPlayerManager<*>,
        var mData : List<Story> = ArrayList<Story>()
) : RecyclerView.Adapter<StoryListAdapter.ViewHolder1>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryListAdapter.ViewHolder1 {
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.item_story, parent, false)
                // set the view's size, margins, paddings and layout parameters
        return StoryListAdapter.ViewHolder1(v)

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(h: StoryListAdapter.ViewHolder1, position: Int) {
//        if (position > 0) {
            val data = mData[position]
            if (data.quote != null) {
                h.quote.text = data.quote
            } else {
                h.quote.text = ""
            }

            h.peopleImage.visibility = View.GONE

            if (data.mediaType == Story.MEDIA_TYPE_NONE) {
                h.thumbnail.visibility = View.GONE
            } else {
                if (data.thumbnail != null) {
                    Glide.with(mContext).load(data.thumbnail).apply(RequestOptions().apply {
                        placeholder(R.drawable.image_not_found)
                        error(R.drawable.image_not_found)
                    })
                            .listener(StoryListAdapter.ThumbnailListener(data.thumbnail!!))
                            .into(h.thumbnail)
                } else {
                    // make sure Glide doesn't load anything into this view until told otherwise
                    Glide.with(mContext).clear(h.thumbnail)
                    // remove the placeholder (optional); read comments below
                    h.thumbnail.setImageResource(R.drawable.thumb2)
                }
            }

            when (data.mediaType) {
                Story.MEDIA_TYPE_VIDEO -> h.play.visibility = View.VISIBLE
                else -> h.play.visibility = View.INVISIBLE
            }
            if (data.media != null) {
                h.videoView.addMediaPlayerListener(
                        StoryListAdapter.MediaListener(mVideoPlayerManager,
                                h.videoView,
                                h.play, h.progressBar))
                h.play.tag = h
                h.play.setOnClickListener { v ->
                    val vh: StoryListAdapter.ViewHolder1 = v.tag as StoryListAdapter.ViewHolder1
                    if (data.mediaType == Story.MEDIA_TYPE_VIDEO) {
                        vh.play.visibility = View.INVISIBLE
                        vh.progressBar.visibility = View.VISIBLE
                        vh.progressBar.isIndeterminate = true
                        vh.progressBar.progress = 0
                        mVideoPlayerManager.stopAnyPlayback()
                        mVideoPlayerManager.playNewVideo(null, vh.videoView,
                                data.media)
                    }
                }
                h.videoView.setOnClickListener {
                    mVideoPlayerManager.stopAnyPlayback()
                }
            }
//        }
    }

}