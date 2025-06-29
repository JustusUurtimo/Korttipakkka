package com.sq.thed_ck_licker.ecs.components.misc

import com.sq.thed_ck_licker.ecs.components.Component

data class TickComponent(
    var currentAmount: Int = 0,
    val tickThreshold: Int = 100
) : Component
