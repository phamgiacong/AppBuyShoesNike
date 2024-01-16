package com.hn_2452.shoes_nike

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.android.material.appbar.MaterialToolbar
import com.hn_2452.shoes_nike.ui.home.shoes.ShoesFragment
import com.hn_2452.shoes_nike.utility.getStringDataByKey

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var mLoadingProgressBar: ProgressBar? = null


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomBar(false)
    }

    override fun onStop() {
        super.onStop()
        setupBottomBar(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
        mNavController = null
    }
    fun setupToolbar(toolbar: MaterialToolbar?) {
        toolbar?.setNavigationOnClickListener {
            mNavController?.popBackStack()
        }
    }

    fun setupBottomBar(show: Boolean) {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.setupBottomNavigation(show)
    }

    fun setupLoading(progressBar: ProgressBar?) {
        mLoadingProgressBar = progressBar
        val doubleBounce: Sprite = FadingCircle()
        mLoadingProgressBar?.indeterminateDrawable = doubleBounce
    }

    fun startLoading() {
        mLoadingProgressBar?.visibility = View.VISIBLE
    }

    fun stopLoading() {
        mLoadingProgressBar?.visibility = View.GONE
    }

    protected fun showLoginRequestPopup() {
        AlertDialog.Builder(requireContext())
            .setMessage("Bạn cần đăng nhập trước khi lưu giày vào yêu thích")
            .setPositiveButton(
                "Đăng nhập"
            ) { dialog, _ ->
                mNavController?.navigate(R.id.loginFragment)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.close)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    protected fun showLoginRequestLayout(mainLayout: ViewGroup?,  needLoginLayout: ViewGroup?) {
        if(getStringDataByKey(requireContext(), TOKEN).isEmpty()) {
            mainLayout?.visibility = View.GONE
            needLoginLayout?.visibility = View.VISIBLE
        } else {
            needLoginLayout?.visibility = View.GONE
            mainLayout?.visibility = View.VISIBLE
        }
    }

}