package com.sq.thed_ck_licker.ecs.components

import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.helpers.DescribedEffect
import com.sq.thed_ck_licker.helpers.MyRandom.random

/**
 * Used to make more complex activations
 * The functions take the target entity id as a parameter.
 * Then you just make what you want in the function body.
 * And maaaagic
 */
@Deprecated("")
data class EffectComponent(
    /**
     * When something dies, it will trigger this at the end of everything.
     */
    val onDeath: DescribedEffect = DescribedEffect.EMPTY,
    /**
     * Used when the card is drawn.
     * This really really easy to trigger multiple times in succession.
     * So the effects should be kind a mild.
     */
    val onDraw: DescribedEffect = DescribedEffect.EMPTY,
    /**
     *  When the turn starts, it will trigger this.
     *  This happens as the first thing, even before player card.
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
            onDraw = this.onDraw.combine(other.onDraw),
            onTurnStart = this.onTurnStart.combine(other.onTurnStart),
            onDeactivate = this.onDeactivate.combine(other.onDeactivate),
            onSpecial = this.onSpecial.combine(other.onSpecial)
        )
    }

    fun shuffleToNew(): EffectComponent {
        val effectHandlers = mutableListOf<DescribedEffect>()
        effectHandlers.add(onDeath)
        effectHandlers.add(onDraw)
        effectHandlers.add(onTurnStart)
        effectHandlers.add(onPlay)
        effectHandlers.add(onDeactivate)
        effectHandlers.add(onSpecial)
        effectHandlers.shuffle(random)
        return EffectComponent(
            onDeath = effectHandlers.removeAt(0),
            onDraw = effectHandlers.removeAt(0),
            onTurnStart = effectHandlers.removeAt(0),
            onPlay = effectHandlers.removeAt(0),
            onDeactivate = effectHandlers.removeAt(0),
            onSpecial = effectHandlers.removeAt(0)
        )
    }


    fun describeTriggers(targetId: EntityId = getPlayerID()): List<String> {
        val lines = mutableListOf<String>()

        fun handle(label: String, effect: DescribedEffect) {
            val text = effect.describe(targetId).trim()
            if (text.isNotBlank()) {
                lines.add("$label: $text")
            }
        }

        handle("On Death", onDeath)
        handle("On Drawn", onDraw)
        handle("On Turn Start", onTurnStart)
        handle("On Play", onPlay)
        handle("On Deactivate", onDeactivate)
        handle("On Special", onSpecial)

        return lines
    }

    override fun toString(): String {
        return this.describeTriggers().joinToString("\n")
    }

    fun getNonEmptyEffects(): List<DescribedEffect> {
        val thing = mutableListOf<DescribedEffect>()
        if (onDeath != DescribedEffect.EMPTY) thing.add(onDeath)
        if (onDraw != DescribedEffect.EMPTY) thing.add(onDraw)
        if (onTurnStart != DescribedEffect.EMPTY) thing.add(onTurnStart)
        if (onPlay != DescribedEffect.EMPTY) thing.add(onPlay)
        if (onDeactivate != DescribedEffect.EMPTY) thing.add(onDeactivate)
        if (onSpecial != DescribedEffect.EMPTY) thing.add(onSpecial)
        return thing
    }
}

