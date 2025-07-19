package com.sq.thed_ck_licker.ecs.managers.locationmanagers

import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.components.Card
import com.sq.thed_ck_licker.ecs.components.Forest
import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.OnCreation
import com.sq.thed_ck_licker.ecs.components.effectthing.OnDeath
import com.sq.thed_ck_licker.ecs.components.effectthing.OnPlay
import com.sq.thed_ck_licker.ecs.components.effectthing.OnSpecial
import com.sq.thed_ck_licker.ecs.components.effectthing.OnTurnStart
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.conditionals.OnThresholdActivations
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.DealDamageFromOwnHealth
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.GiftTickingSelfDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeSelfDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeSelfPercentageDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects.HealEntitiesInDeckToFull
import com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects.MultiplyMaxHp
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.CoActivation
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.GiftEffects
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.ManyEffectsHolder
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.None
import com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects.AddFlatMultiplier
import com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects.AddMultiplier
import com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects.AddTempMultiplierToCardsInDeck
import com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects.GiveCardInDeckMultiplier
import com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects.RemoveFlatMultiplier
import com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects.RemoveMultiplier
import com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects.GainScoreFromScoreComp
import com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects.GainSelfHpAsScore
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.CardConfig
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.generateCards
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.withBasicCardDefaults
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler
import com.sq.thed_ck_licker.helpers.MyRandom

object ForestManager {
    val positiveForestEffects = listOf(
        HealEntitiesInDeckToFull(5f),
        MultiplyMaxHp(2f),
        GainSelfHpAsScore(0.25f),
        GiveCardInDeckMultiplier(3.5f),
        AddTempMultiplierToCardsInDeck(amount = 5f, size = 2f),
        CoActivation(
            newSource = null,
            OnPlay
        )
    )
    val negativeForestEffects = listOf(
        TakeSelfPercentageDamage(0.10f),
        GiftTickingSelfDamage(amount = 4f),
        DealDamageFromOwnHealth(amount = 0.1f)
    )

    var fuq = true

    fun getForestPackage(targetId: EntityId): MutableList<EntityId> {
        val list = mutableListOf<EntityId>()

        // For some reason, looking at you hilt..., we init before game starts and then init again after game starts.
        //So this needs to be here to block getting 2 of these...
        if (fuq) {
            if (MyRandom.random.nextBoolean()) {
                addBasicForestArtifact2(targetId)
            } else {
                addBasicForestArtifact(targetId)
            }
            fuq = false
        }
        list.addAll(addLameForestCards(5))
        list.addAll(addEnchantressForestCards(1))
        list.addAll(buildingTheEnchantressPart1(1))
        list.addAll(buildingTheEnchantressPart2(1))
        list.addAll(buildingTheEnchantressPart3(5))
        list.addAll(buildingTheEnchantressPart4(3))
        list.addAll(buildingTheEnchantressPart5(1))
        list.addAll(buildingTheEnchantressPart5dot5(1))
        list.addAll(buildingTheEnchantressPart6(5))
        list.addAll(buildingTheEnchantressPart7(1))
        list.addAll(buildingTheEnchantressPart8(1))
        list.addAll(buildingTheEnchantressPart9(1))
        return list
    }

    fun addBasicForestArtifact(targetId: EntityId): EntityId {
        val multiArti = generateEntity()
        multiArti add ScoreComponent(100)
        multiArti add TriggeredEffectsComponent(
            OnTurnStart,
            OnThresholdActivations(
                threshold = 20,
                effect = ManyEffectsHolder(
                    listOf(
                        GainScoreFromScoreComp, RemoveFlatMultiplier(0.02f)
                    )
                )
            )
        )
        multiArti add Forest
        multiArti add OwnerComponent(targetId)
        return multiArti
    }

    fun addBasicForestArtifact2(targetId: EntityId): EntityId {
        val multiArti = generateEntity()
        multiArti add ScoreComponent(-200)
        multiArti add TriggeredEffectsComponent(
            OnTurnStart,
            OnThresholdActivations(
                threshold = 10,
                effect = ManyEffectsHolder(
                    listOf(
                        GainScoreFromScoreComp, AddFlatMultiplier(0.5f)
                    )
                )
            )
        )
        multiArti add Forest
        multiArti add OwnerComponent(targetId)
        return multiArti
    }

    fun addLameForestCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Lame Tree...?",
                    hp = 1000f,
                    score = 0,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            (cardId get HealthComponent::class).setHealth(100f)
            cardId add TriggeredEffectsComponent(OnPlay, None)
        }
    }

    fun addEnchantressForestCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "The Forest Enchantress",
                    hp = 3f,
                    score = 0,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                OnPlay,
                GiftEffects(amount = 2f, trigger = OnPlay, effects = positiveForestEffects),
                GiftEffects(amount = 2f, trigger = OnPlay, effects = negativeForestEffects)
            )
        }
    }

    fun buildingTheEnchantressPart1(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Gift of Life",
                    hp = 2f,
                    score = 0,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(OnPlay, HealEntitiesInDeckToFull(3f))
        }
    }

    fun buildingTheEnchantressPart2(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Gift of growth",
                    hp = 2f,
                    score = 0,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(OnPlay, MultiplyMaxHp(2f))
        }
    }

    fun buildingTheEnchantressPart3(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Joy of Life",
                    hp = 100f,
                    score = 0,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(OnPlay, GainSelfHpAsScore(0.25f))
        }
    }

    fun buildingTheEnchantressPart4(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Gift Super Strength",
                    hp = 1f,
                    score = 0,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                OnPlay,
                GiveCardInDeckMultiplier(3.5f)
            )
        }
    }

    fun buildingTheEnchantressPart5(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Gift Burst of Speed",
                    hp = 1f,
                    score = 0,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                OnPlay,
                AddTempMultiplierToCardsInDeck(amount = 5f, size = 2f)
            )
        }
    }

    fun addTemporaryForestMultiplierTo(
        targetEntityId: EntityId,
        health: Float = 1f,
        multiplier: Float
    ): EntityId {
        val limitedMultiEntity = generateEntity()
        limitedMultiEntity add HealthComponent(health)
        limitedMultiEntity add OwnerComponent(targetEntityId)

        limitedMultiEntity add TriggeredEffectsComponent(
            mutableMapOf(
                OnCreation to mutableListOf(
                    AddMultiplier(multiplier)
                ),
                OnSpecial to mutableListOf(
                    TakeSelfDamage(1f)
                ),
                OnDeath to mutableListOf(
                    RemoveMultiplier(multiplier)
                )
            )
        )

        TriggerEffectHandler.handleTriggerEffect(
            EffectContext(
                trigger = OnCreation,
                source = limitedMultiEntity,
                target = targetEntityId
            )
        )
        return limitedMultiEntity
    }

    fun buildingTheEnchantressPart5dot5(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Humble gardener",
                    hp = 3f,
                    score = 0,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                OnPlay,
                CoActivation(
                    newSource = null,
                    OnPlay
                ),
                CoActivation(
                    newSource = null,
                    OnPlay
                )
            )
        }
    }

    fun buildingTheEnchantressPart6(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Broken Tree",
                    hp = 100f,
                    score = 0,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(OnPlay, TakeSelfPercentageDamage(0.10f))
        }
    }

    fun buildingTheEnchantressPart7(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Deaths call",
                    hp = 100f,
                    score = 0,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(OnPlay, GiftTickingSelfDamage(amount = 2f))
        }
    }

    fun buildingTheEnchantressPart8(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Flowery growth",
                    hp = 3f,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                OnPlay, GiftEffects(
                    amount = 3f, trigger = null, effects = listOf(
                        GiftTickingSelfDamage(amount = 1f),
                        CoActivation(
                            newSource = null,
                            OnPlay
                        ),
                        TakeSelfPercentageDamage(0.10f),
                        GiveCardInDeckMultiplier(0.5f),
                        HealEntitiesInDeckToFull(2f)
                    )
                )
            )
        }
    }

    fun buildingTheEnchantressPart9(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Broken Branches",
                    hp = 100f,
                    tags = listOf(Forest, Card),
                    img = R.drawable.forest_card
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                OnPlay,
                DealDamageFromOwnHealth(amount = 0.1f)
            )
        }
    }

}