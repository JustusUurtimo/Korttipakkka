package com.sq.thed_ck_licker.viewModels

import androidx.lifecycle.ViewModel
import com.sq.thed_ck_licker.ecs.systems.WitchSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WitchViewModel @Inject constructor(
    private val witchSystem: WitchSystem
) : ViewModel() {

    fun rejectOffering() {
        witchSystem.rejectOffering()
    }


}