package com.sq.thed_ck_licker.ecs.components

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf

// TODO it is bit nonsensical that health starts from 0,
//  so it should be refactored to start from max health
data class HealthComponent(var health: MutableFloatState, val maxHealth: MutableFloatState) {
    constructor(health: Float = 0f, maxHealth: Float = 100f) : this(
        mutableFloatStateOf(health),
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


//fun ScoreComponent.plus(scoreC: ScoreComponent) {
//    this.score.intValue += scoreC.score.intValue
//}

//fun ScoreComponent.plus(entityId: Int, components: ComponentManager) {
//    val incoming = components.getComponent(entityId, ScoreComponent::class).score.intValue
//    this.score.intValue += incoming
//}