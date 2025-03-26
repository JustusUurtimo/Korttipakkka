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

class CardsSystem {
    // Component Manager
    private val componentManager = ComponentManager()

    // List of all card entities
    private val allCards = mutableListOf<Int>()

    fun initCards(
        amount: Int,
        cardImage: Int,
        score: Int,
        description: String,
        name: String,
        tags: List<CardTag>
    ): List<Int> {
        val cardIds: MutableList<Int> = mutableListOf()
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            componentManager.addComponent(cardEntity, ImageComponent(cardImage))
            componentManager.addComponent(cardEntity, ScoreComponent(score))
            componentManager.addComponent(cardEntity, DescriptionComponent(description))
            componentManager.addComponent(cardEntity, NameComponent(name))
            componentManager.addComponent(cardEntity, TagsComponent(tags))
            cardIds.add(cardEntity)
        }
        return cardIds
    }

    fun getCardComponentByEntity(entity: Int): Pair<ImageComponent, CardEffect> {
        val card = componentManager.getComponent(entity, ImageComponent::class)
        val cardEffect = componentManager.getComponent(entity, CardEffect::class)
        return Pair(card, cardEffect)
    }


    // Function to pull a random card from deck
    fun pullRandomCardFromEntityDeck(entity: Int): Int {
        val drawDeck = componentManager.getComponent(entity, DrawDeckComponent::class)
        if (drawDeck.cardIds.isEmpty()) {
            throw IllegalStateException("No cards available")
        }
        return drawDeck.cardIds.getRandomElement()
    }

    fun applyCardEffect(
        newCard: Pair<ImageComponent, CardEffect>,
        playerHealth: MutableFloatState,
        reverseDamage: Boolean,
        doubleTrouble: Boolean
    ) {
        val cardEffectSystem = CardEffectSystem()
        cardEffectSystem.applyEffect(
            newCard,
            playerHealth,
            componentManager,
            reverseDamage,
            doubleTrouble
        )
    }
}