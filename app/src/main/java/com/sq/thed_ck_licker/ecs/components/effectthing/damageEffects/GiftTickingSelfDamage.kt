package com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.TickComponent
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.getOrNull
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DeckHelper
import kotlin.math.max

data class GiftTickingSelfDamage(override val amount: Float) : DamageEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Gift (${modifiedAmount?.toInt()}) targets with curse of time "
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val amount = (this.amount * sourceMulti * targetMulti)

        val subDeck = DeckHelper.getSubDeck(context.target, amount.toInt())

        subDeck.forEach { card ->
            val trigComp = card.getOrNull<TriggeredEffectsComponent>()
            check(trigComp != null) { "This card $card is missing all TrigComp things?" }
            card add (trigComp.addEffects(Trigger.OnTick, listOf(TakeSelfDamage(1f)))).removeNone()

            var tickComp: TickComponent? = card.getOrNull<TickComponent>()

            if (tickComp == null) {
                tickComp = TickComponent(1000)
            } else {
                // luls: min(max(tickComp.tickThreshold -100, 100), 1000)
                val hold = max(tickComp.tickThreshold - 100, 100)
                tickComp = TickComponent(hold)
            }
            card add tickComp

        }
        return amount
    }

}
