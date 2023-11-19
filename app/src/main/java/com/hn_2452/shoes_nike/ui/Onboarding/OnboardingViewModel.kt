package com.hn_2452.shoes_nike.ui.Onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Onboarding

class OnboardingViewModel : ViewModel() {

    var onboardingData = MutableLiveData<List<Onboarding>>()

    init {

        var list = mutableListOf(
            Onboarding(R.drawable.welcome1, "We provide high \n quality products just \n for you"),
            Onboarding(R.drawable.welcome2, "We provide high \n quality products just \n for you"),
            Onboarding(R.drawable.welcome2, "We provide high \n quality products just \n for you"),
        )

        onboardingData.value = list
    }
}