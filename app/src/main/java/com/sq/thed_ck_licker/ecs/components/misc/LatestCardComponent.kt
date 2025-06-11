package com.sq.thed_ck_licker.ecs.components.misc

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf

data class LatestCardComponent(
    private var latestCard: MutableIntState = mutableIntStateOf(-1)) {
    constructor(value: Int) : this(mutableIntStateOf(value))

    fun getLatestCard(): Int {
        return this.latestCard.intValue
    }

    fun setLatestCard(cardId: Int) {
        this.latestCard.intValue = cardId
    }
}
