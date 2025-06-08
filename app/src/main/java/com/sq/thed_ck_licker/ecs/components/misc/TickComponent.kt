package com.sq.thed_ck_licker.ecs.components.misc

import com.sq.thed_ck_licker.helpers.DescribedEffect

data class TickComponent(
    var currentAmount: Int = 0,
    val tickThreshold: Int = 100,
    val tickAction: DescribedEffect
)
