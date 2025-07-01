package com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects

import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers
import com.sq.thed_ck_licker.helpers.MyRandom
import kotlin.reflect.KClass

data class CorruptCards(override val amount: Float, val targetDeck: KClass<*>) : MiscEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Corrupt $modifiedAmount card(s) in ${targetDeck.simpleName}"
    }

    override fun execute(context: EffectContext): Float {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val efficiency = (this.amount * sourceMulti * targetMulti).toInt()
        val target = (context.target get this.targetDeck)
        val deck = when (target) {
            is DrawDeckComponent -> {
                var thing = target.getDrawCardDeck()
                if (thing.isEmpty()) {
                    thing = (context.target get DiscardDeckComponent::class).getDiscardDeck()
                }
                thing
            }

            is DiscardDeckComponent -> {
                var thing = target.getDiscardDeck()
                if (thing.isEmpty()) {
                    thing = (context.target get DrawDeckComponent::class).getDrawCardDeck()
                }
                thing
            }

            else -> {
                error("Unknown target deck type")
            }
        }
        repeat(efficiency) {
            val card = deck.removeAt(MyRandom.random.nextInt(deck.size))
            val trigEffComp = card get TriggeredEffectsComponent::class
            val corruptedTriggeredEffect = trigEffComp.shuffleTo()
            card add corruptedTriggeredEffect
            val tags = card get TagsComponent::class
            tags.addTag(TagsComponent.Tag.CORRUPTED)
            deck.add(card)
        }
        return efficiency.toFloat()
    }
}
