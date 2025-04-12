package com.sq.thed_ck_licker.ecs.components

import android.os.Parcelable
import android.util.MutableBoolean
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import kotlinx.parcelize.Parcelize

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


data class MerchantComponent(var merchantId: MutableIntState, var cardsInMerchantsHand: MutableList<Int>) {
    constructor(merchantId: Int = -1, cardsInMerchantsHand: MutableList<Int> = mutableListOf()) : this(mutableIntStateOf(merchantId), cardsInMerchantsHand)
}

fun MerchantComponent.addCardToMerchantHand(cardId: Int) {
    this.cardsInMerchantsHand.add(cardId)
}

fun MerchantComponent.removeAllCardsFromMerchantHand() {
    this.cardsInMerchantsHand.removeAll(this.cardsInMerchantsHand)
}

fun MerchantComponent.getCardsInMerchantHand(): List<Int> {
    return this.cardsInMerchantsHand.toList()
}

//this should be implemented after we refactor the card creations system
data class CardPriceComponent(var price: MutableIntState) {
    constructor(price: Int = 50) : this(mutableIntStateOf(price))
}





