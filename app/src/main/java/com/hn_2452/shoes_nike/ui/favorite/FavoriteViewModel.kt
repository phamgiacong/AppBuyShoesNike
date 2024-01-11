package com.hn_2452.shoes_nike.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.repository.AuthRepository
import com.hn_2452.shoes_nike.utility.Resource
import com.hn_2452.shoes_nike.utility.handleEx
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val mAuthRepository: AuthRepository,
    @ApplicationContext private val mContext: Context
) : ViewModel() {

    fun loadFavoriteShoesList() = liveData(handleEx(mContext)) {
        try {
            emit(Resource.loading(null))
            emit(mAuthRepository.getFavoriteShoesOfUser())
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message!!))
        }
    }

}