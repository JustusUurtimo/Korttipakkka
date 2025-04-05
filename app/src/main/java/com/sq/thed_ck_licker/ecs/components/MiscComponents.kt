package com.sq.thed_ck_licker.ecs.components

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf

// TODO it is bit nonsensical that health starts from 0,
//  so it should be refactored to start from max health.

// TODO The hp starts from 0 bc the health bar is inverted at the moment,
//  if hp starts from 100 then hp bar will go from right to left ¯\_(ツ)_/¯ -Jupi
data class HealthComponent(var health: MutableFloatState, val maxHealth: MutableFloatState) {
    constructor(health: Float = 0f, maxHealth: Float = 100f) : this(
        mutableFloatStateOf(health),
        mutableFloatStateOf(maxHealth)
    )
}

data class ScoreComponent(var score: MutableIntState, val funkkari: () -> Unit ={}) {
    constructor(score: Int = 0) : this(mutableIntStateOf(score))
}