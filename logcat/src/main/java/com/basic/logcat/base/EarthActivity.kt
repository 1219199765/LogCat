package com.basic.logcat.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass



abstract class EarthActivity<VB : ViewBinding, VM : ViewModel>(
    private val inflate: (LayoutInflater) -> VB,
    private val viewModelClass: KClass<VM>? = null, // Use KClass for better Kotlin interop
) : AppCompatActivity() {

    protected val viewModel: VM? by lazy {
        viewModelClass?.let {
            ViewModelProvider(this)[it.java] // Use it.java to get the Java Class
        }
    }

//    protected val binding: VB by viewBinding(inflate)
    protected lateinit var binding: VB

    abstract fun initActivity()

    abstract fun initData()

    open fun immersiveExit(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (immersiveExit()) {
            enableEdgeToEdge()
        }
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        initActivity()
        initData()
    }
}