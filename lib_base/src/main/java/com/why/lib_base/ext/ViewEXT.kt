package com.why.lib_base.ext

import androidx.appcompat.widget.Toolbar

/**
 *
 * @author why
 * @date 2022/11/14 10:09
 */
fun hideLoading() {

}

/**
 * 初始化普通的toolbar 只设置标题
 *
 * @param titleStr 标题
 */
fun Toolbar.initTitle(titleStr: String = "") {
    title = titleStr
}