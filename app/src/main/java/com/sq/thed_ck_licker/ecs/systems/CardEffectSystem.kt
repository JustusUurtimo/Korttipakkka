package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.runtime.MutableFloatState
import com.sq.thed_ck_licker.ecs.components.CardEffect
import com.sq.thed_ck_licker.ecs.components.CardEffectType
import com.sq.thed_ck_licker.ecs.components.ImageComponent


// Systems
class CardEffectSystem {
    fun applyEffect(
        newCard: Triple<ImageComponent, CardEffect, Int>,
        playerHealth: MutableFloatState,
        reverseDamage: Boolean,
        doubleTrouble: Boolean
    ) {
        val effect = newCard.second
        when (effect.effectType) {
            CardEffectType.DAMAGE -> applyDamage(
                playerHealth,
                effect.effectValue.value,
                reverseDamage,
                doubleTrouble
            )

            CardEffectType.HEAL -> applyHeal(playerHealth, effect.effectValue.value, doubleTrouble)
            // Handle other effect types...
            else -> println("Unknown effect type")
        }
    }

    private fun applyDamage(
        playerHealth: MutableFloatState,
        amount: Float,
        reverseDamage: Boolean,
        doubleTrouble: Boolean
    ) {
        if (reverseDamage) {
            playerHealth.floatValue -= (amount)
        } else if (doubleTrouble) {
            playerHealth.floatValue += (amount * 2)
        } else {
            playerHealth.floatValue += (amount)
        }
    }

    private fun applyHeal(playerHealth: MutableFloatState, amount: Float, doubleTrouble: Boolean) {
        if (doubleTrouble) {
            playerHealth.floatValue -= (amount * 2)
        } else {
            playerHealth.floatValue -= (amount)
        }
    }
}