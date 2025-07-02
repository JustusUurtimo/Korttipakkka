package com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get

data class SelfAddEffectsToTrigger(val trigger: Trigger, var effects: List<Effect>) : MiscEffect() {

    override fun execute(context: EffectContext): Float {
        context.source add (context.source get TriggeredEffectsComponent::class)
            .addEffects(
                this.trigger, this.effects
            )
        return 0f
    }
}