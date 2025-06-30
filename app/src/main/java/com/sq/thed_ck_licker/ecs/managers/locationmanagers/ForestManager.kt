package com.sq.thed_ck_licker.ecs.managers.locationmanagers

import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.Tag
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
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
                    score = 0,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            (cardId get HealthComponent::class).setHealth(100f)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.None)
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
}