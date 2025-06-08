package com.sq.thed_ck_licker.ecs.states

data class PlayerState(
    val health: Float,
    val maxHealth: Float,
    val score: Int,
    val latestCard: Int,
)