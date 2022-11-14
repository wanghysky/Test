package com.why.lib_base.util

import android.R
import android.graphics.Bitmap
import android.net.Uri
import android.util.Size
import androidx.core.content.ContextCompat
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ImageDecodeOptions
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.common.RotationOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.why.lib_base.base.BaseApp
import com.why.lib_base.network.IpManager
import kotlin.math.max


/**
 *
 * @author why
 * @date 2022/11/14 19:55
 */
object ImageLoader {

    private var baseUrl = IpManager.getDefaultIP()

    /**
     * 加载基本图片
     *
     * @param url
     * @param simpleDraweeView
     */
    fun loadImageView(url: String?,
                      simpleDraweeView: SimpleDraweeView,
                      widthPixels: Int = 0,
                      heightPixels:Int = 0,
                      roundingParams: RoundingParams?) {
        val builder = GenericDraweeHierarchyBuilder(BaseApp.appContext.resources)
        val imageDecodeOptionsBuilder = ImageDecodeOptions.newBuilder()
        imageDecodeOptionsBuilder.bitmapConfig = Bitmap.Config.RGB_565
        val uri = Uri.parse(baseUrl + url)

        val imageRequestBuilder = ImageRequestBuilder
            .newBuilderWithSource(uri)
            .setRotationOptions(RotationOptions.autoRotate())
            .setImageDecodeOptions(imageDecodeOptionsBuilder.build())

        if(widthPixels > 0 && heightPixels > 0) {
            val resizeOptions = ResizeOptions(
                widthPixels, heightPixels,
                max(widthPixels.coerceAtLeast(heightPixels), 1024).toFloat()
            )
            imageRequestBuilder.resizeOptions = resizeOptions
        }


        val hierarchy: GenericDraweeHierarchy = builder
            .setFadeDuration(300)
            .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
            .setPlaceholderImage(R.color.darker_gray)
            .setPlaceholderImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
            .setFailureImage(R.color.darker_gray)
            .setFailureImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
            .setRetryImage(R.color.darker_gray)
            .setRetryImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
            .build()

        roundingParams?.run {
            hierarchy.roundingParams = this
        }

        val controller: DraweeController = Fresco.newDraweeControllerBuilder()
            .setUri(uri)
            .setTapToRetryEnabled(true)
            .setOldController(simpleDraweeView.controller)
            .setImageRequest(imageRequestBuilder.build())
            .build()
        simpleDraweeView.hierarchy = hierarchy
        simpleDraweeView.controller = controller
    }

    fun loadRoundPic(
        url: String?,
        simpleDraweeView: SimpleDraweeView,
        topLeft: Float,
        topRight: Float,
        bottomLeft: Float,
        bottomRight: Float,
        width: Float,
        color: Int,
        widthPixels: Int = 0,
        heightPixels:Int = 0
    ) {
        val roundingParams = RoundingParams.fromCornersRadius(0f)
        if (width > 0) {
            roundingParams.setBorder(color, width) //描边线
        }
        roundingParams.setCornersRadii(topLeft, topRight, bottomRight, bottomLeft)
        loadImageView(url, simpleDraweeView,widthPixels, heightPixels, roundingParams)
    }
}