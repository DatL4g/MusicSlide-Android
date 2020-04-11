package de.datlag.musicslide

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import de.datlag.musicslide.adapter.LockPager
import de.datlag.musicslide.transformer.LockTransformer


class LockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)

        setLockAppearance()
        CommonUtil.enterFullScreen(window)
        CommonUtil.useNotchSpace(window)
        CommonUtil.hideSystemUI(window)
        CommonUtil.requestPortrait(this)
        pagerSetup()
    }

    private fun pagerSetup() {
        var scrollState = 0
        var pos = 1
        var offset = 0.5F

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = LockPager(this)
        viewPager.currentItem = pos
        viewPager.setPageTransformer(LockTransformer())
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                pos = position
                exit(pos, scrollState, offset)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                scrollState = state
                exit(pos, scrollState, offset)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                offset = positionOffset
                exit(pos, scrollState, offset)
            }

        })
    }

    private fun exit(pos: Int, scrollState: Int, offset: Float) {
        if ((pos == 0 && scrollState == 2) && (offset > 0.9F || offset == 0.0F)) {
            finishAffinity()
        }
    }

    private fun setLockAppearance() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
    }
}