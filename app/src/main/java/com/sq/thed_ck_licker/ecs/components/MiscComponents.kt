package com.sq.thed_ck_licker.ecs.components

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID


data class HealthComponent(private var health: MutableFloatState, private val maxHealth: MutableFloatState) {
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
    fun getHealth(): Float {
        return this.health.floatValue
    }
    fun getMaxHealth(): Float {
        return this.maxHealth.floatValue
    }
    fun increaseMaxHealth(amount: Float) {
        this.maxHealth.floatValue += amount
    }

    fun heal(healAmount: Float) {
        println("this is going to be modified ${this}")
        if ((this.health.floatValue + healAmount) > this.maxHealth.floatValue) {
            this.health.floatValue = this.maxHealth.floatValue
        } else {
            this.health.floatValue += healAmount
        }
        println("and the end result is ${this}")
    }

    fun damage(damageAmount: Float, targetId: Int) {
        this.health.floatValue -= damageAmount
        if (targetId == getPlayerID() && this.health.floatValue <= 0) {
            GameEvents.onPlayerDied.tryEmit(Unit)
        }
    }
}

data class ScoreComponent(var score: MutableIntState) {
    constructor(score: Int = 0) : this(mutableIntStateOf(score))
}

fun ScoreComponent.addScore(scoreComponent: ScoreComponent) {
    this.score.intValue += scoreComponent.score.intValue
}

data class MerchantComponent(
    var merchantId: MutableIntState,
    val activeMerchantSummonCard: MutableIntState
) {
    constructor(merchantId: Int = -1, activeMerchantSummonCard: Int = -1) : this(
        mutableIntStateOf(
            merchantId
        ), mutableIntStateOf(activeMerchantSummonCard)
    )
}

//this should be implemented after we refactor the card creations system
data class CardPriceComponent(var price: MutableIntState) {
    constructor(price: Int = 50) : this(mutableIntStateOf(price))
}





