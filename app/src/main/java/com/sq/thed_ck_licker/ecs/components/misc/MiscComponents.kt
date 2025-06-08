package com.sq.thed_ck_licker.ecs.components.misc

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf

//this should be implemented after we refactor the card creations system
data class CardPriceComponent(private var price: MutableIntState) {
    constructor(price: Int = 50) : this(mutableIntStateOf(price))
}

data class EntityMemoryComponent(private var affinity: MutableIntState) {
    constructor(affection: Int = 0) : this(mutableIntStateOf(affection))

    fun getAffinity(): Int {
        return this.affinity.intValue
    }
    fun setAffinity(affection: Int) {
        this.affinity.intValue = affection
    }
    fun updateAffinity(amount: Int) {
        this.affinity.intValue += amount
    }
}





