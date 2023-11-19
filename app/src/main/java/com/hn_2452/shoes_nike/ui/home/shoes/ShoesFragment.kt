package com.hn_2452.shoes_nike.ui.home.shoes

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.databinding.FragmentShoesBinding
import com.hn_2452.shoes_nike.ui.home.shoes.shoes_image.ShoesImageAdapter
import com.hn_2452.shoes_nike.utility.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ShoesFragment : BaseFragment<FragmentShoesBinding>() {

    companion object {
        private const val TAG = "Nike:ShoesFragment: "
    }

    private val mShoesViewModel: ShoesViewModel by viewModels()
    private val mArgs: ShoesFragmentArgs by navArgs()

    @Inject
    lateinit var mSizeAdapter: SizeAdapter

    @Inject
    lateinit var mColorAdapter: ColorAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShoesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPlaceHolderLayout()
        setupLoading(mBinding?.loadingProgress)
        setupToolbar(mBinding?.toolBar)
        setupBottomBar(false)
        setupShoesData(mArgs.shoesId)
        setupCurrentShoes()
        setupAddToFavorite()
    }

    private fun startPlaceHolderLayout() {
        mBinding?.mainLayout?.visibility = View.INVISIBLE
        mBinding?.placeHolderLayout?.visibility = View.VISIBLE
        mBinding?.placeHolderLayout?.startShimmerAnimation()
    }

    private fun stopPlaceHolderLayout() {
        mBinding?.mainLayout?.visibility = View.VISIBLE
        mBinding?.placeHolderLayout?.visibility = View.GONE
        mBinding?.placeHolderLayout?.stopShimmerAnimation()
    }

    private fun startProcessBar() {
        mBinding?.loadingProgress?.visibility = View.VISIBLE
    }

    private fun stopProcessBar() {
        mBinding?.loadingProgress?.visibility = View.GONE
    }


    private fun setupLoading(progressBar: ProgressBar?) {
        val doubleBounce: Sprite = FadingCircle()
        progressBar?.indeterminateDrawable = doubleBounce
    }

    private fun setupAddToFavorite() {
        mBinding?.imvFavorite?.setOnClickListener {
            // Todo: add favorite shoes
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setupShoesData(shoesId: String) {
        mShoesViewModel.getShoesById(shoesId).observe(viewLifecycleOwner) {
            it?.let {
                when (it.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        val shoes = it.data
                        shoes?.let {
                            mBinding?.run {
                                mShoesViewModel.setCurrentShoes(it)
                                mShoesViewModel.updateCurrentOrderDetail(
                                    OrderDetail(shoesId = shoes._id)
                                )
                                stopPlaceHolderLayout()
                            }

                        }
                    }

                    Status.ERROR -> {
                        Log.e(TAG, "setupShoesData: ${it.message}")
                        stopPlaceHolderLayout()
                    }
                }
            }
        }
    }

    private fun setupCurrentShoes() {
        mShoesViewModel.mCurrentShoes.observe(viewLifecycleOwner) { shoes ->
            shoes?.let {
                mBinding?.run {
                    viewPagerShoesImage.adapter =
                        ShoesImageAdapter(this@ShoesFragment, shoes.images)
                    autoRunOfferBanner(shoes.images.size)
                    tvTitle.text = shoes.name
                    tvDescription.text = shoes.description
                    sold.text = "${shoes.sold} sold"
                    rate.text = "${shoes.rate}"
                    setupSizeDataList()
                    setupColorDataList()
                    setupQuantity()
                    setupBtnAddToCar()
                }
            }
        }
    }

    private fun autoRunOfferBanner(size: Int) {
        val currentIndex = mBinding?.viewPagerShoesImage?.currentItem
        currentIndex?.let {
            var index = 0
            if (currentIndex < size - 1) {
                index = mBinding?.viewPagerShoesImage?.currentItem?.plus(1) ?: 0
            } else if (currentIndex >= size) {
                index = 0
            }

            mBinding?.viewPagerShoesImage?.setCurrentItem(index, false)
            Handler(Looper.getMainLooper()).postDelayed({
                autoRunOfferBanner(size)
            }, 3000L)
        }
    }

    private fun setupBtnAddToCar() {
        mBinding?.btnAddToCart?.setOnClickListener {
            Log.i(TAG, "setupShoesData: " + mShoesViewModel.mCurrentOrderDetail.value.toString())
            startProcessBar()
            Thread.sleep(3000L)
            Log.i(TAG, "push success")
            stopProcessBar()
        }
    }

    private fun setupQuantity() {
        mBinding?.btnReduce?.setOnClickListener {
            val currentValue = Integer.valueOf(mBinding?.editQuantity?.text.toString())
            if (currentValue > 1) {
                mBinding?.editQuantity?.text = currentValue.minus(1).toString()
            }
        }

        mBinding?.btnAugment?.setOnClickListener {
            val currentValue = Integer.valueOf(mBinding?.editQuantity?.text.toString())
            if (currentValue <= 30) {
                mBinding?.editQuantity?.text = currentValue.plus(1).toString()
            } else {
                Toast.makeText(
                    this.context,
                    "Please, contact to admin to buy a big amount of shoes",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
        mShoesViewModel.mCurrentShoes.observe(viewLifecycleOwner) { shoes ->
            shoes?.let {
                mBinding?.editQuantity?.doAfterTextChanged {
                    Log.e("TAG", "setupShoesData: " + mBinding?.editQuantity?.text.toString())
                    val numberOfShoes =
                        Integer.parseInt(mBinding?.editQuantity?.text.toString())
                    val totalPrice = numberOfShoes * shoes.price
                    mBinding?.tvTotalPrice?.text = totalPrice.toString()
                    mShoesViewModel.updateCurrentOrderDetail(
                        mShoesViewModel.mCurrentOrderDetail.value?.apply {
                            this.numberOfShoes = numberOfShoes
                            this.totalPrice = totalPrice.toLong()
                        }
                    )
                }
                mBinding?.editQuantity?.text = "1"
            }
        }

    }

    private fun setupColorDataList() {
        mShoesViewModel.mCurrentShoes.observe(viewLifecycleOwner) { shoes ->
            shoes?.let {
                mColorAdapter.setOnClick { color ->
                    mShoesViewModel.updateCurrentOrderDetail(
                        mShoesViewModel.mCurrentOrderDetail.value?.copy(
                            color = color
                        )
                    )
                    val updatedColorDataList = mutableListOf<Pair<String, Boolean>>()
                    shoes.available_colors.forEach {
                        if (color == it) {
                            updatedColorDataList.add(Pair(it, true))
                        } else {
                            updatedColorDataList.add(Pair(it, false))
                        }
                    }
                    mColorAdapter.setData(updatedColorDataList)
                }
                val colorDataList = mutableListOf<Pair<String, Boolean>>()
                shoes.available_colors.forEach {
                    colorDataList.add(Pair(it, false))
                }
                mColorAdapter.setData(colorDataList)
                mBinding?.rcvColor?.adapter = mColorAdapter
            }
        }

    }

    private fun setupSizeDataList() {
        mShoesViewModel.mCurrentShoes.observe(viewLifecycleOwner) { shoes ->

            mSizeAdapter.setOnClickListener { size ->
                mShoesViewModel.updateCurrentOrderDetail(
                    mShoesViewModel.mCurrentOrderDetail.value?.apply {
                        this.size = size
                    }
                )

                mShoesViewModel.mCurrentShoes.value?.let { currentShoes ->
                    val newSizeDataList = mutableListOf<Pair<Int, Boolean>>()

                    currentShoes.available_sizes.forEach {
                        if (it == size) {
                            newSizeDataList.add(Pair(it, true))
                        } else {
                            newSizeDataList.add(Pair(it, false))
                        }
                    }
                    mSizeAdapter.setData(newSizeDataList)
                }

            }

            val sizeDataList = mutableListOf<Pair<Int, Boolean>>()
            shoes.available_sizes.forEach {
                sizeDataList.add(Pair(it, false))
            }
            mSizeAdapter.setData(sizeDataList)
            mBinding?.rcvSize?.adapter = mSizeAdapter
        }

    }


    override fun onStop() {
        super.onStop()
        setupBottomBar(true)
    }


}