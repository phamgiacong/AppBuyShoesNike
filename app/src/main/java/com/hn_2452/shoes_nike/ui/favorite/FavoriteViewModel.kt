package com.hn_2452.shoes_nike.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.ShoesRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val mShoesRepository: ShoesRepository
) : ViewModel() {

    fun loadFavoriteShoesList() = liveData {
        try {
            emit(Resource.loading(null))
            emit(mShoesRepository.getPopularShoes())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

}

class FavoriteViewModelFactory(
    private val mShoesRepository: ShoesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(mShoesRepository) as T
        }
        return super.create(modelClass)
    }
}