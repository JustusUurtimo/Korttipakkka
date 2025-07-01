package com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DeckHelper
import com.sq.thed_ck_licker.helpers.MyRandom

/**
 * @param amount How many targets to heal
 */
data class HealEntitiesInDeckToFull(override val amount: Float) : HealthEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Heal to full ($modifiedAmount targets)"
    }

    override fun execute(context: EffectContext): Float {
        val deck = DeckHelper.getEntityFullDeck(context.target)

        var counter = 0
        val targets = this.amount
        deck.shuffle(MyRandom.random)
        deck.forEach {
            val healthComp = (it get HealthComponent::class)
            if (counter < targets && healthComp.getHealth() < healthComp.getMaxHealth() * 0.80) {
                healthComp.heal(healthComp.getMaxHealth())
                counter++
            }
        }
        return targets
    }
}
