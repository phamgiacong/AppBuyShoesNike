package com.hn_2452.shoes_nike.ui.searching

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.slider.RangeSlider
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.model.ShoesType
import com.hn_2452.shoes_nike.databinding.FragmentSearchBinding
import com.hn_2452.shoes_nike.databinding.SearchOptionItemBinding
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.dpToPx
import com.hn_2452.shoes_nike.utility.toVND
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject


const val DEFAULT_SHOES_ID = "com.hn_2452.shoes_nike.default"
const val GENDER_TAG = "gender"
const val SORT_TAG = "sort"
const val STAR_TAG = "star"
const val PRICE_TAG = "price"
const val TYPE_TAG = "type"

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    companion object {
        private const val TAG = "Nike:SearchFragment: "
    }

    private val mSearchViewModel: SearchViewModel by activityViewModels()

    private lateinit var mBottomSheetDialog: BottomSheetDialog

    @Inject
    lateinit var mRecentSearchAdapter: RecentSearchAdapter

    @Inject
    lateinit var mShoesAdapter: ShoesAdapterController

    @Inject
    lateinit var mShoesTypeAdapter: ShoesTypeAdapter

    @Inject
    lateinit var mStarAdapter: StarAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearching()
        setupRcvRecentSearching()
        setupRcvShoesList()
        setupClearAllRecentSearch()
        setupBtnSearch()

        setupFilterAndSort()
        setupQueryText()
        setupShoesList()
        fillPreviousData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSearchViewModel.mShowSortOption = false
    }

    private fun fillPreviousData() {
        mSearchViewModel.mQueryText.value = mSearchViewModel.mQueryText.value
        mSearchViewModel.mSortAndFilter.value = mSearchViewModel.mSortAndFilter.value?.copy()
    }

    private fun setupShoesList() {
        mSearchViewModel.mCurrentShoesList.observe(viewLifecycleOwner) {
            mBinding?.tvFound?.text = "${it?.size ?: 0} sản phẩm"
            mShoesAdapter.setData(it)
        }
    }

    private fun setupQueryText() {
        mSearchViewModel.mQueryText.observe(viewLifecycleOwner) { queryText ->
            if (mSearchViewModel.mNoObserveQueryText) {
                mSearchViewModel.mNoObserveQueryText = false
                return@observe
            }
            mBinding?.edtSearch?.setText(queryText)
        }
    }

    private fun updateStarUI(star: Int) {
        removeSearchOptionByTag(STAR_TAG)
        if (star != -1) {
            addSearchOptionByTag(STAR_TAG, "Đánh giá: $star sao") {
                setupRcvStar()
                mSearchViewModel.mSortAndFilter.value =
                    mSearchViewModel.mSortAndFilter.value?.copy(star = -1)
            }
        }

        val updateStartList = mutableListOf<Pair<Int, Boolean>>()
        mSearchViewModel.mStarList.forEach {
            if (it == star) {
                updateStartList.add(Pair(it, true))
            } else {
                updateStartList.add(Pair(it, false))
            }
        }
        mStarAdapter.setData(updateStartList)
    }

    private fun updateShoesTypeListUI(shoesType: ShoesType) {
        removeSearchOptionByTag(TYPE_TAG)
        if (shoesType.id != DEFAULT_SHOES_ID) {
            addSearchOptionByTag(TYPE_TAG, "Loại giày: ${shoesType.name}") {
                setupRcvShoesTypeList()
                mSearchViewModel.mSortAndFilter.value =
                    mSearchViewModel.mSortAndFilter.value?.copy(type = ShoesType(id = DEFAULT_SHOES_ID))
            }
        }
        val updatedShoesTypeList = mutableListOf<Pair<ShoesType, Boolean>>()
        updatedShoesTypeList.add(
            Pair(
                ShoesType(id = DEFAULT_SHOES_ID, name = "All"),
                shoesType.id == DEFAULT_SHOES_ID
            )
        )
        mSearchViewModel.mShoesTypeList.forEach {
            if (it.id == shoesType.id) {
                updatedShoesTypeList.add(Pair(it, true))
            } else {
                updatedShoesTypeList.add(Pair(it, false))
            }
        }
        mShoesTypeAdapter.setData(updatedShoesTypeList)
    }

    private fun updatePriceUI(values: MutableList<Float>) {
        removeSearchOptionByTag(PRICE_TAG)
        if (values[0].toLong() != 50000L || values[1].toLong() != 10000000L) {
            addSearchOptionByTag(
                PRICE_TAG,
                "Giá từ ${values[0].toLong().toVND()} tới ${values[1].toLong().toVND()}"
            ) {
                mSearchViewModel.mAsSetPriceOptionFromUserAction = true
                val slide = mBottomSheetDialog.findViewById<RangeSlider>(R.id.price_range)
                slide?.setValues(MIN_VALUE.toFloat(), MAX_VALUE.toFloat())
            }
        }
    }

    private fun updateGenderUI(gender: Int) {
        val allGender = mBottomSheetDialog.findViewById<TextView>(R.id.gender_all)
        val menGender = mBottomSheetDialog.findViewById<TextView>(R.id.gender_men)
        val womenGender = mBottomSheetDialog.findViewById<TextView>(R.id.gender_women)
        when (gender) {
            0 -> {
                allGender?.setTextColor(Color.WHITE)
                allGender?.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                menGender?.setTextColor(Color.BLACK)
                menGender?.backgroundTintList = null
                womenGender?.setTextColor(Color.BLACK)
                womenGender?.backgroundTintList = null
                removeSearchOptionByTag(GENDER_TAG)
            }

            1 -> {
                allGender?.setTextColor(Color.BLACK)
                allGender?.backgroundTintList = null
                menGender?.setTextColor(Color.WHITE)
                menGender?.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                womenGender?.setTextColor(Color.BLACK)
                womenGender?.backgroundTintList = null


                removeSearchOptionByTag(GENDER_TAG)
                addSearchOptionByTag(GENDER_TAG, "Giới tính: Nam") {
                    allGender?.performClick()
                }
            }

            2 -> {
                allGender?.setTextColor(Color.BLACK)
                allGender?.backgroundTintList = null
                menGender?.setTextColor(Color.BLACK)
                menGender?.backgroundTintList = null
                womenGender?.setTextColor(Color.WHITE)
                womenGender?.backgroundTintList = ColorStateList.valueOf(Color.BLACK)


                removeSearchOptionByTag(GENDER_TAG)
                addSearchOptionByTag(GENDER_TAG, "Giới tính: Nữ") {
                    allGender?.performClick()
                }
            }
        }
    }

    private fun updateSortUI(sort: String) {
        val popular = mBottomSheetDialog.findViewById<TextView>(R.id.sortPopular)
        val recent = mBottomSheetDialog.findViewById<TextView>(R.id.sortRecent)
        when (sort) {
            POPULAR -> {
                popular?.setTextColor(Color.WHITE)
                popular?.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                recent?.setTextColor(Color.BLACK)
                recent?.backgroundTintList = null

                removeSearchOptionByTag(SORT_TAG)
                if(mSearchViewModel.mShowSortOption) {
                    addSearchOptionByTag(SORT_TAG, "Sắp xếp: Phổ biến") {
                        popular?.setTextColor(Color.WHITE)
                        popular?.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                        recent?.setTextColor(Color.BLACK)
                        recent?.backgroundTintList = null
                        mSearchViewModel.mShowSortOption = false
                        mSearchViewModel.mSortAndFilter.value =
                            mSearchViewModel.mSortAndFilter.value?.copy(sort = POPULAR)
                    }
                }


            }

            RECENT -> {
                popular?.setTextColor(Color.BLACK)
                popular?.backgroundTintList = null
                recent?.setTextColor(Color.WHITE)
                recent?.backgroundTintList = ColorStateList.valueOf(Color.BLACK)

                removeSearchOptionByTag(SORT_TAG)
                if(mSearchViewModel.mShowSortOption) {
                    addSearchOptionByTag(SORT_TAG, "Sắp xếp: Gần đây") {
                        popular?.setTextColor(Color.WHITE)
                        popular?.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                        recent?.setTextColor(Color.BLACK)
                        recent?.backgroundTintList = null
                        mSearchViewModel.mShowSortOption = false
                        mSearchViewModel.mSortAndFilter.value =
                            mSearchViewModel.mSortAndFilter.value?.copy(sort = POPULAR)
                    }
                }

            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupFilterAndSort() {
        mBinding?.imvFilter?.setOnClickListener {
            showFilterAndSortSheet()
        }
        val view = layoutInflater.inflate(R.layout.layout_filter_search_bottom_sheet, null)
        mBottomSheetDialog = BottomSheetDialog(requireContext())
        mBottomSheetDialog.setContentView(view)

        setupStarList()
        setupShoesTypeList()
        setupPriceRange()
        setupGender()
        setupSort()
        setupBtnReset()

        mSearchViewModel.mSortAndFilter.observe(viewLifecycleOwner) { filter ->
            Log.i(TAG, "setupFilterAndSort: $filter")

            updateStarUI(filter.star)
            updateShoesTypeListUI(filter.type)
            updateGenderUI(filter.gender)
            updateSortUI(filter.sort)

            val slide = mBottomSheetDialog.findViewById<RangeSlider>(R.id.price_range)
            slide?.setValues(filter.priceRange.first.toFloat(), filter.priceRange.second.toFloat())
            updatePriceUI(mutableListOf(filter.priceRange.first.toFloat(), filter.priceRange.second.toFloat()))

            performSearch()
        }
    }

    private fun setupBtnReset() {
        val btnReset = mBottomSheetDialog.findViewById<Button>(R.id.btnReset)
        btnReset?.setOnClickListener {
            mSearchViewModel.mSortAndFilter.value =
                mSearchViewModel.mSortAndFilter.value?.copy(
                    type = ShoesType(id = DEFAULT_SHOES_ID),
                    gender = 0,
                    sort = POPULAR,
                    star = -1
                )
            val slide = mBottomSheetDialog.findViewById<RangeSlider>(R.id.price_range)
            slide?.setValues(MIN_VALUE.toFloat(), MAX_VALUE.toFloat())
            mBinding?.searchOptionLayout?.removeAllViews()
        }
    }

    private fun setupSort() {
        val popular = mBottomSheetDialog.findViewById<TextView>(R.id.sortPopular)
        val recent = mBottomSheetDialog.findViewById<TextView>(R.id.sortRecent)

        popular?.setTextColor(Color.WHITE)
        popular?.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
        popular?.setOnClickListener {
            mSearchViewModel.mShowSortOption = true
            mSearchViewModel.mSortAndFilter.value =
                mSearchViewModel.mSortAndFilter.value?.copy(sort = POPULAR)
        }

        recent?.setOnClickListener {
            mSearchViewModel.mShowSortOption = true
            mSearchViewModel.mSortAndFilter.value =
                mSearchViewModel.mSortAndFilter.value?.copy(sort = RECENT)
        }

    }

    private fun setupGender() {
        val allGender = mBottomSheetDialog.findViewById<TextView>(R.id.gender_all)
        val menGender = mBottomSheetDialog.findViewById<TextView>(R.id.gender_men)
        val womenGender = mBottomSheetDialog.findViewById<TextView>(R.id.gender_women)

        allGender?.setTextColor(Color.WHITE)
        allGender?.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
        allGender?.setOnClickListener {
            mSearchViewModel.mSortAndFilter.value =
                mSearchViewModel.mSortAndFilter.value?.copy(gender = GENDER_ALL)
        }

        menGender?.setOnClickListener {
            mSearchViewModel.mSortAndFilter.value =
                mSearchViewModel.mSortAndFilter.value?.copy(gender = GENDER_MEN)
        }

        womenGender?.setOnClickListener {
            mSearchViewModel.mSortAndFilter.value =
                mSearchViewModel.mSortAndFilter.value?.copy(gender = GENDER_WOMEN)
        }
    }

    private fun removeSearchOptionByTag(tag: String) {
        val genderView = mBinding?.root?.findViewWithTag<LinearLayout>(tag)
        genderView?.let {
            mBinding?.searchOptionLayout?.removeView(it)
        }
    }

    private fun addSearchOptionByTag(tag: String, name: String, onDelete: () -> Unit) {
        val recentViewBinding =
            SearchOptionItemBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        recentViewBinding.clear.setOnClickListener {
            mBinding?.searchOptionLayout?.removeView(recentViewBinding.root)
            onDelete()
        }
        val recentView = recentViewBinding.root

        val layoutParams = FlexboxLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.rightMargin = dpToPx(requireContext(), 16)
        layoutParams.bottomMargin = dpToPx(requireContext(), 8)
        recentView.layoutParams = layoutParams

        val tvName = recentViewBinding.tvName
        tvName.text = name
        recentView.tag = tag
        mBinding?.searchOptionLayout?.addView(recentView)
    }

    private fun setupStarList() {
        val rcvStarList = mBottomSheetDialog.findViewById<RecyclerView>(R.id.rcvStart)
        mStarAdapter.setOnClick { star ->
            mSearchViewModel.mSortAndFilter.value =
                mSearchViewModel.mSortAndFilter.value?.copy(star = star)
        }

        setupRcvStar()
        rcvStarList?.adapter = mStarAdapter
    }

    private fun setupRcvStar() {
        val starList = mutableListOf<Pair<Int, Boolean>>()
        mSearchViewModel.mStarList.forEach {
            if (it == -1) {
                starList.add(Pair(it, true))
            } else {
                starList.add(Pair(it, false))
            }
        }
        mStarAdapter.setData(starList)
    }

    private fun setupPriceRange() {
        val slide = mBottomSheetDialog.findViewById<RangeSlider>(R.id.price_range)
        slide?.setLabelFormatter { value ->
            NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(value)
        }

        slide?.addOnChangeListener { slider, _, fromUser ->
            if (fromUser || mSearchViewModel.mAsSetPriceOptionFromUserAction) {
                mSearchViewModel.mAsSetPriceOptionFromUserAction = false
                val values = slider.values
                Log.i(TAG, "setupPriceRange: start=${values[0]} end=${values[1]}")
                val newValue = Pair(values[0].toLong(), values[1].toLong())
                mSearchViewModel.mSortAndFilter.value =
                    mSearchViewModel.mSortAndFilter.value?.copy(priceRange = newValue)
                updatePriceUI(values)
            }
        }
    }

    private fun setupShoesTypeList() {
        val rcvShoesType = mBottomSheetDialog.findViewById<RecyclerView>(R.id.rcvShoesType)
        mShoesTypeAdapter.setOnClick { shoesType ->
            mSearchViewModel.mSortAndFilter.value =
                mSearchViewModel.mSortAndFilter.value?.copy(type = shoesType)
        }
        rcvShoesType?.adapter = mShoesTypeAdapter

        if(mSearchViewModel.mShoesTypeList.isEmpty()) {
            mSearchViewModel.getShoesType().observe(viewLifecycleOwner) { result ->
                result?.let {
                    when (result.status) {
                        Status.LOADING -> {
                            Log.i(TAG, "setupFilterAndSort: loading...")
                        }

                        Status.SUCCESS -> {
                            Log.i(TAG, "getShoesTypeList: ${result.data}")
                            mSearchViewModel.mShoesTypeList = result.data ?: emptyList()
                            setupRcvShoesTypeList()
                        }

                        Status.ERROR -> {
                            Log.e(TAG, "getShoesTypeList: ${result.message}")
                        }
                    }
                }
            }
        }

    }

    private fun setupRcvShoesTypeList() {
        val shoesTypeList = mutableListOf<Pair<ShoesType, Boolean>>()
        shoesTypeList.add(
            Pair(
                ShoesType(id = DEFAULT_SHOES_ID, name = "All"),
                true
            )
        )
        mSearchViewModel.mShoesTypeList.forEach { shoesType ->
            shoesTypeList.add(Pair(shoesType, false))
        }
        mShoesTypeAdapter.setData(shoesTypeList)
    }

    private fun showFilterAndSortSheet() {
        mBottomSheetDialog.show()
    }


    private fun setupBtnSearch() {
        mBinding?.edtSearch?.addTextChangedListener {
            mSearchViewModel.mNoObserveQueryText = true
            mSearchViewModel.mQueryText.value = it.toString()
            if (it.isNullOrEmpty()) {
                mBinding?.recentSearchingLayout?.visibility = View.VISIBLE
                mBinding?.SearchedShoesLayout?.visibility = View.GONE
            } else {
                mBinding?.recentSearchingLayout?.visibility = View.GONE
                mBinding?.SearchedShoesLayout?.visibility = View.VISIBLE
            }
        }
    }

    private fun setupClearAllRecentSearch() {
        mBinding?.tvClearAll?.setOnClickListener {
            mSearchViewModel.deleteRecentSearching()
        }
    }

    private fun setupRcvShoesList() {
        mBinding?.rcvShoesList?.setController(mShoesAdapter)
        mShoesAdapter.setOnClick { shoes ->
            mNavController?.navigate(
                SearchFragmentDirections.actionSearchFragmentToShoesFragment(shoes._id)
            )
        }
    }

    private fun setupRcvRecentSearching() {
        mSearchViewModel.mRecentSearching.observe(viewLifecycleOwner) { searchList ->
            searchList?.let {
                mRecentSearchAdapter.setData(it)
            }
        }
        mRecentSearchAdapter.setOnClick {
            mBinding?.edtSearch?.setText(it.content)
            performSearch()
        }

        mRecentSearchAdapter.setOnDelete {
            mSearchViewModel.deleteRecentSearching(it)
        }
        mBinding?.rcvRecentSearch?.setController(mRecentSearchAdapter)
    }

    private fun setupSearching() {
        mBinding?.edtSearch?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
            }
            true
        }
    }

    private fun performSearch() {
        val string = mBinding?.edtSearch?.text.toString()
        if(string.isEmpty()) {
            return
        }
        mSearchViewModel.loadingShoesByName(string).observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        mBinding?.recentSearchingLayout?.visibility = View.GONE
                        mBinding?.SearchedShoesLayout?.visibility = View.VISIBLE
                        mSearchViewModel.addRecentSearching(string)

                        Log.i(TAG, "performSearch: ${result.data}")
                        mShoesAdapter.setData(result.data)
                        mBinding?.tvFound?.text = "${result.data?.size ?: 0} sản phẩm"
                    }

                    Status.ERROR -> {
                        Log.e(TAG, "performSearch: ${result.message}")
                    }
                }
            }
        }
    }

    private fun filter(shoesList: List<Shoes>?, filter: SortAndFilter): List<Shoes>? {
        Log.i(TAG, "shoesList: ${shoesList?.size} filter: ${filter}")
        val filteredShoesList = shoesList?.filter { shoes ->
            (shoes.gender == filter.gender || filter.gender == 0) &&
                    (shoes.price >= filter.priceRange.first && shoes.price <= filter.priceRange.second)
                    && (shoes.rate.toInt() == filter.star || filter.star == -1)
                    && (shoes.type == filter.type.id || filter.type.id == DEFAULT_SHOES_ID)
        }

        if (filter.sort == POPULAR) {
            filteredShoesList?.sortedBy { shoes -> shoes.sold }
        } else {
            filteredShoesList?.sortedBy { shoes -> shoes.created_date }
        }
        Log.i(TAG, "output filter: ${filteredShoesList?.size}")
        return filteredShoesList
    }
}