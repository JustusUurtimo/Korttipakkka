package com.sq.thed_ck_licker.ecs.components

import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.helpers.DescribedEffect
import com.sq.thed_ck_licker.helpers.MyRandom.random
import com.sq.thed_ck_licker.helpers.combine

/**
 * Used to make more complex activations
 * The functions take the target entity id as a parameter.
 * Then you just make what you want in the function body.
 * And maaaagic
 */
data class EffectComponent(
    /**
     * When something dies, it will trigger this at the end of everything.
     */
    val onDeath: DescribedEffect = DescribedEffect.EMPTY,
    /**
     *  Huh, not actually used currently...
     *  TODO: Remove? or make useful somewhere
     */
    val onSpawn: DescribedEffect = DescribedEffect.EMPTY,
    /**
     *  When the turn starts, it will trigger this.
     *  This happens as the first thing, even before player card.
     *  TODO: Currently only happens for things inside effect stack.
     */
    val onTurnStart: DescribedEffect = DescribedEffect.EMPTY,
    /**
     *  When the card is played, it will trigger this.
     *  Nice and simple when player activates thing, it will trigger this.
     */
    val onPlay: DescribedEffect = DescribedEffect.EMPTY,
    /**
     *  When player chooses not to activate the card, it will trigger this.
     */
    val onDeactivate: DescribedEffect = DescribedEffect.EMPTY,
    /**
     *  There is no normal way of triggering this.
     *  This should be used for things like on being buried in to the pit.
     */
    val onSpecial: DescribedEffect = DescribedEffect.EMPTY
) {
    fun combineEffectComponents(other: EffectComponent): EffectComponent {
        return EffectComponent(
            onPlay = this.onPlay.combine(other.onPlay),
            onDeath = this.onDeath.combine(other.onDeath),
            onSpawn = this.onSpawn.combine(other.onSpawn),
            onTurnStart = this.onTurnStart.combine(other.onTurnStart),
            onDeactivate = this.onDeactivate.combine(other.onDeactivate),
            onSpecial = this.onSpecial.combine(other.onSpecial)
        )
    }

    fun shuffleToNew(): EffectComponent {
        val effectHandlers = mutableListOf<DescribedEffect>()
        effectHandlers.add(onDeath)
        effectHandlers.add(onSpawn)
        effectHandlers.add(onTurnStart)
        effectHandlers.add(onPlay)
        effectHandlers.add(onDeactivate)
        if (onSpecial != DescribedEffect.EMPTY) {
            effectHandlers.add(onSpecial)
        }

        return EffectComponent(
            onDeath = effectHandlers.removeAt(random.nextInt(effectHandlers.size)),
            onSpawn = effectHandlers.removeAt(random.nextInt(effectHandlers.size)),
            onTurnStart = effectHandlers.removeAt(random.nextInt(effectHandlers.size)),
            onPlay = effectHandlers.removeAt(random.nextInt(effectHandlers.size)),
            onDeactivate = effectHandlers.removeAt(random.nextInt(effectHandlers.size))
        )
    }


    fun describeTriggers(targetId: Int = getPlayerID()): List<String> {
        val lines = mutableListOf<String>()

        fun handle(label: String, effect: DescribedEffect) {
            val text = effect.describe(targetId).trim()
            if (text.isNotBlank()) {
                lines.add("$label: $text")
            }
        }

        handle("On Death", onDeath)
        handle("On Spawn", onSpawn)
        handle("On Turn Start", onTurnStart)
        handle("On Play", onPlay)
        handle("On Deactivate", onDeactivate)
        handle("On Special", onSpecial)

        return lines
    }

    override fun toString(): String {
        return this.describeTriggers().joinToString("\n")
    }
}

