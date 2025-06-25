package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.IdentificationComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.CardTag
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity
import com.sq.thed_ck_licker.helpers.typealiases.CardPreset


/*
 * This is mainly a suggestion.
 * As we need more and more cards that need to refer their own components,
 * this way supports it more cleanly.
 * This could even be used for generic building of entities.
 * Thou since basically all things are cards, that would be over kill.
 *
 * As bonus this only needs Component Manager as dependency.
 */
object CardBuilderSystem2 {

    fun generateCards(amount: Int, init: (EntityId) -> Unit): List<EntityId> {
        val cardIds = mutableListOf<EntityId>()
        repeat(amount) {
            val entity = generateEntity()
            init(entity)
            cardIds.add(entity)
        }
        return cardIds
    }

    fun withBasicCardDefaults(): CardPreset = {
        this add ActivationCounterComponent()
        this add ImageComponent(R.drawable.placeholder)
        this add TagsComponent(listOf(CardTag.CARD))
    }

    fun withHealth(hp: Number): CardPreset = {
        this add HealthComponent(hp)
    }

    fun withScore(score: Int): CardPreset = {
        this add ScoreComponent(score)
    }

    fun withName(name: String, characterId: Int? = null): CardPreset = {
        this add IdentificationComponent(name, characterId)
    }
}