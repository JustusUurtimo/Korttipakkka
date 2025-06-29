package com.sq.thed_ck_licker.ecs.systems.cardSystems

import android.util.Log
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.components.Component
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.CardTag
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.misc.TickComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.CardConfig
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.generateCards
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.withBasicCardDefaults
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems
import com.sq.thed_ck_licker.helpers.DescribedEffect
import com.sq.thed_ck_licker.helpers.MyRandom.random
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import javax.inject.Inject
import kotlin.reflect.KClass

class CardCreationSystem @Inject constructor(
    private val cardCreationHelperSystems: CardCreationHelperSystems,
    private val cardBuilder: CardBuilderSystem,
    private val gameNavigator: GameNavigator
) {

    fun addShovelCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(name = "Shovel", hp = 10f)
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.Shovel)
        }
    }

    fun addBasicScoreCards(amount: Int, scoreSize: Int = 10): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Basic score card V4", hp = 100f, score = scoreSize
                )
            )(cardId)
            val score = (cardId get ScoreComponent::class).getScoreF()
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.GainScore(score))
        }
    }

    fun addHealingCards(amount: Int, healSize: Float = 40f): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    img = R.drawable.heal_10, name = "Heal", hp = 5f
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.GainHealth(healSize))
        }
    }

    fun addDamageCards(amount: Int, damageSize: Float = 150f): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    img = R.drawable.damage_6, name = "Damage", hp = 20f
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.TakeDamage(damageSize))
        }
    }

    fun addMerchantCards(amount: Int, merchantId: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Merchant #$merchantId",
                    characterId = merchantId,
                    tags = listOf(CardTag.CARD, CardTag.MERCHANT)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                Trigger.OnPlay,
                Effect.OpenMerchant(merchantId.toFloat(), gameNavigator)
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
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.TakeDamageOrGainMaxHP(10f))
        }
    }

    fun addBreakingDefaultCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Default Card", hp = 10f, score = 100
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.GainScoreFromScoreComp)
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
            cardId add TriggeredEffectsComponent(
                mutableMapOf(
                    Trigger.OnPlay to mutableListOf( //Not happy at all with this...
                        Effect.GainScoreFromScoreComp,
                        Effect.ResetSelfScore(),
                        Effect.ResetTakeRisingDamage(asd,asd)
                    ), Trigger.OnDeactivation to mutableListOf(
                        Effect.TakeRisingDamage(asd,asd),
                        Effect.StoreDamageDealtAsSelfScore
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
                    Trigger.OnPlay to mutableListOf(Effect.TakeRisingDamage(5f, 5f)),
                    Trigger.OnDeactivation to mutableListOf(Effect.TakeRisingScore(-30f, -30f)),
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
                Effect.AddScoreGainer(pointsPerCard.toFloat())
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
                Effect.AddBeerGoggles(healLimit)
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
                Effect.AddTempMultiplier(multiplier)
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
                        Effect.CorruptCards(efficiency.toFloat(),
                            DrawDeckComponent::class as KClass<Component>
                        )
                    ), Trigger.OnDeactivation to mutableListOf(
                        Effect.CorruptCards(efficiency.toFloat(),
                            DiscardDeckComponent::class as KClass<Component>
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
                    name = "Time Bound Card", hp = 123f, score = 10000
                )
            )(cardId)
            cardId add TickComponent(1000)
            cardId add TriggeredEffectsComponent(
                mutableMapOf(
                    Trigger.OnPlay to mutableListOf(
                        Effect.OnRepeatActivationGainScore(3f),
                        Effect.SelfAddEffectsToTrigger(Trigger.OnTick, listOf(Effect.TakeSelfDamage(1f)))
                    ),
                )
            )
        }
    }
}
