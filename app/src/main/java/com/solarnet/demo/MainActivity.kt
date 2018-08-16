package com.solarnet.demo

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.solarnet.demo.adapter.MainPagerAdapter
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.solarnet.demo.data.trx.TrxViewModel
import io.codetail.animation.ViewAnimationUtils
import android.widget.Toast
import com.solarnet.demo.activity.ProfileActivity


class MainActivity : AppCompatActivity() {
    private var mIsNavVisible = false
    private var offsetScrollUp : Int = -1 //offset for animating navigation bar up
    private var offsetScrollDown : Int = -1 //offset for animating navigation bar down

    interface OnScrollListener {
        fun onScrollChange(scrollY : Int, oldScrollY : Int)
    }
    private val mOnScrollListener = object : OnScrollListener {
        override fun onScrollChange(scrollY: Int, dy : Int) {
//            Log.i("Test", "onScrollChange {$scrollY} vs {${navigationLayout.height}")
            if (mIsNavVisible) {
                if (scrollY > offsetScrollUp && dy > 0) {
                    setNavigationLayout(false)
                }
            } else {
                if (scrollY < offsetScrollDown && dy < 0) {
                    setNavigationLayout(true)
                }
            }
        }

    }
    private lateinit var adapterViewPager: MainPagerAdapter

    private val onClickNavigationButton = View.OnClickListener { view ->
        when (view.id) {
            R.id.buttonPayment -> {
                setNavigationButton(true, false, false)
                viewPager.currentItem = 0
            }
            R.id.buttonChat -> {
                setNavigationButton(false, true, false)
                viewPager.currentItem = 1
            }
            R.id.buttonStory -> {
                setNavigationButton(false, false, true)
                viewPager.currentItem = 2
            }
        }
    }

    fun setNavigationLayout(visible : Boolean) {
        mIsNavVisible = visible
        if (visible) {
            var animation = ObjectAnimator.ofFloat(navigationLayout, "translationY", 0F);
            animation.duration = 100
            animation.start()
            animation.addUpdateListener { animation ->
                val y = animation?.animatedValue
                //                    Log.i("Test", "show: $y")
            }
        } else {

            var animation = ObjectAnimator.ofFloat(navigationLayout, "translationY", -357F);
            animation.duration = 100
            animation.start()
            animation.addUpdateListener { animation ->
                val y = animation?.animatedValue
                //                    Log.i("Test", "hide: $y")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.elevation = 0F

        offsetScrollUp = resources.getDimensionPixelOffset(R.dimen.nav_height) + 5
        offsetScrollDown = resources.getDimensionPixelOffset(R.dimen.nav_height) - 5
        fab.setOnFloatingActionsMenuUpdateListener(object :
                FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {
            override fun onMenuCollapsed() {
                showPopUp(fab, false)
            }

            override fun onMenuExpanded() {
                showPopUp(fab, true)
            }
        })

        setNavigationButton(true, false, false)

        buttonPayment.setOnClickListener(onClickNavigationButton)
        buttonChat.setOnClickListener(onClickNavigationButton)
        buttonStory.setOnClickListener(onClickNavigationButton)

        cardProfile.setOnClickListener({v-> startActivity(Intent(this@MainActivity, ProfileActivity::class.java))})

        adapterViewPager = MainPagerAdapter(supportFragmentManager, mOnScrollListener)
        viewPager.adapter = adapterViewPager
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                when (position) {
                    MainPagerAdapter.PAGE_PAYMENT ->  {
                        textPage.setText(R.string.title_payment)
                        setNavigationButton(true, false, false)
                    }
                    MainPagerAdapter.PAGE_CHAT -> {
                        textPage.setText(R.string.title_chat)
                        setNavigationButton(false, true, false)
                    }
                    MainPagerAdapter.PAGE_STORY -> {
                        textPage.setText(R.string.title_story)
                        setNavigationButton(false, false, true)
                    }
                    else -> {
                        textPage.text = ""
                        setNavigationButton(false, false, false)
                    }
                }
            }
        })
        viewPager.currentItem = 1

        popup.setOnClickListener{ _ ->
            showPopUp(fab, false)
            fab.collapse()
        }

    }

    private fun showPopUp(sourceView : View, show : Boolean) {
        // get the center for the clipping circle
        val cx = (sourceView.getLeft() + sourceView.getRight()) / 2
        val cy = (sourceView.getTop() + sourceView.getBottom()) / 2

        // get the final radius for the clipping circle
        val dx = Math.max(cx, sourceView.getWidth() - cx)
        val dy = Math.max(cy, sourceView.getHeight() - cy)
        val finalRadius = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()

        if (show) {
            popup.visibility = View.VISIBLE
            val animator = ViewAnimationUtils.createCircularReveal(popup, cx, cy, 0f, finalRadius)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = 200
            animator.start()
        } else {
            popup.visibility = View.VISIBLE
            val animator = ViewAnimationUtils.createCircularReveal(popup, cx, cy, finalRadius, 0f)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = 200
            animator.start()
            animator.addListener(object : Animation.AnimationListener, Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    popup.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                }

            })
        }
    }

    private fun setNavigationButton(enablePayment : Boolean,
                                    enableChat : Boolean,
                                    enableStory : Boolean) {
        if (enablePayment) {
            buttonPayment.setColorFilter(Color.WHITE)
        } else {
            buttonPayment.setColorFilter(resources.getColor(R.color.softPurple))
        }

        if (enableChat) {
            buttonChat.setColorFilter(Color.WHITE)
        } else {
            buttonChat.setColorFilter(resources.getColor(R.color.softPurple))
        }

        if (enableStory) {
            buttonStory.setColorFilter(Color.WHITE)
        } else {
            buttonStory.setColorFilter(resources.getColor(R.color.softPurple))
        }
    }

    private var back_pressed: Long = 0
    override fun onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(baseContext, "Press once again to exit", Toast.LENGTH_SHORT).show()
            back_pressed = System.currentTimeMillis()
        }
    }
}
