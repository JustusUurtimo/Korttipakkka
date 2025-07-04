package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.Tag
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeRisingDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeSelfDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects.AddBeerGoggles
import com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects.GainHealth
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.CorruptCards
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.OpenMerchant
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.SelfAddEffectsToTrigger
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.Shovel
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.TakeDamageOrGainMaxHP
import com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects.AddTempMultiplier
import com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects.AddScoreGainer
import com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects.GainScoreFromScoreComp
import com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects.OnRepeatActivationGainScore
import com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects.ResetSelfScore
import com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects.TakeRisingScore
import com.sq.thed_ck_licker.ecs.components.misc.TickComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.CardConfig
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.generateCards
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.withBasicCardDefaults
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import javax.inject.Inject

class CardCreationSystem @Inject constructor(
    private val gameNavigator: GameNavigator
) {

    fun addShovelCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(name = "Shovel", hp = 10f)
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Shovel)
        }
    }

    fun addBasicScoreCards(amount: Int, scoreSize: Int = 10): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Basic score card V4", hp = 100f, score = scoreSize
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, GainScoreFromScoreComp)
        }
    }

    fun addHealingCards(amount: Int, healSize: Float = 40f): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    img = R.drawable.heal_10, name = "Heal", hp = 5f
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, GainHealth(healSize))
        }
    }

    fun addDamageCards(amount: Int, damageSize: Float = 150f): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    img = R.drawable.damage_6, name = "Damage", hp = 20f
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, TakeDamage(damageSize))
        }
    }

    fun addMerchantCards(amount: Int, merchantId: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Merchant #$merchantId",
                    characterId = merchantId,
                    tags = listOf(Tag.CARD, Tag.MERCHANT)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                Trigger.OnPlay,
                OpenMerchant(merchantId.toFloat(), gameNavigator)
            )
        }
    }

    fun addMaxHpTrapCards(amount: Int = 5): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Max HP Trap Card", hp = 5f
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, TakeDamageOrGainMaxHP(10f))
        }
    }

    fun addBreakingDefaultCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Default Card", hp = 10f, score = 100
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, GainScoreFromScoreComp)
        }
    }

    fun addReallyBasicScoreCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Default Card", hp = 1000f, score = 1
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, GainScoreFromScoreComp)
        }
    }

    fun addDeactivationTestCards(amount: Int = 2, multiplier:Float = 30f): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Deactivation Card", hp = 10f, score = 0, multiplier = multiplier
                )
            )(cardId)

            val asd = 1/multiplier
            val risingDamage = TakeRisingDamage(asd, asd)
            cardId add TriggeredEffectsComponent(
                mutableMapOf(
                    Trigger.OnPlay to mutableListOf( //Not happy at all with this...
                        GainScoreFromScoreComp,
                        ResetSelfScore(),
                        risingDamage
                    ), Trigger.OnDeactivation to mutableListOf(
                        risingDamage
                    )
                )
            )

        }
    }

    fun addTrapTestCards(amount: Int = 1): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Trap card", hp = 50f, score = 1
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                mutableMapOf(
                    Trigger.OnPlay to mutableListOf(TakeRisingDamage(5f, 5f)),
                    Trigger.OnDeactivation to mutableListOf(TakeRisingScore(-30f, -30f)),
                )
            )
        }
    }

    fun addScoreGainerTestCards(amount: Int = 1, pointsPerCard: Int = 3): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Score Gainer Card", hp = 1f, score = pointsPerCard
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                Trigger.OnPlay,
                AddScoreGainer(pointsPerCard.toFloat())
            )
        }
    }

    fun addBeerGogglesTestCards(amount: Int = 1, healLimit: Float = 150f): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Beer Goggles Card", hp = 1f
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                Trigger.OnPlay,
                AddBeerGoggles(healLimit)
            )
        }
    }

    fun addTempMultiplierTestCards(amount: Int = 1, multiplier: Float = 3f): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Steroids", hp = 1f
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                Trigger.OnPlay,
                AddTempMultiplier(multiplier)
            )
        }
    }

    fun addShuffleTestCards(amount: Int = 1, efficiency: Int = 1): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    img = R.drawable.double_trouble, name = "Corrupt cards", hp = 3f
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                mutableMapOf(
                    Trigger.OnPlay to mutableListOf(
                        CorruptCards(
                            efficiency.toFloat(),
                            DrawDeckComponent::class
                        )
                    ), Trigger.OnDeactivation to mutableListOf(
                        CorruptCards(
                            efficiency.toFloat(),
                            DiscardDeckComponent::class
                        )
                    )
                )
            )
        }
    }

    fun addTimeBoundTestCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Time Bound Card", hp = 183f, score = 10000
                )
            )(cardId)
            cardId add TickComponent(1000)
            cardId add TriggeredEffectsComponent(
                mutableMapOf(
                    Trigger.OnPlay to mutableListOf(
                        OnRepeatActivationGainScore(3f),
                        SelfAddEffectsToTrigger(Trigger.OnTick, listOf(TakeSelfDamage(1f)))
                    ),
                )
            )
        }
    }
}
