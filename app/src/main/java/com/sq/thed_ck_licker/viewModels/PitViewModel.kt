package com.sq.thed_ck_licker.viewModels

import androidx.lifecycle.ViewModel
import com.sq.thed_ck_licker.ecs.systems.PitSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PitViewModel @Inject constructor(
    private val pitSystem: PitSystem,
) : ViewModel() {

    fun dropCardInPit(chosenCard: Int) {
        pitSystem.dropCardInPit(chosenCard)
    }

}