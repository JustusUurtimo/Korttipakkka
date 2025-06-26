package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.IdentificationComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.CardTag
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity
import com.sq.thed_ck_licker.helpers.typealiases.CardPreset


/**
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

    data class CardConfig(
        val img: Int = R.drawable.placeholder,
        val tags: List<CardTag> = listOf(CardTag.CARD),
        val name: String = "Card",
        val characterId: Int? = null,
        val hp: Float? = 10f,
        val score: Int? = null,
        val multiplier: Float = 1f
    )

    fun withBasicCardDefaults(config: CardConfig = CardConfig()): CardPreset = {
        this add ActivationCounterComponent()
        this add ImageComponent(config.img)
        this add TagsComponent(config.tags)
        this add IdentificationComponent(config.name, config.characterId)
        config.hp?.let { this add HealthComponent(it) }
        config.score?.let { this add ScoreComponent(it) }
        this add MultiplierComponent(config.multiplier)
    }

}