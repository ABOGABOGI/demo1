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

class StoryListAdapter(
        val mContext : Context,
        val mVideoPlayerManager: VideoPlayerManager<*>,
        val mTopUsers : List<People>,
        var mData : List<Story> = ArrayList<Story>()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_HEADER = 0
        const val VIEW_CONTENT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_HEADER
            else -> VIEW_CONTENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(
                parent.context)
        return when (viewType) {
            VIEW_HEADER -> {
                val v = inflater.inflate(R.layout.item_topuser, parent, false)
                // set the view's size, margins, paddings and layout parameters
                ViewHolder2(v)
            }
            else -> {
                val v = inflater.inflate(R.layout.item_story, parent, false)
                // set the view's size, margins, paddings and layout parameters
                ViewHolder1(v)
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position > 0) {
            val h = holder as StoryListAdapter.ViewHolder1
            val data = mData[position - 1]
            if (data.quote != null) {
                h.quote.text = data.quote
            } else {
                h.quote.text = ""
            }

            h.submitted.text = PrettyTime(Locale.getDefault()).format(data.submitted)
            if (data.people != null) {
                h.peopleName.text = data.people!!.name
                if (data.people!!.picture != null) {
                    Glide.with(mContext).load(data.people!!.picture).apply(RequestOptions().apply {
                        placeholder(R.drawable.profile_default)
                        error(R.drawable.profile_default)
                    }).into(h.peopleImage)
                } else {
                    // make sure Glide doesn't load anything into this view until told otherwise
                    Glide.with(mContext).clear(holder.thumbnail)
                    // remove the placeholder (optional); read comments below
                    h.thumbnail.setImageResource(R.drawable.profile_default)
                }
            }
            if (data.mediaType == Story.MEDIA_TYPE_NONE) {
                h.thumbnail.visibility = View.GONE
            } else {
                if (data.thumbnail != null) {
                    Glide.with(mContext).load(data.thumbnail).apply(RequestOptions().apply {
                        placeholder(R.drawable.image_not_found)
                        error(R.drawable.image_not_found)
                    })
                            .listener(ThumbnailListener(data.thumbnail!!))
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
                        MediaListener(mVideoPlayerManager,
                                holder.videoView,
                                holder.play, holder.progressBar))
                h.play.tag = holder
                h.play.setOnClickListener { v ->
                    val vh: ViewHolder1 = v.tag as ViewHolder1
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
        } else {
            //header top user
            val h = holder as ViewHolder2
            h.recyclerView.layoutManager = LinearLayoutManager(mContext,
                    LinearLayoutManager.HORIZONTAL, false)
            h.recyclerView.adapter = TopUserAdapter(mContext, mTopUsers)
        }
    }

    class ViewHolder1(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail : ImageView = itemView.findViewById(R.id.imageThumbnail)
        val play : ImageView = itemView.findViewById(R.id.imagePlay)
        val progressBar  : ProgressBar = itemView.findViewById(R.id.progressBar)
        val videoView : VideoPlayerView = itemView.findViewById(R.id.videoPlayer)
        val quote : TextView = itemView.findViewById(R.id.textQuote)
        val peopleName : TextView = itemView.findViewById(R.id.peopleName)
        val peopleImage : ImageView = itemView.findViewById(R.id.peopleImage)
        val submitted : TextView = itemView.findViewById(R.id.textSubmitted)
    }

    inner class ViewHolder2(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView : RecyclerView = itemView.findViewById(R.id.recyclerView)
    }

    class ThumbnailListener(
            val url : String
    ) : RequestListener<Drawable> {
        init {
            Log.i("Test", "Listener $url")
        }
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            Log.i("Test", "Glide failed $url")
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            Log.i("Test", "Glide success $url")
            return false
        }

    }

    class MediaListener(
            val videoPlayerManager: VideoPlayerManager<*>,
            val videoPlayerView : VideoPlayerView,
            val imagePlay : ImageView?,
            val progressPlay : ProgressBar?
    ) : MediaPlayerWrapper.MainThreadMediaPlayerListener {
        override fun onVideoSizeChangedMainThread(width: Int, height: Int) {

        }

        override fun onVideoPreparedMainThread() {
            Log.i("Test", "Prepared")
            videoPlayerView.visibility = View.VISIBLE
            progressPlay?.visibility = View.INVISIBLE
        }

        override fun onVideoCompletionMainThread() {
            Log.i("Test", "Completed")
            videoPlayerView.visibility = View.INVISIBLE
            imagePlay?.visibility = View.VISIBLE
        }

        override fun onErrorMainThread(what: Int, extra: Int) {
            //Toast.makeText(mContext, "Failed streaming!", Toast.LENGTH_SHORT).show()

            videoPlayerView.visibility = View.INVISIBLE
            imagePlay?.visibility = View.VISIBLE
            progressPlay?.visibility = View.INVISIBLE
        }

        override fun onBufferingUpdateMainThread(percent: Int) {
            Log.i("Test", "Buffering: $percent")
            if (progressPlay?.isIndeterminate == true) {
                progressPlay?.isIndeterminate = false
            }
            progressPlay?.progress = percent
        }

        override fun onVideoStoppedMainThread() {
            Log.i("Test", "Stopped")
            videoPlayerView.visibility = View.INVISIBLE
            imagePlay?.visibility = View.VISIBLE
            progressPlay?.visibility = View.INVISIBLE
            videoPlayerManager.stopAnyPlayback()
        }

    }
}