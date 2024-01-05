package com.hn_2452.shoes_nike.ui.review_shoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hn_2452.shoes_nike.data.model.ShoesReview
import com.hn_2452.shoes_nike.data.repository.ShoesReviewRepository
import com.hn_2452.shoes_nike.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShoesReviewViewModel @Inject constructor(
    private val mShoesReviewRepository: ShoesReviewRepository
) : ViewModel() {

    var mStarList: List<Int> = mutableListOf(-1, 5, 4, 3, 2, 1, 0)
    var mReviewList : List<ShoesReview>? = mutableListOf()

    fun getShoesReview(shoesId: String) = liveData {
        try {
            emit(Resource.loading())
            emit(mShoesReviewRepository.getShoesReview(shoesId))
        } catch (ex: Exception) {
            emit(Resource.error(message = ex.message!!))
        }
    }

}