package com.sq.thed_ck_licker.ecs.managers.locationmanagers

import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.Tag
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeSelfDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeSelfPercentageDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects.HealEntitiesInDeckToFull
import com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects.MultiplyMaxHp
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.CoActivation
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

    var fuq = true

    fun getForestPackage(targetId: EntityId): MutableList<EntityId> {
        val list = mutableListOf<EntityId>()

        // For some reason, looking at you hilt..., we init before game starts and then init again after game starts.
        //So this needs to be here to block getting 2 of these...
        if (fuq) {
            if (MyRandom.random.nextBoolean()) {
                addBasicForestArtifact(targetId)
            } else {
                addBasicForestArtifact2(targetId)
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
        return list
    }

    fun addBasicForestArtifact(targetId: EntityId): EntityId {
        val multiArti = generateEntity()
        multiArti add ScoreComponent(5)
        multiArti add TriggeredEffectsComponent(
            Trigger.OnTurnStart, GainScoreFromScoreComp, RemoveFlatMultiplier(0.001f)
        )
        multiArti add TagsComponent(Tag.FOREST)
        multiArti add OwnerComponent(targetId)
        return multiArti
    }

    fun addBasicForestArtifact2(targetId: EntityId): EntityId {
        val multiArti = generateEntity()
        multiArti add ScoreComponent(-30)
        multiArti add TriggeredEffectsComponent(
            Trigger.OnTurnStart, GainScoreFromScoreComp, AddFlatMultiplier(0.05f)
        )
        multiArti add TagsComponent(Tag.FOREST)
        multiArti add OwnerComponent(targetId)
        return multiArti
    }

    fun addLameForestCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Lame Tree Card...?",
                    hp = 1000f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            (cardId get HealthComponent::class).setHealth(100f)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, None)
        }
    }

    fun addEnchantressForestCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "The Forest Enchantress",
                    hp = 3f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, None)
        }
    }

    fun buildingTheEnchantressPart1(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Gift of life",
                    hp = 5f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, HealEntitiesInDeckToFull(5f))
        }
    }

    fun buildingTheEnchantressPart2(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Gift of growth",
                    hp = 2f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, MultiplyMaxHp(2f))
        }
    }

    fun buildingTheEnchantressPart3(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "HP as score",
                    hp = 100f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, GainSelfHpAsScore(0.25f))
        }
    }

    fun buildingTheEnchantressPart4(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Gift Super Strength",
                    hp = 1f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                Trigger.OnPlay,
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
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                Trigger.OnPlay,
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
                Trigger.OnCreation to mutableListOf(
                    AddMultiplier(multiplier)
                ),
                Trigger.OnSpecial to mutableListOf(
                    TakeSelfDamage(1f)
                ),
                Trigger.OnDeath to mutableListOf(
                    RemoveMultiplier(multiplier)
                )
            )
        )

        TriggerEffectHandler.handleTriggerEffect(
            EffectContext(
                trigger = Trigger.OnCreation,
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
                    name = "Shielded Activation",
                    hp = 3f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                Trigger.OnPlay,
                CoActivation(
                    newSource = null,
                    Trigger.OnPlay
                ),
                CoActivation(
                    newSource = null,
                    Trigger.OnPlay
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
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, TakeSelfPercentageDamage(0.10f))
        }
    }
}