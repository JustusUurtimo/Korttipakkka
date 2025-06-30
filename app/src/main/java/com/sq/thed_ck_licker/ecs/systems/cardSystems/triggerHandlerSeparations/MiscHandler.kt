package com.sq.thed_ck_licker.ecs.systems.cardSystems.triggerHandlerSeparations

import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.MerchantEvent
import com.sq.thed_ck_licker.ecs.managers.MerchantEvents
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.getMultipliers
import com.sq.thed_ck_licker.helpers.MyRandom
import com.sq.thed_ck_licker.helpers.navigation.Screen

object MiscHandler {

    fun applyCorruptCardsEffect(
        effect: Effect.CorruptCards, context: EffectContext
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val efficiency = (effect.amount * sourceMulti * targetMulti).toInt()
        val target = (context.target get effect.targetDeck)
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
                return
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
        return
    }


    fun openMerchant(
        effect: Effect.OpenMerchant, context: EffectContext
    ) {
        MerchantEvents.tryEmit(
            MerchantEvent.MerchantShopOpened(
                effect.amount.toInt(), context.source
            )
        )
        effect.gameNavigator.navigateTo(Screen.MerchantShop.route)
    }

    fun applyDamageOrBoostMaxHp(
        context: EffectContext, effect: Effect.TakeDamageOrGainMaxHP
    ) {
        val (sourceMulti, targetMulti) = getMultipliers(context)
        val destiny = MyRandom.getRandomInt()
        if (destiny <= 1) {
            val healthComp = (context.target get HealthComponent::class)
            val amount = healthComp.getHealth() * (0.5 * sourceMulti * targetMulti).toFloat()
            healthComp.damage(amount)

            (context.source get HealthComponent::class).kill()
        } else {
            val healthComp = (context.target get HealthComponent::class)
            val amount = (effect.amount * sourceMulti * targetMulti).toFloat()
            healthComp.increaseMaxHealth(amount)
        }
    }

    fun addEffectsToSourceTrigger(
        context: EffectContext,
        effect: Effect.SelfAddEffectsToTrigger
    ) {
        context.source add (context.source get TriggeredEffectsComponent::class).addEffects(
            effect.trigger,
            effect.effects
        )
    }
}