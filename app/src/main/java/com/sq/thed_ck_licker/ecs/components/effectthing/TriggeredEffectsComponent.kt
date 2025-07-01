package com.sq.thed_ck_licker.ecs.components.effectthing

import com.sq.thed_ck_licker.ecs.components.Component
import com.sq.thed_ck_licker.helpers.MyRandom
import kotlin.reflect.KClass

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


    fun <T : Effect> findEffect(eff: KClass<T>): List<T> {
        val result = mutableListOf<T>()
        for (entry in effectsByTrigger) {
            for (effect in entry.value) {
                if (effect::class == eff) {
                    @Suppress("UNCHECKED_CAST")
                    result.add(effect as T)
                }
            }
        }
        return result
    }

    fun hasEffect(effect: KClass<Effect>): Trigger? {
        val effects = effectsByTrigger.values.flatten()
        for(i in effects.indices){
            if(effects[i]::class == effect){
                return effectsByTrigger.keys.toList()[i]
            }
        }
        return null
    }

    fun removeEffect(trigger: Trigger, effect: Effect): TriggeredEffectsComponent {
        val effects = effectsByTrigger[trigger]
        if (effects == null) return this

        val newEffects = effects.toMutableList()
        newEffects.remove(effect)

        val newMap = effectsByTrigger.toMutableMap()
        newMap[trigger] = newEffects
        return TriggeredEffectsComponent(newMap.toMap())
    }

    fun addEffects(trigger: Trigger, effects: List<Effect>): TriggeredEffectsComponent {
        val thing = effectsByTrigger.toMutableMap()
        val effectsForTrigger = thing[trigger]
        if (effectsForTrigger != null) {
            thing[trigger] = effectsForTrigger + effects
        } else {
            thing[trigger] = effects
        }
        return TriggeredEffectsComponent(thing.toMap())
    }

    fun shuffleTo(activeTriggers: Set<Trigger> = Trigger.duringPlayTriggers): TriggeredEffectsComponent {
        val values = effectsByTrigger.values.toMutableList()
        val size= values.size
        val result = mutableMapOf<Trigger, List<Effect>>()

        val activeTriggersShuffled = activeTriggers.toMutableList()
        activeTriggersShuffled.shuffle(MyRandom.random)

        for(i in 0 until size){
            result[activeTriggersShuffled[i]] = values[i]
        }
        return TriggeredEffectsComponent(result.toMap())
    }

}
