package com.why.test.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.tabs.TabLayout

/**
 *
 * @author why
 * @date 2022/11/14 16:56
 */
class MinSpacingTabLayout@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    init {
        initMinWidth()
    }

    private fun initMinWidth() {
        try {
            val field = TabLayout::class.java.getDeclaredField("scrollableTabMinWidth")
            field.isAccessible = true
            // 设定最小的间距
            field.set(this, 10)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}