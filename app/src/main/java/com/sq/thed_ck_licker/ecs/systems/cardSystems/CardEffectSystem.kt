package com.sq.thed_ck_licker.ecs.systems.cardSystems

import androidx.compose.runtime.MutableFloatState
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.components.CardEffect
import com.sq.thed_ck_licker.ecs.components.CardEffectType
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.addHealth
import com.sq.thed_ck_licker.ecs.components.addScore


// Systems
class CardEffectSystem(private val componentManager: ComponentManager) {

    companion object {
        val instance: CardEffectSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CardEffectSystem(ComponentManager.componentManager)
        }
    }


    @Deprecated("Should be done via onPlay invoke")
    fun applyEffect(
        newCard: Pair<ImageComponent, CardEffect>,
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


    @Deprecated("Should be done via onPlay invoke")
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

    @Deprecated("Should be done via onPlay invoke")
    private fun applyHeal(playerHealth: MutableFloatState, amount: Float, doubleTrouble: Boolean) {
        if (doubleTrouble) {
            playerHealth.floatValue -= (amount * 2)
        } else {
            playerHealth.floatValue -= (amount)
        }
    }

    @Deprecated("Should be done via onPlay invoke")
    private fun activateThing(theActivator: Int, theUsedThing: Int, theTarget: Int) {
        val kohdeComponents = componentManager.getAllComponentsOfEntity(theUsedThing)

        for (component in kohdeComponents) {
            when (component) {
                is ScoreComponent -> activateScore(theActivator, theUsedThing, theTarget)
                is HealthComponent -> activateHealth(theActivator, theUsedThing, theTarget)
                else -> println("Unknown component type: $component")
            }
        }
    }

    @Deprecated("Should be done via onPlay invoke")
    private fun activateScore(theActivator: Int, theUsedThing: Int, theTarget: Int) {
        componentManager.getComponent(theActivator, ScoreComponent::class)

        val kohde = componentManager.getComponent(theTarget, ScoreComponent::class)

        val tehtava = componentManager.getComponent(theUsedThing, ScoreComponent::class)
        kohde.addScore(tehtava)
    }

    @Deprecated("Should be done via onPlay invoke")
    private fun activateHealth(theActivator: Int, theUsedThing: Int, theTarget: Int) {

        val kohde = componentManager.getComponent(theTarget, HealthComponent::class)

        val tehtava = componentManager.getComponent(theUsedThing, HealthComponent::class)

        kohde.addHealth(tehtava)
    }

    @Deprecated("Should be done via onPlay invoke")
    fun playerTargetsPlayer(theUsedThingId: Int) {
        return activateThing(getPlayerID(), theUsedThingId, getPlayerID())
    }

}