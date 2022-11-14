package com.why.test.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 *
 * @author why
 * @date 2022/11/14 18:13
 */
class MainRecyclerViewAtViewPager2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var startX = 0f
    private var startY = 0f

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val mv = ev ?: return super.dispatchTouchEvent(ev)
        when(mv.action){
            MotionEvent.ACTION_DOWN -> {
                startX = mv.x
                startY = mv.y
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = mv.x
                val endY = mv.y
                val disX = abs(endX - startX)
                val disY = abs(endY - startY)
                if (disX > disY) {
                    //如果是纵向滑动，告知父布局不进行时间拦截，交由子布局消费，　requestDisallowInterceptTouchEvent(true)
                    parent.requestDisallowInterceptTouchEvent(canScrollHorizontally((startX - endX).toInt()));
                } else {
                    parent.requestDisallowInterceptTouchEvent(canScrollVertically((startX - endX).toInt()));
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false);
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}