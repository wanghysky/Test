package com.why.lib_base.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.why.lib_base.R
import com.why.lib_base.kotterknife.bindView

/**
 *
 * @author why
 * @date 2022/11/14 18:15
 */
class LoadingView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

//    private val loading by bindView<LinearLayout>(R.id.ll_loading)
//    private val loadFail by bindView<TextView>(R.id.tv_load_fail)
//    private val loadNoMore by bindView<TextView>(R.id.tv_no_more)
//
//    init {
//        inflate(context, R.layout.live_recycler_widget_load_more, this)
//    }
//
//    fun showLoading(){
//        loading.visibility = View.VISIBLE
//        loadFail.visibility = View.GONE
//        loadNoMore.visibility = View.GONE
//    }
//
//    fun hideLoading(){
//        loading.visibility = View.GONE
//    }

}