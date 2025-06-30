package com.sq.thed_ck_licker.ecs.managers.locationmanagers

import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.Tag
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
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
//                addBasicForestArtifact(targetId)
            } else {
//                addBasicForestArtifact2(targetId)
            }
            fuq = false
        }
        list.addAll(addLameForestCards(5))
//        list.addAll(addEnchantressForestCards(1))
//        list.addAll(buildingTheEnchantressPart1(1))
//        list.addAll(buildingTheEnchantressPart2(1))
//        list.addAll(buildingTheEnchantressPart3(5))
//        list.addAll(buildingTheEnchantressPart4(3))
        list.addAll(buildingTheEnchantressPart5(1))
//        list.addAll(buildingTheEnchantressPart5dot5(1))
        return list
    }

    fun addBasicForestArtifact(targetId: EntityId): EntityId {
        val multiArti = generateEntity()
        multiArti add ScoreComponent(15)
        multiArti add TriggeredEffectsComponent(
            Trigger.OnTurnStart, Effect.GainScoreFromScoreComp, Effect.RemoveFlatMultiplier(0.005f)
        )
        multiArti add TagsComponent(Tag.FOREST)
        multiArti add OwnerComponent(targetId)
        return multiArti
    }

    fun addBasicForestArtifact2(targetId: EntityId): EntityId {
        val multiArti = generateEntity()
        multiArti add ScoreComponent(-30)
        multiArti add TriggeredEffectsComponent(
            Trigger.OnTurnStart, Effect.GainScoreFromScoreComp, Effect.AddFlatMultiplier(0.05f)
        )
        multiArti add TagsComponent(Tag.FOREST)
        multiArti add OwnerComponent(targetId)
        return multiArti
    }

    fun addLameForestCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Lame Forest Card...?",
                    hp = 1000f,
                    score = cardId,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            (cardId get HealthComponent::class).setHealth(100f)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.GainScoreFromScoreComp)
        }
    }

    fun addEnchantressForestCards(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Enchantress",
                    hp = 3f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.None)
        }
    }

    fun buildingTheEnchantressPart1(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Heal to full",
                    hp = 5f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.HealEntitiesInDeckToFull(5f))
        }
    }

    fun buildingTheEnchantressPart2(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Double The Max HP",
                    hp = 2f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.MultiplyMaxHp(2f))
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
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.GainSelfHpAsScore(0.25f))
        }
    }

    fun buildingTheEnchantressPart4(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Gain Super Strength",
                    hp = 1f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                Trigger.OnPlay,
                Effect.GiveCardInDeckMultiplier(10f)
            )
        }
    }

    fun buildingTheEnchantressPart5(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Burst of Speed",
                    hp = 1f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.AddTempMultiplierToCardsInDeck(amount= 5f, size = 2f))
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
                    Effect.AddMultiplier(multiplier)
                ),
                Trigger.OnSpecial to mutableListOf(
                    Effect.TakeSelfDamage(1f)
                ),
                Trigger.OnDeath to mutableListOf(
                    Effect.RemoveMultiplier(multiplier)
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
                    name = "CoActivation",
                    hp = 1f,
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(
                Trigger.OnPlay, Effect.CoActivation(
                    newSource = null,
                    Trigger.OnPlay
                )
            )
        }
    }
}