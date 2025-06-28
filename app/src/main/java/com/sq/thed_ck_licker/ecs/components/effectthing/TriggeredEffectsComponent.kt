package com.sq.thed_ck_licker.ecs.components.effectthing

import com.sq.thed_ck_licker.ecs.components.Component

/**
 * These two are functionally identical:
 * cardId add TriggeredEffectsComponent(mutableMapOf(Trigger.OnPlay to mutableListOf(Effect.TakeDamage(damageAmount))))
 * and
 * cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.TakeDamage(damageAmount))
 *
 * You can also go wild like:
 * cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.TakeDamage(damageAmount), Effect.GainHealth(healAmount))
 */
data class TriggeredEffectsComponent(
    val effectsByTrigger: Map<Trigger, List<Effect>> = mapOf()
) : Component {
    /**
     * This constructor is perfect when you want to have one or more effects that are tied to one trigger
     */
    constructor(trigger: Trigger = Trigger.OnPlay, vararg effects: Effect) : this(
        mutableMapOf(trigger to effects.toList())
    )
}
