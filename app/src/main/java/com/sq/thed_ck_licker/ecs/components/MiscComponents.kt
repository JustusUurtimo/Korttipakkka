package com.sq.thed_ck_licker.ecs.components

import android.util.Log
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.GameEvent


data class HealthComponent(
    private var health: MutableFloatState,
    private val maxHealth: MutableFloatState
) {
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
        var logging = "this is going to be modified $this"
        if ((this.health.floatValue + healAmount) > this.maxHealth.floatValue) {
            this.health.floatValue = this.maxHealth.floatValue
        } else {
            this.health.floatValue += healAmount
        }
        logging += "and the end result is $this"
        Log.i("HealthComponent", logging)
    }

    fun damage(damageAmount: Float, targetId: Int) {
        this.health.floatValue -= damageAmount
        if (targetId == getPlayerID() && this.health.floatValue <= 0) {
            GameEvents.tryEmit(GameEvent.PlayerDied)
        }
    }
}

data class ScoreComponent(private var score: MutableIntState) {
    constructor(score: Int = 0) : this(mutableIntStateOf(score))

    fun addScore(score: Int) {
        this.score.intValue += score
    }

    fun reduceScore(score: Int) {
        this.score.intValue -= score
    }

    fun getScore(): Int {
        return this.score.intValue
    }

    fun setScore(score: Int) {
        this.score.intValue = score
    }

}


data class MerchantComponent(
    private var merchantId: MutableIntState,
    private val activeMerchantSummonCard: MutableIntState
) {
    constructor(merchantId: Int = -1, activeMerchantSummonCard: Int = -1) : this(
        mutableIntStateOf(
            merchantId
        ), mutableIntStateOf(activeMerchantSummonCard)
    )

    fun getMerchantId(): Int {
        return this.merchantId.intValue
    }

    fun getActiveMerchantSummonCard(): Int {
        return this.activeMerchantSummonCard.intValue
    }

    fun setMerchantId(merchantId: Int) {
        this.merchantId.intValue = merchantId
    }

    fun setActiveMerchantSummonCard(activeMerchantSummonCard: Int) {
        this.activeMerchantSummonCard.intValue = activeMerchantSummonCard
    }
}
//this should be implemented after we refactor the card creations system
data class CardPriceComponent(private var price: MutableIntState) {
    constructor(price: Int = 50) : this(mutableIntStateOf(price))
}

data class EntityMemoryComponent(private var affinity: MutableIntState) {
    constructor(affection: Int = 0) : this(mutableIntStateOf(affection))

    fun getAffinity(): Int {
        return this.affinity.intValue
    }
    fun setAffinity(affection: Int) {
        this.affinity.intValue = affection
    }
    fun updateAffinity(amount: Int) {
        this.affinity.intValue += amount
    }
}






