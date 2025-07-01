package com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.getOrNull
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DeckHelper
import com.sq.thed_ck_licker.helpers.MyRandom

data class GiftEffects(
    override val amount: Float,
    val trigger: Trigger?,
    val effects: List<Effect>,
    val useAllEffects: Boolean = false
) : MiscEffect() {
    override fun describe(modifiedAmount: Float?): String? {
        return "Gift ${effects.size} Effects to $modifiedAmount"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val amount = (this.amount * sourceMulti * targetMulti)
        val deck = DeckHelper.getSubDeck(context.target, amount.toInt())
        val trigger = this.trigger ?: Trigger.duringPlayTriggers.random(MyRandom.random)
        deck.forEach { card ->
            val trigComp = card.getOrNull<TriggeredEffectsComponent>()
            check(trigComp != null) { "This card $card is missing all TrigComp things? With love from Gift Effects." }
            val effects2 =
                if (useAllEffects) this.effects else listOf(this.effects.random(MyRandom.random))
            /*
             * TODO: When we want we should add the nice way of quarantining the requirement safety
             *  Described here: https://chatgpt.com/s/t_68641ed0f0cc8191b5e0aea42884a6af
             *  The First one is nice feeling
             */
            card add trigComp.addEffects(trigger, effects2).removeNone()
        }
        return amount
    }

}
