package com.basic.logcat.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

abstract class EarthFragment<VB : ViewBinding, VM : ViewModel>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val viewModelClass: KClass<VM>?=null,
) : Fragment() {

    protected val viewModel: VM? by lazy {
        viewModelClass?.let {
            ViewModelProvider(this)[it.java] // Use it.java to get the Java Class
        }
    }

    protected lateinit var binding: VB


    abstract fun initFragment(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, container, false)
        initFragment(inflater,container,savedInstanceState)
        return binding.root
    }

}