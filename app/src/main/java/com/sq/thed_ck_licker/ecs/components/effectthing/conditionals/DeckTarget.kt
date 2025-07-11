package com.sq.thed_ck_licker.ecs.components.effectthing.conditionals

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DeckHelper

data class DeckTarget(override val amount: Float, val effect: Effect) : ConditionalEffect() {
    override fun execute(context: EffectContext): Float {
        val targets = DeckHelper.getSubDeck(context.target, amount.toInt())
        for (target in targets) {
            val contextHack = context.copy(target = target)
            effect.executeCall(contextHack)
        }
        return 0f
    }
}
