package com.sq.thed_ck_licker.ecs.components.misc

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.Component

//this should be implemented after we refactor the card creations system
data class PriceComponent(private var price: MutableIntState):Component {
    constructor(price: Int = 50) : this(mutableIntStateOf(price))
}
