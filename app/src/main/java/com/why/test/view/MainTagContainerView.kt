package com.why.test.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import com.bumptech.glide.util.Util
import com.why.lib_base.util.Utils
import com.why.test.R

/**
 *
 * @author why
 * @date 2022/11/14 22:54
 */
@SuppressLint("NewApi")
class TagContainerView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    ViewGroup(context, attrs, defStyleAttr) {

    // 垂直方向间距
    private var mVerticalMargin: Int = 0

    // 水平方向间距
    private var mHorizontalMargin: Int = 0

    // tag高度
    private var mTagHeight: Int = 0

    // tag水平padding
    private var mTagHorizontalPadding: Int = 0

    // tag垂直padding
    private var mTagVerticalPadding: Int = 0

    // tag文字颜色
    private var mTagTextColor: Int = Color.RED

    // tag选中的文字颜色
    private var mTagTextColorSelected: Int = Color.WHITE

    // tag字体大小
    private var mTagTextSize: Int = 14

    // 组件宽度
    private var mWidth: Int = 0

    // 组件高度
    private var mHeight: Int = 0

    // tag背景Drawable
    private var mBgTagDrawableResId: Int = R.drawable.bg_tag

    // tag选中背景Drawable
    private var mBgTagSelectedDrawableResId: Int = R.drawable.bg_tag_selected

    // 该画笔用来辅助测量tag中的文字
    private var mTagTextPaint: Paint

    // Tag被点击的监听器
    private var mOnTagClickListener: OnTagClickListener? = null

    // Tag被长按的监听器
    private var mOnTagLongClickListener: OnTagLongClickListener? = null

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null)

    init {
        val a = context!!.obtainStyledAttributes(attrs, R.styleable.TagContainerView)

        mVerticalMargin = a.getDimensionPixelSize(
            R.styleable.TagContainerView_tag_vertical_margin,
            Utils.dp2px(context, 1)
        )

        mHorizontalMargin = a.getDimensionPixelSize(
            R.styleable.TagContainerView_tag_horizontal_margin,
            Utils.dp2px(context, 2)
        )

        val tagTextSizePx = a.getDimensionPixelSize(
            R.styleable.TagContainerView_tag_text_size,
            Utils.sp2px(context, 14)
        )
        mTagTextSize = Utils.px2sp(context, tagTextSizePx)

        mTagVerticalPadding = a.getDimensionPixelSize(
            R.styleable.TagContainerView_tag_padding_vertical,
            Utils.dp2px(context, 1)
        )

        mTagHorizontalPadding = a.getDimensionPixelSize(
            R.styleable.TagContainerView_tag_padding_horizontal,
            Utils.dp2px(context, 2)
        )

        mTagTextColor = a.getColor(
            R.styleable.TagContainerView_tag_text_color,
            context.resources.getColor(R.color.red, null)
        )

        mTagTextColorSelected = a.getColor(
            R.styleable.TagContainerView_tag_text_color_selected,
            context.resources.getColor(R.color.white, null)
        )

        mBgTagDrawableResId =
            a.getResourceId(R.styleable.TagContainerView_tag_bg_drawable, R.drawable.bg_tag)

        mBgTagSelectedDrawableResId = a.getResourceId(
            R.styleable.TagContainerView_tag_bg_drawable_selected,
            R.drawable.bg_tag_selected
        )

        // 这边需要加上一个文字的偏移否则展示有问题
        mTagTextPaint = Paint()
        mTagTextPaint.textSize = mTagTextSize.toFloat()
        val textOffset = mTagTextPaint.fontMetrics.bottom - mTagTextPaint.fontMetrics.top

        mTagHeight = (textOffset + tagTextSizePx + mTagVerticalPadding * 2).toInt()

        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var lines = 1
        if (childCount == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        // 处理宽
        mWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                width
            }
            MeasureSpec.AT_MOST -> {
                // 此时让横向占满屏幕
                context.resources.displayMetrics.widthPixels
            }
            else -> {
                width
            }
        }

        // 处理高
        when (heightMode) {
            MeasureSpec.EXACTLY -> {
                mHeight = height
            }
            MeasureSpec.AT_MOST -> {
                var tempWidth = 0
                var index = 0
                while (index < childCount) {
                    val child = getChildAt(index)
                    val childParams = child.layoutParams as MarginLayoutParams

                    if (tempWidth <= mWidth - paddingLeft - paddingRight) {//当前行摆得下
                        tempWidth += childParams.width + childParams.leftMargin + childParams.rightMargin
                        index++
                    } else {// 当前行摆不下了
                        index--
                        lines++
                        tempWidth = 0
                    }
                }
                mHeight = paddingTop + paddingBottom + lines * (mTagHeight + 2 * mVerticalMargin)
            }
            else -> {
                mHeight = height
            }
        }

        setMeasuredDimension(mWidth, mHeight)

        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            val childParams = child.layoutParams as MarginLayoutParams

            var left: Int
            var top: Int
            var right: Int
            var bottom: Int

            if (index == 0) {// 第一个tag
                left = paddingLeft + childParams.leftMargin
                top = paddingTop + childParams.topMargin
                right = left + childParams.width
                bottom = top + childParams.height
            } else {// 非第一个tag
                val preChild = getChildAt(index - 1)
                val preChildParams = preChild.layoutParams as MarginLayoutParams

                if (preChild.right + preChildParams.rightMargin + childParams.leftMargin + childParams.width + childParams.rightMargin
                    <= mWidth - paddingRight
                ) {// 不需要换行
                    left = preChild.right + preChildParams.rightMargin + childParams.leftMargin
                    top = preChild.top
                    right = left + childParams.width
                    bottom = top + childParams.height
                } else {// 需要换行
                    left = paddingLeft + childParams.leftMargin
                    top = preChild.bottom + preChildParams.bottomMargin + childParams.topMargin
                    right = left + childParams.width
                    bottom = top + childParams.height
                }
            }

            child.layout(left, top, right, bottom)
        }
    }

    override fun onDragEvent(event: DragEvent?): Boolean {
        return super.onDragEvent(event)
    }

    /**
     * 设置tags
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setTags(@NonNull tagList: List<String>) {
        removeAllViews()

        for (index in tagList.indices) {
            val s = tagList[index]
            if (TextUtils.isEmpty(s)) {
                continue
            }

            // 方式1：使用LayoutInflater添加tag
            val view = LayoutInflater.from(context).inflate(R.layout.main_tag, this, false)
            val txtTag = view.findViewById<TextView>(R.id.txt_tag)
            txtTag.tag = false
            txtTag.text = s
            txtTag.setTextColor(mTagTextColor)
            txtTag.textSize = mTagTextSize.toFloat()
            txtTag.gravity = Gravity.CENTER
            txtTag.background = context.getDrawable(mBgTagDrawableResId)
            txtTag.setOnClickListener {
                onTagClick(index, txtTag, s)
            }
            txtTag.setOnLongClickListener {
                onTagLongClick(index, txtTag, s)
                true
            }
            txtTag.setPadding(
                mTagHorizontalPadding,
                mTagVerticalPadding,
                mTagHorizontalPadding,
                mTagVerticalPadding
            )

            val params = view.layoutParams as MarginLayoutParams
            params.width =
                2 * mTagHorizontalPadding + Utils.sp2px(
                    context,
                    mTagTextPaint.measureText(s).toInt()
                )
            params.height = mTagHeight

            params.leftMargin = mHorizontalMargin
            params.rightMargin = mHorizontalMargin
            params.topMargin = mHorizontalMargin
            params.bottomMargin = mHorizontalMargin
            view.layoutParams = params

            addView(view)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onTagLongClick(index: Int, @NonNull tag: TextView, @NonNull s: String) {
        if (mOnTagLongClickListener == null) {
            return
        }

        tag.tag = !(tag.tag as Boolean)
        if (tag.tag as Boolean) {
            tag.background = context.getDrawable(R.drawable.bg_tag_selected)
            tag.setTextColor(mTagTextColorSelected)
        } else {
            tag.background = context.getDrawable(R.drawable.bg_tag)
            tag.setTextColor(mTagTextColor)
        }

        mOnTagLongClickListener!!.onTagLongClicked(index, tag, s, tag.tag as Boolean)
    }

    /**
     * 对tag的点击
     */
    private fun onTagClick(index: Int, @NonNull tag: TextView, @NonNull s: String) {
        if (mOnTagClickListener == null) {
            return
        }

        mOnTagClickListener!!.onTagClicked(index, tag, s)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    /**
     * Tag点击的监听器
     */
    interface OnTagClickListener {
        /**
         * 点击了tag
         *
         * @param index 位置游标
         * @param tag 被点击的tag
         * @param tagStr tag内容
         */
        fun onTagClicked(index: Int, @NonNull tag: TextView, @NonNull tagStr: String)
    }

    /**
     * 设置Tag被点击的监听器
     *
     * @param listener 监听器
     */
    fun setOnTagClickListener(listener: OnTagClickListener) {
        this.mOnTagClickListener = listener
    }

    /**
     * Tag点击的监听器
     */
    interface OnTagLongClickListener {
        /**
         * 点击了tag
         *
         * @param index 位置游标
         * @param tag 被点击的tag
         * @param tagStr tag内容
         * @param isSelected 是否选中
         */
        fun onTagLongClicked(
            index: Int,
            @NonNull tag: TextView,
            @NonNull tagStr: String,
            isSelected: Boolean
        )
    }

    /**
     * 设置Tag被点击的监听器
     *
     * @param listener 监听器
     */
    fun setOnTagLongClickListener(listener: OnTagLongClickListener) {
        this.mOnTagLongClickListener = listener
    }
}