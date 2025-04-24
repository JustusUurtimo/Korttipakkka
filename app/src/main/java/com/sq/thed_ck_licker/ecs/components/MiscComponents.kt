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
     * and multiplier of 1
     */
    constructor(maxHealth: Float = 100f) : this(
        mutableFloatStateOf(maxHealth),
        mutableFloatStateOf(maxHealth)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HealthComponent

        if (multiplier != other.multiplier) return false
        if (health.floatValue != other.health.floatValue) return false
        if (maxHealth.floatValue != other.maxHealth.floatValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = multiplier.hashCode()
        result = 31 * result + health.floatValue.hashCode()
        result = 31 * result + maxHealth.floatValue.hashCode()
        return result
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







