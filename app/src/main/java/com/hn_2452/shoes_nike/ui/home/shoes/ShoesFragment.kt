package com.hn_2452.shoes_nike.ui.home.shoes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.databinding.FragmentShoesBinding
import com.hn_2452.shoes_nike.ui.home.shoes.shoes_image.ShoesImageAdapter
import com.hn_2452.shoes_nike.utility.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoesFragment : BaseFragment<FragmentShoesBinding>() {

    private val mShoesViewModel: ShoesViewModel by viewModels()
    private val mArgs: ShoesFragmentArgs by navArgs()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShoesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomBar(false)
        setupShoesData(mArgs.shoesId)
        setupCurrentOrder()
    }

    private fun setupCurrentOrder() {
        mShoesViewModel.mCurrentOrderDetail.observe(viewLifecycleOwner) { orderDetail ->
            orderDetail?.let {

            }
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
                                mShoesViewModel.updateCurrentOrderDetail(
                                    OrderDetail(shoesId = shoes._id)
                                )
                                viewPagerShoesImage.adapter =
                                    ShoesImageAdapter(this@ShoesFragment, shoes.images)
                                tvTitle.text = shoes.name
                                tvDescription.text = shoes.description
                                sold.text = "${shoes.sold} sold"
                                rate.text = "${shoes.rate}"

                                val sizeController = SizeAdapter { size ->
                                    mShoesViewModel.updateCurrentOrderDetail(
                                        mShoesViewModel.mCurrentOrderDetail.value?.apply {
                                            this.size = size
                                        }
                                    )
                                }
                                sizeController.setData(shoes.available_sizes)
                                rcvSize.setController(sizeController)

                                val colorController = ColorAdapter { color, selected ->
                                    mShoesViewModel.updateCurrentOrderDetail(
                                        mShoesViewModel.mCurrentOrderDetail.value?.apply {
                                            if(selected) {
                                                this.color = color
                                            }
                                        }
                                    )
                                }
                                colorController.setData(shoes.available_colors)
                                rcvColor.setController(colorController)


                                editQuantity.doAfterTextChanged {
                                    Log.e("TAG", "setupShoesData: " +  editQuantity.text.toString() )
                                    val numberOfShoes = Integer.parseInt(editQuantity.text!!.toString())
                                    val totalPrice = numberOfShoes * shoes.price
                                    mBinding?.tvTotalPrice?.text = totalPrice.toString()
                                    mShoesViewModel.updateCurrentOrderDetail(
                                        mShoesViewModel.mCurrentOrderDetail.value?.apply {
                                            this.numberOfShoes = numberOfShoes
                                            this.totalPrice = totalPrice.toLong()
                                        }
                                    )
                                }
                                editQuantity.text = "1"

                                mBinding?.btnAddToCart?.setOnClickListener {
                                    Log.i("TAG", "setupShoesData: " + mShoesViewModel.mCurrentOrderDetail.value.toString())
                                }
                            }

                        }
                    }

                    Status.ERROR -> {

                    }
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        setupBottomBar(true)
    }


}