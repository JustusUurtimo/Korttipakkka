package com.sq.thed_ck_licker.ecs.components.misc

data class TickComponent(
    var currentAmount: Int = 0,
    val tickThreshold: Int = 100,
    val tickAction: (Int) -> Unit
)
