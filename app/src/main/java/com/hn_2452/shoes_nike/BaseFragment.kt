package com.hn_2452.shoes_nike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseFragment<T : ViewBinding> : Fragment() {


    protected var mNavController : NavController? = null

    private var _mBinding: T? = null
    protected val mBinding get() = _mBinding

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mNavController = requireActivity().findNavController(R.id.host_fragment)
        _mBinding = getViewBinding(inflater, container)
        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
        mNavController = null
    }
    fun setupToolbar(toolbar: MaterialToolbar?) {
        toolbar?.setNavigationOnClickListener {
            mNavController?.navigateUp()
        }
    }

    fun setupBottomBar(show: Boolean) {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.setupBottomNavigation(show)
    }

}