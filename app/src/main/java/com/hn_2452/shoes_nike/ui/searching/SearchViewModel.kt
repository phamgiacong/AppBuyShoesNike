package com.hn_2452.shoes_nike.ui.searching

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mShoesRepository: ShoesRepository,
    private val mShoesTypeRepository: ShoesTypeRepository,
    private val mNikeDatabase: NikeDatabase
) : ViewModel() {

    var mShoesTypeList: List<ShoesType> = emptyList()

    var mStarList: List<Int> = mutableListOf(-1, 5, 4, 3, 2, 1, 0)

    val mSortAndFilter = MutableLiveData(SortAndFilter())

    var mCurrentShoesList : List<Shoes> = emptyList()

    companion object {
        private const val TAG = "Nike:SearchViewModel: "
    }

    val mRecentSearching: LiveData<List<Searching>> = mNikeDatabase.searchingDao().getAllSearching()

    fun addRecentSearching(content: String) = viewModelScope.launch(Dispatchers.IO) {
        val searching = Searching(content)
        mNikeDatabase.searchingDao().addSearching(searching)
    }

    fun deleteRecentSearching(searching: Searching) = viewModelScope.launch(Dispatchers.IO) {
        mNikeDatabase.searchingDao().deleteSearching(searching)
    }

    fun deleteRecentSearching() = viewModelScope.launch(Dispatchers.IO) {
        mNikeDatabase.searchingDao().deleteAllSearch()
    }

    fun loadingShoesByName(name: String) = liveData {
        try {
            Log.i(TAG, "loadingShoesByName: $name")
            emit(mShoesRepository.getShoesByName(name, mSortAndFilter.value ?: SortAndFilter()))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message ?: "error"))
        }
    }

    fun getShoesType() = liveData {
        try {
            emit(Resource.loading(null))
            emit(mShoesTypeRepository.getShoesType())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }


}

class SearchViewModelFactory(
    private val mShoesRepository: ShoesRepository,
    private val mShoesTypeRepository: ShoesTypeRepository,
    private val mNikeDatabase: NikeDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(mShoesRepository, mShoesTypeRepository, mNikeDatabase) as T
        }
        return super.create(modelClass)
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
    var type: String = DEFAULT_SHOES_ID,
    var gender: Number = GENDER_ALL,
    var priceRange: Pair<Long, Long> = Pair(MIN_VALUE, MAX_VALUE),
    var sort: String = POPULAR,
    var star: Int = -1
)