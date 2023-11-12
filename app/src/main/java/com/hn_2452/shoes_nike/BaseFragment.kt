package com.hn_2452.shoes_nike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    var _mBinding: T? = null
    protected val mBinding get() = _mBinding

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mBinding = getViewBinding(inflater, container)
        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }

}