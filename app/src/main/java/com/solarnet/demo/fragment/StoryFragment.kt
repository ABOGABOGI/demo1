package com.solarnet.demo.fragment

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.solarnet.demo.MainActivity
import com.solarnet.demo.R
import com.solarnet.demo.adapter.StoryListAdapter
import com.solarnet.demo.data.story.People
import com.solarnet.demo.data.story.Story
import com.solarnet.demo.design.ItemOffsetDecoration
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView
import kotlinx.android.synthetic.main.fragment_story.*
import com.volokh.danylo.video_player_manager.meta.MetaData
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager
import com.volokh.danylo.video_player_manager.ui.MediaPlayerWrapper
import com.volokh.danylo.video_player_manager.ui.ScalableTextureView
import java.util.*


class StoryFragment : Fragment() {
    private var mOnScrollListener : MainActivity.OnScrollListener? = null
    private var mScrollY : Int = 0
    private val mVideoPlayerManager = SingleVideoPlayerManager(PlayerItemChangeListener { })

    // Store instance variables based on arguments passed
    // newInstance constructor for creating fragment with arguments
    companion object {
        fun newInstance(): StoryFragment {
            return StoryFragment()
        }
    }

    fun setOnScrollListener(onScrollListener: MainActivity.OnScrollListener) {
        mOnScrollListener = onScrollListener
    }

    // Inflate the view for the fragment based on layout XML
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_story, container, false)
//        val video_cover_1 = view.findViewById<ImageView>(R.id.video_cover_1)
//        val video_play_1 = view.findViewById<ImageView>(R.id.video_play_1)
//        val video_progress_1 = view.findViewById<ProgressBar>(R.id.video_progress_1)
//        video_player_1 = view.findViewById<VideoPlayerView>(R.id.video_player_1)
//        video_player_1.addMediaPlayerListener(MediaListener(video_player_1, video_play_1,
//                video_progress_1))
//        video_cover_1.setOnClickListener { v ->
//            video_play_1.visibility = View.INVISIBLE
//            video_progress_1.visibility = View.VISIBLE
//            video_progress_1.progress = 0
//            mVideoPlayerManager.playNewVideo(null, video_player_1,
//                    "https://www.sample-videos.com/video/mp4/360/big_buck_bunny_360p_1mb.mp4")
//        }
//
//        val video_cover_2 = view.findViewById<ImageView>(R.id.video_cover_2)
//        video_player_2 = view.findViewById<VideoPlayerView>(R.id.video_player_2)
//        video_player_2.addMediaPlayerListener(MediaListener(video_player_2, null, null))
//        video_cover_2.setOnClickListener { v ->
//            mVideoPlayerManager.playNewVideo(null, video_player_2,
//                    "http://mirrors.standaloneinstaller.com/video-sample/lion-sample.3gp")
//        }
        val list = ArrayList<Story>()
        val suneo = People("sun01", "Suneo", "http://demo.sistemonline.biz.id/public/people/images/suneo.jpg")
        val godzilla = People("god01", "Blue Godzilla", "http://demo.sistemonline.biz.id/public/people/images/godzilla.jpg")
        val minions = People("min01", "Yellow Minions", "http://demo.sistemonline.biz.id/public/people/images/minions.jpg")
        val cal = Calendar.getInstance();
        //cal.add(Calendar.DATE, -30);
        val dateBefore30Days = cal.time
        list.add(Story(suneo, "Listening to a story", Story.MEDIA_TYPE_PICTURE,
                "http://demo.sistemonline.biz.id/public/stories/images/lion-sample.png",
                "http://demo.sistemonline.biz.id/public/stories/images/lion-sample.png",
                cal.time))
        cal.add(Calendar.HOUR, -1)
        list.add(Story(godzilla, "Good morning friends ... ", Story.MEDIA_TYPE_VIDEO,
                "https://www.sample-videos.com/video/mp4/360/big_buck_bunny_360p_1mb.mp4",
                "http://demo.sistemonline.biz.id/public/stories/images/thumb2.jpg",
                cal.time))
        cal.add(Calendar.HOUR, -2)
        list.add(Story(minions, "Ba-ba-ba-ba-ba-nana\n" +
                "Ba-ba-ba-ba-ba-nana\n" +
                "banana-ah-ah (ba-ba-ba-ba-ba-nana)\n" +
                "potato-na-ah-ah (ba-ba-ba-ba-ba-nana)", cal.time)
                )
        cal.add(Calendar.DATE, -1)
        list.add(Story(suneo, "Listening to a story", Story.MEDIA_TYPE_VIDEO,
                "http://mirrors.standaloneinstaller.com/video-sample/lion-sample.3gp",
                "http://demo.sistemonline.biz.id/public/stories/images/lion-sample.png",
                cal.time))
        cal.add(Calendar.DATE, -7)
        list.add(Story(godzilla, "", Story.MEDIA_TYPE_PICTURE,
                null,
                "http://demo.sistemonline.biz.id/public/stories/images/landscape1.jpg",
                cal.time))

        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.addItemDecoration(ItemOffsetDecoration(context!!, R.dimen.nav_height))
        recyclerView.layoutManager = LinearLayoutManager(context)!!
        recyclerView.adapter = StoryListAdapter(context!!, mVideoPlayerManager, list)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mScrollY += dy
                mOnScrollListener?.onScrollChange(mScrollY, dy)
            }

        })
        return view
    }

//    class MediaListener(
//            val videoPlayerView : VideoPlayerView,
//            val imagePlay : ImageView?,
//            val progressPlay : ProgressBar?
//    ) : MediaPlayerWrapper.MainThreadMediaPlayerListener {
//        override fun onVideoSizeChangedMainThread(width: Int, height: Int) {
//
//        }
//
//        override fun onVideoPreparedMainThread() {
//            Log.i("Test", "Prepared")
//            videoPlayerView.visibility = View.VISIBLE
//            progressPlay?.visibility = View.INVISIBLE
//        }
//
//        override fun onVideoCompletionMainThread() {
//            Log.i("Test", "Completed")
//            videoPlayerView.visibility = View.INVISIBLE
//            imagePlay?.visibility = View.VISIBLE
//        }
//
//        override fun onErrorMainThread(what: Int, extra: Int) {
//
//        }
//
//        override fun onBufferingUpdateMainThread(percent: Int) {
//            Log.i("Test", "Buffering: $percent")
//            progressPlay?.progress = percent
//        }
//
//        override fun onVideoStoppedMainThread() {
//            videoPlayerView.visibility = View.INVISIBLE
//            imagePlay?.visibility = View.VISIBLE
//        }
//
//    }
}