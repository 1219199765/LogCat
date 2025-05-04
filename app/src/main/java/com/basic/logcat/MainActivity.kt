package com.basic.logcat

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.drake.net.Get
import com.drake.net.NetConfig
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var btn1:Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn1 = findViewById(R.id.btn_1)


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

        btn1.setOnClickListener {
//            Logger.d(jsons)
//            Logger.d("-------------")
//            Logger.json(jsons)
            request()
        }
    }


    private fun request(){
        lifecycleScope.launch {
         val data =    Get<HomeArticleBean.Data>("/article/list/1/json").await()

        }
    }

}

val jsons = "啥也不是"

