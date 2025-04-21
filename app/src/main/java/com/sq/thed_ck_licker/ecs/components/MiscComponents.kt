package com.sq.thed_ck_licker.ecs.components

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import kotlin.math.min

data class HealthComponent(
    var health: MutableFloatState,
    val maxHealth: MutableFloatState,
    var multiplier: Float = 1f
) {
    /**
     * This can be used in cases where you want thing to be not on full hp at the start
     */
    constructor(health: Float = 100f, maxHealth: Float = 100f, multiplier: Float = 1f) : this(
        mutableFloatStateOf(health),
        mutableFloatStateOf(maxHealth),
        multiplier
    )

    /**
     * This can be used to construct Health component with health same as max health
     */
    constructor(maxHealth: Float = 100f, multiplier: Float = 1f) : this(
        mutableFloatStateOf(maxHealth),
        mutableFloatStateOf(maxHealth),
        multiplier
    )

    fun timesMultiplier(f: Float) {
        this.multiplier *= f
    }

    fun heal(healAmount: Float) {
        val realAmount = healAmount * this.multiplier
        val toHealed = this.health.floatValue + realAmount
        this.health.floatValue = min(toHealed, this.maxHealth.floatValue)
    }

    fun removeMultiplier(f: Float) {
        this.multiplier *= 1 / f
    }
}

data class ScoreComponent(var score: MutableIntState) {
    constructor(score: Int = 0) : this(mutableIntStateOf(score))
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







