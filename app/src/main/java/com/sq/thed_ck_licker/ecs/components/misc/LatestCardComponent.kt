package com.sq.thed_ck_licker.ecs.components.misc

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.Component

data class LatestCardComponent(
    private var latestCard: MutableIntState = mutableIntStateOf(-1)):Component {

    fun getLatestCard(): Int {
        return this.latestCard.intValue
    }

    fun setLatestCard(cardId: Int) {
        this.latestCard.intValue = cardId
    }
}
