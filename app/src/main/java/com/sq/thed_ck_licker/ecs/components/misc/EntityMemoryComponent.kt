package com.sq.thed_ck_licker.ecs.components.misc

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.Component



data class EntityMemoryComponent(private var affinity: MutableIntState): Component {
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





