package com.basic.logcat

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.basic.logcat.base.EarthActivity
import com.basic.logcat.base.EarthViewModel
import com.basic.logcat.databinding.ActivityMainBinding
import com.basic.logcat.log.LogCat
import com.basic.logcat.log.LogCatHttpLoggingInterceptor
import com.basic.logcat.utils.getNavigationBarHeight
import com.basic.logcat.utils.isNetworkConnected
import com.basic.logcat.utils.saveToPhoto
import com.drake.net.Get
import com.drake.net.NetConfig
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MainActivity : EarthActivity<ActivityMainBinding,EarthViewModel>(
    ActivityMainBinding::inflate,
    EarthViewModel::class
) {
    override fun initActivity() {
        NetConfig.initialize("https://www.wanandroid.com",this) {
            connectTimeout(300, TimeUnit.SECONDS)
            readTimeout(300, TimeUnit.SECONDS)
            writeTimeout(300, TimeUnit.SECONDS)
            setDebug(true)
            setConverter(MoshiConverter())

            addNetworkInterceptor(LogCatHttpLoggingInterceptor().apply {
                setLevel(LogCatHttpLoggingInterceptor.Level.BODY)
            })
        }
    }

    override fun initData() {
        binding.btn1.setOnClickListener {
            request()
        }

        binding.btn2.setOnClickListener {
        }

        binding.btn3.setOnClickListener {
        }
    }





    private fun request(){
        lifecycleScope.launch {
         val data =    Get<HomeArticleBean.Data>("/article/list/1/json").await()

        }
    }

}

val jsons = "啥也不是"

