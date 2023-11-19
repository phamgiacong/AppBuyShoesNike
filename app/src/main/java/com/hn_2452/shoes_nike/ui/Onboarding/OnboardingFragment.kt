package com.hn_2452.shoes_nike.ui.Onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.MainActivity
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentOnboardingBinding

class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    private val onboardingViewModel: OnboardingViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnboardingBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView()
        onClickListener()
        observerData()
    }

    private fun bindView() {
        (requireActivity() as MainActivity).hideBottomNav()
    }

    private fun onClickListener() {
        _mBinding?.btnGetStarted?.setOnClickListener {
            val check = _mBinding?.viewPager2?.currentItem
            _mBinding?.viewPager2?.currentItem = _mBinding?.viewPager2?.currentItem!! + 1
            if (check == 2) {
                _mBinding?.btnGetStarted?.setText(R.string.get_started)
            }
            if (check!! >= 2) {
                findNavController().navigate(R.id.action_onboardingFragment_to_registerFragment)
            }
        }

        _mBinding?.viewPager2?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position >= 2) {
                    _mBinding?.btnGetStarted?.setText(R.string.get_started)
                } else {
                    _mBinding?.btnGetStarted?.setText(R.string.next)
                }
            }
        })
    }

    private fun observerData() {
        onboardingViewModel.onboardingData.observe(viewLifecycleOwner) {
            _mBinding?.viewPager2?.adapter = OnboardingAdapter(it)
            _mBinding?.indicator?.setViewPager(_mBinding?.viewPager2)
        }
    }

}