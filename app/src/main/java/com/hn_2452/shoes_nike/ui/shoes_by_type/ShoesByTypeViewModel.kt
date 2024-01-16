package com.hn_2452.shoes_nike.ui.shoes_by_type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.data.repository.ShoesRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShoesByTypeViewModel @Inject constructor(
    private val mShoesRepository: ShoesRepository
) : ViewModel() {

    var mShoesList: List<Shoes>? = emptyList()

    var mSort = -1
    var mNeedToLoadOldData = false

    fun loadShoesListByType(type: String) = liveData {
        try {
            emit(Resource.loading(null))
            emit(mShoesRepository.getShoesByType(type))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }


}