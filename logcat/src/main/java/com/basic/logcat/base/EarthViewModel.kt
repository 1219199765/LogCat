package com.basic.logcat.base

import androidx.lifecycle.ViewModel

open class EarthViewModel : ViewModel() {

    /**
     * 统一处理错误
     * @param e 异常
     * @param showErrorToast 是否显示错误吐司
     */
//    @SuppressLint("WrongConstant")
//    private fun onError(e: Exception, showErrorToast: Boolean) {
//        when (e) {
//            is ApiException -> {
//
//            }
//            // 网络请求失败
//            is ConnectException, is SocketTimeoutException, is UnknownHostException, is HttpException -> {
//                if (showErrorToast) Toast.makeText(AppHelper.mContext, "网络请求失败", 1000).show()
//                Log.e(TAG, "网络请求失败" + e.toString())
//
//
//            }
//            // 数据解析错误
//            is JsonParseException -> {
//                Log.e(TAG, "数据解析错误" + e.toString())
//
//            }
//            // 其他错误
//            else -> {
//                Log.e(TAG, "其他错误" + e.toString())
//            }
//        }
//    }
}