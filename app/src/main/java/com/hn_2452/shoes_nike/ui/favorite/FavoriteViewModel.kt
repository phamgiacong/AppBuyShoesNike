package com.hn_2452.shoes_nike.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository
) : ViewModel() {

    fun loadFavoriteShoesList() = liveData {
        try {
            emit(Resource.loading(null))
            emit(mAuthRepository.getFavoriteShoesOfUser())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

}