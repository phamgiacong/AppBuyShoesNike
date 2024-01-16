package com.hn_2452.shoes_nike.ui.searching

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.hn_2452.shoes_nike.data.local.NikeDatabase
import com.hn_2452.shoes_nike.data.model.Searching
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.model.ShoesType
import com.hn_2452.shoes_nike.data.repository.ShoesRepository
import com.hn_2452.shoes_nike.data.repository.ShoesTypeRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.handleEx
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mShoesRepository: ShoesRepository,
    private val mShoesTypeRepository: ShoesTypeRepository,
    private val mNikeDatabase: NikeDatabase,
    @ApplicationContext private val mContext: Context
) : ViewModel() {

    var mShowSortOption = false
    var mAsSetPriceOptionFromUserAction = false
    var mNoObserveQueryText = false


    var mShoesTypeList: List<ShoesType> = emptyList()

    var mStarList: List<Int> = mutableListOf(-1, 5, 4, 3, 2, 1, 0)

    val mSortAndFilter = MutableLiveData(SortAndFilter())

    var mCurrentShoesList : MutableLiveData<List<Shoes>?> = MutableLiveData()

    var mQueryText : MutableLiveData<String?> = MutableLiveData("")

    companion object {
        private const val TAG = "Nike:SearchViewModel: "
    }

    val mRecentSearching: LiveData<List<Searching>> = mNikeDatabase.searchingDao().getAllSearching()

    fun addRecentSearching(content: String) = viewModelScope.launch(Dispatchers.IO + handleEx(mContext)) {
        val searching = Searching(content)
        mNikeDatabase.searchingDao().addSearching(searching)
    }

    fun deleteRecentSearching(searching: Searching) = viewModelScope.launch(Dispatchers.IO + handleEx(mContext)) {
        mNikeDatabase.searchingDao().deleteSearching(searching)
    }

    fun deleteRecentSearching() = viewModelScope.launch(Dispatchers.IO + handleEx(mContext)) {
        mNikeDatabase.searchingDao().deleteAllSearch()
    }

    fun loadingShoesByName(name: String) = liveData(handleEx(mContext)) {
        try {
            Log.i(TAG, "loadingShoesByName: $name")
            val resource = mShoesRepository.getShoesByName(name, mSortAndFilter.value ?: SortAndFilter())
            mCurrentShoesList.value = resource.data
            emit(resource)
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "error"))
        }
    }

    fun getShoesType() = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading(null))
            emit(mShoesTypeRepository.getShoesType())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }


}


const val GENDER_ALL = 0
const val GENDER_MEN = 1
const val GENDER_WOMEN = 2

const val POPULAR = "Popular"
const val RECENT = "Recent"

const val MIN_VALUE = 50_000L
const val MAX_VALUE = 10_000_000L

data class SortAndFilter(
    var type: ShoesType = ShoesType(id = DEFAULT_SHOES_ID),
    var gender: Int = GENDER_ALL,
    var priceRange: Pair<Long, Long> = Pair(MIN_VALUE, MAX_VALUE),
    var sort: String = POPULAR,
    var star: Int = -1
)