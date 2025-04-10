package com.sq.thed_ck_licker.ecs.components

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf

data class HealthComponent(var health: MutableFloatState, val maxHealth: MutableFloatState) {
    /**
     * This can be used in cases where you want thing to be not on full hp at the start
     */
    constructor(health: Float = 100f, maxHealth: Float = 100f) : this(
        mutableFloatStateOf(health),
        mutableFloatStateOf(maxHealth)
    )

    /**
     * This can be used to construct Health component with health same as max health
     */
    constructor(maxHealth: Float = 100f) : this(
        mutableFloatStateOf(maxHealth),
        mutableFloatStateOf(maxHealth)
    )
}

fun HealthComponent.addHealth(healthComponent: HealthComponent) {
    println("this is going to be modified ${this}")
    if (healthComponent.health.floatValue != 0f) {
        this.health.floatValue += healthComponent.health.floatValue
    } else if (healthComponent.maxHealth.floatValue != 0f) {
        this.maxHealth.floatValue += healthComponent.maxHealth.floatValue
    }
    println("and the end result is ${this}")
}

data class ScoreComponent(var score: MutableIntState) {
    constructor(score: Int = 0) : this(mutableIntStateOf(score))
}

fun ScoreComponent.addScore(scoreComponent: ScoreComponent) {
    this.score.intValue += scoreComponent.score.intValue
}
