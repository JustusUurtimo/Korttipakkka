package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.runtime.MutableFloatState
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.components.CardEffect
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.generateEntity
import com.sq.thed_ck_licker.helpers.getRandomElement
import kotlin.reflect.KClass

class CardsSystem {
    // Component Manager
    private val componentManager = ComponentManager.componentManager

    fun <T : Any> initCards(
        amount: Int,
        cardImage: Int,
        description: String,
        name: String,
        tags: List<CardTag>,
        cardComponent: T
    ): List<Int> {
        val cardIds: MutableList<Int> = mutableListOf()
        repeat (amount) {
            val cardEntity = generateEntity()
            componentManager.addComponent(cardEntity, ImageComponent(cardImage))
            componentManager.addComponent(cardEntity, cardComponent)
            componentManager.addComponent(cardEntity, DescriptionComponent(description))
            componentManager.addComponent(cardEntity, NameComponent(name))
            componentManager.addComponent(cardEntity, TagsComponent(tags))
            cardIds.add(cardEntity)
        }
        return cardIds
    }

    fun getCardComponentByEntity(entity: Int): Pair<ImageComponent, CardEffect> {
        val cardImage = componentManager.getComponent(entity, ImageComponent::class)
        val cardEffect = componentManager.getComponent(entity, CardEffect::class)
        return Pair(cardImage, cardEffect)
    }

    fun getCardComponentByTag(tag: CardTag): Pair<ImageComponent, CardEffect> {
        val entityMap = componentManager.getEntitiesWithTags(listOf(tag))
        return getCardComponentByEntity(entityMap.keys.first())
    }


    // Function to pull a random card from deck
    fun pullRandomCardFromEntityDeck(entity: Int): Pair<ImageComponent, CardEffect> {
        val drawDeck = componentManager.getComponent(entity, DrawDeckComponent::class)
        if (drawDeck.cardIds.isEmpty()) {
            throw IllegalStateException("No cards available")
        }
        return getCardComponentByEntity(drawDeck.cardIds.getRandomElement())
    }

    fun applyCardEffect(
        newCard: Pair<ImageComponent, CardEffect>,
        playerHealth: MutableFloatState,
        reverseDamage: Boolean,
        doubleTrouble: Boolean
    ) {
        val cardEffectSystem = CardEffectSystem(componentManager)
        cardEffectSystem.applyEffect(
            newCard,
            playerHealth,
            componentManager,
            reverseDamage,
            doubleTrouble
        )
    }
}