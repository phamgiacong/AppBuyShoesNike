package com.hn_2452.shoes_nike.ui.home.shoes

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.databinding.FragmentShoesBinding
import com.hn_2452.shoes_nike.ui.home.shoes.shoes_image.ShoesImageAdapter
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.getOriginPrice
import com.hn_2452.shoes_nike.utility.getPrice
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import com.hn_2452.shoes_nike.utility.handleResource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ShoesFragment : BaseFragment<FragmentShoesBinding>() {

    companion object {
        private const val TAG = "Nike:ShoesFragment: "
    }

    private val mShoesViewModel: ShoesViewModel by activityViewModels()

    private val mArgs: ShoesFragmentArgs by navArgs()

    @Inject
    lateinit var mSizeAdapter: SizeAdapter

    @Inject
    lateinit var mColorAdapter: ColorAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShoesBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mShoesViewModel.mNeedToLoadOldData = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mShoesViewModel.mSelectedSize = -1
        mShoesViewModel.mSelectedColor = null
        mShoesViewModel.mSelectedNumber = 1
        mShoesViewModel.mNeedToLoadOldData = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPlaceHolderLayout()
        setupLoading(mBinding?.loadingProgress)
        setupToolbar(mBinding?.toolBar)
        setupShoesData(mArgs.shoesId)
        setupCurrentShoes()
        setupAddToFavorite()
        setupBtnAddOrderDetailToCart()
        setupBuyNow()
        loadFavoriteShoesState()

        setupSizeDataList()
        setupColorDataList()
        setupQuantity()


    }

    private fun setupBuyNow() {
        mBinding?.btnBuyNow?.setOnClickListener {
            if (getStringDataByKey(requireContext(), TOKEN).isEmpty()) {
                showLoginRequestPopup()
                return@setOnClickListener
            }
            mShoesViewModel.mNeedToLoadOldData = true
            mShoesViewModel.buyNow().observe(viewLifecycleOwner) {
                val action = ShoesFragmentDirections.actionShoesFragmentToBuyNowFragment(it)
                mNavController?.navigate(action)
            }
        }
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


    private fun setupAddToFavorite() {
        mBinding?.imvFavorite?.setOnClickListener {
            if (getStringDataByKey(requireContext(), TOKEN).isEmpty()) {
                showLoginRequestPopup()
                return@setOnClickListener
            }
            handleResource(
                data = mShoesViewModel.addFavoriteShoes(mArgs.shoesId),
                lifecycleOwner = viewLifecycleOwner,
                context = requireContext(),
                onSuccess = {
                    if (it == true) {
                        Toast.makeText(
                            requireContext(),
                            "Thêm thành công giày vào mục ưu thích",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Xóa thành công giày vào mục ưu thích",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    loadFavoriteShoesState()
                },
                isErrorInform = true
            )
        }
    }

    private fun loadFavoriteShoesState() {
        val token = getStringDataByKey(requireContext(), TOKEN)
        if (token.isNotEmpty()) {
            handleResource(
                data = mShoesViewModel.checkFavoriteShoes(mArgs.shoesId),
                lifecycleOwner = viewLifecycleOwner,
                context = requireContext(),
                onSuccess = {
                    if (it == true) {
                        mBinding?.imvFavorite?.imageTintList = ColorStateList.valueOf(Color.RED)
                    } else {
                        mBinding?.imvFavorite?.imageTintList = ColorStateList.valueOf(Color.BLACK)
                    }
                }
            )
        }

    }


    @SuppressLint("SetTextI18n")
    private fun setupShoesData(shoesId: String) {
        if (mShoesViewModel.mNeedToLoadOldData) {
            stopPlaceHolderLayout()
            return
        }

        mShoesViewModel.getShoesById(shoesId).observe(viewLifecycleOwner) {
            it?.let {
                when (it.status) {
                    Status.LOADING -> {
                        Log.i(TAG, "setupShoesData: loading...")
                    }

                    Status.SUCCESS -> {
                        val shoes = it.data
                        shoes?.let {
                            mBinding?.run {
                                mShoesViewModel.setCurrentShoes(it)
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
                    when (shoes.state) {
                        0 -> {
                            mBinding?.tvNote?.text = "Sản phẩm ngừng kinh doanh"
                            mBinding?.tvNote?.visibility = View.VISIBLE
                            mBinding?.btnAddToCart?.visibility = View.GONE
                            mBinding?.btnBuyNow?.visibility = View.GONE
                        }

                        1 -> {
                            mBinding?.btnAddToCart?.visibility = View.VISIBLE
                            mBinding?.btnBuyNow?.visibility = View.VISIBLE
                        }

                        2 -> {
                            mBinding?.tvNote?.text = "Sản phẩm đang hết hàng"
                            mBinding?.tvNote?.visibility = View.VISIBLE
                            mBinding?.btnAddToCart?.isEnabled = false
                            mBinding?.btnBuyNow?.isEnabled = false
                        }
                    }


                    viewPagerShoesImage.adapter =
                        ShoesImageAdapter(this@ShoesFragment, shoes.images)
                    autoRunOfferBanner(shoes.images.size)
                    tvTitle.text = shoes.name
                    tvPrice.text = getPrice(shoes)
                    if (shoes.discount > 0) {
                        tvOriginPrice.visibility = View.VISIBLE
                        tvOriginPrice.text = getOriginPrice(shoes)
                    } else {
                        tvOriginPrice.visibility = View.GONE
                    }
                    tvDescription.text = shoes.description
                    sold.text = getString(R.string.shoes_sold, shoes.sold)
                    rate.text = "${shoes.rate} (${shoes.number_of_reviews} đánh giá)"
                    tvWatchReview.setOnClickListener {
                        if (shoes.number_of_reviews > 0) {
                            mNavController?.navigate(
                                ShoesFragmentDirections.actionShoesFragmentToShoesReviewFragment2(
                                    shoes._id
                                )
                            )
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.not_yet_review),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
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

    private fun setupBtnAddOrderDetailToCart() {
        mBinding?.btnAddToCart?.setOnClickListener {
            if (getStringDataByKey(requireContext(), TOKEN).isEmpty()) {
                showLoginRequestPopup()
                return@setOnClickListener
            }
            mShoesViewModel.addOrderDetail().observe(viewLifecycleOwner) { result ->
                when (result?.status) {
                    Status.LOADING -> {
                        Log.i(TAG, "setupBtnAddToCar: loading...")
                    }

                    Status.SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            "Thêm sản phẩm vào giỏ thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.i(TAG, "setupBtnAddToCar: success ${result.data}")
                        resetShoes()
                    }

                    Status.ERROR -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "setupBtnAddToCar: ${result.message}")
                    }

                    null -> {
                        Log.i(TAG, "setupBtnAddToCar: null")
                    }
                }
            }
        }
    }

    private fun resetShoes() {
        mBinding?.editQuantity?.text = "1"

        val sizeDataList = mutableListOf<Pair<Int, Boolean>>()
        mShoesViewModel.mCurrentShoes.value?.available_sizes?.forEach {
            sizeDataList.add(Pair(it, false))
        }
        mSizeAdapter.setData(sizeDataList)
        mShoesViewModel.mSelectedSize = -1

        val colorDataList = mutableListOf<Pair<String, Boolean>>()
        mShoesViewModel.mCurrentShoes.value?.available_colors?.forEach {
            colorDataList.add(Pair(it, false))
        }
        mColorAdapter.setData(colorDataList)
        mShoesViewModel.mSelectedColor = null
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

        if (mShoesViewModel.mNeedToLoadOldData) {
            mBinding?.editQuantity?.text = mShoesViewModel.mSelectedNumber.toString()
        }

        mBinding?.editQuantity?.doAfterTextChanged {
            mShoesViewModel.mSelectedNumber = it.toString().toInt()
        }

    }

    private fun setupColorDataList() {
        mShoesViewModel.mCurrentShoes.observe(viewLifecycleOwner) { shoes ->
            shoes?.let {
                mColorAdapter.setOnClick { color ->
                    mShoesViewModel.mSelectedColor = color
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

                if (mShoesViewModel.mNeedToLoadOldData) {
                    val updatedColorDataList = mutableListOf<Pair<String, Boolean>>()
                    shoes.available_colors.forEach {
                        if (mShoesViewModel.mSelectedColor == it) {
                            updatedColorDataList.add(Pair(it, true))
                        } else {
                            updatedColorDataList.add(Pair(it, false))
                        }
                    }
                    mColorAdapter.setData(updatedColorDataList)
                } else {
                    val colorDataList = mutableListOf<Pair<String, Boolean>>()
                    shoes.available_colors.forEach {
                        colorDataList.add(Pair(it, false))
                    }
                    mColorAdapter.setData(colorDataList)
                }
                mBinding?.rcvColor?.adapter = mColorAdapter
            }
        }

    }

    private fun setupSizeDataList() {
        mShoesViewModel.mCurrentShoes.observe(viewLifecycleOwner) { shoes ->

            mSizeAdapter.setOnClickListener { size ->
                mShoesViewModel.mSelectedSize = size
                val newSizeDataList = mutableListOf<Pair<Int, Boolean>>()
                shoes?.available_sizes?.forEach {
                    if (it == size) {
                        newSizeDataList.add(Pair(it, true))
                    } else {
                        newSizeDataList.add(Pair(it, false))
                    }
                }
                mSizeAdapter.setData(newSizeDataList)
            }

            if (mShoesViewModel.mNeedToLoadOldData) {
                val newSizeDataList = mutableListOf<Pair<Int, Boolean>>()
                shoes?.available_sizes?.forEach {
                    if (it == mShoesViewModel.mSelectedSize) {
                        newSizeDataList.add(Pair(it, true))
                    } else {
                        newSizeDataList.add(Pair(it, false))
                    }
                }
                mSizeAdapter.setData(newSizeDataList)
            } else {
                val sizeDataList = mutableListOf<Pair<Int, Boolean>>()
                shoes?.available_sizes?.forEach {
                    sizeDataList.add(Pair(it, false))
                }
                mSizeAdapter.setData(sizeDataList)
            }
            mBinding?.rcvSize?.adapter = mSizeAdapter
        }

    }


}