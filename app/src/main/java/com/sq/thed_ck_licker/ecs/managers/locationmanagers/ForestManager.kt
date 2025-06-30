package com.sq.thed_ck_licker.ecs.managers.locationmanagers

import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.Tag
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity
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

    fun addLameForestCards(amount: Int, scoreSize: Int = 1): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults(
                CardConfig(
                    name = "Lame Forest Card...?",
                    hp = 1000f,
                    score = scoreSize,
                    tags = listOf(Tag.FOREST, Tag.CARD)
                )
            )(cardId)
            cardId add TriggeredEffectsComponent(Trigger.OnPlay, Effect.None)
        }
    }
}