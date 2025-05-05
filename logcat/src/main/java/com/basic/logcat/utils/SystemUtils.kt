package com.basic.logcat.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 获取状态栏高度
 *
 * @param activity
 * @return 老版本
 */
fun getStatusBarHeight(activity: Activity?): Int {
    if (activity == null) return 0
    val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        activity.resources.getDimensionPixelSize(resourceId)
    } else 0
}


/**
 * 获取状态栏高度
 *
 * @param view == window.decorView
 * @return 新版本
 */
fun getStatusBarHeight(view: View): Int {
    val insets = ViewCompat.getRootWindowInsets(view)
    return insets?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0
}

/**
 * 设置内容到状态栏以及导航栏边距
 * systemBars.top 状态栏边距
 * systemBars.bottom 导航栏边距
 *
 * @param activity
 * @return
 */

fun systemBarPadding(rootView: View, top:Int=0, bottom:Int=0){
    ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
}

/**
 * 获取导航栏高度
 * 跟上边的 systemBars.bottom 值一样
 *
 * @return 老版本
 */
fun getNavigationBarHeight(context: Context): Int {
    val rid: Int =
        context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return if (rid != 0) {
        val resourceId: Int =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        context.resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

/**
 * 获取导航栏高度
 *
 * @return 新版本
 */

fun getNavigationBarHeight(view: View): Int {
    val insets = ViewCompat.getRootWindowInsets(view)
    return insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0
}



@Suppress("DEPRECATION")
fun Activity.makeStatusBarTransparent() {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        statusBarColor = android.graphics.Color.TRANSPARENT//android.graphics.Color.TRANSPARENT
        navigationBarColor = android.graphics.Color.WHITE
    }
}



/**
 * 设置状态栏字体颜色
 *
 * @param darkMode  false = 白色  true = 黑色
 *
 * @return
 */
fun Activity.darkMode(darkMode: Boolean = true){
    // 获取 WindowInsetsControllerCompat
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.isAppearanceLightStatusBars = darkMode
}
