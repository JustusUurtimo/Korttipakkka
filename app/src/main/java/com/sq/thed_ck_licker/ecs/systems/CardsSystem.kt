package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.generateEntity
import com.sq.thed_ck_licker.ecs.systems.CardEffectSystem.Companion.cardEffectSystem
import com.sq.thed_ck_licker.helpers.getRandomElement

class CardsSystem(private val componentManager: ComponentManager) {

    companion object {
        val cardsSystem: CardsSystem = CardsSystem(ComponentManager.componentManager)
    }

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

    // Function to pull a random card from deck
    fun pullRandomCardFromEntityDeck(entityId: Int,latestCard: MutableIntState): Int {
        val drawDeck = componentManager.getComponent(entityId, DrawDeckComponent::class)
        if (drawDeck.cardIds.isEmpty()) {
            throw IllegalStateException("No cards available")
        }
        latestCard.intValue = drawDeck.cardIds.getRandomElement()
        return latestCard.intValue
    }

    fun activateCard(latestCard: Int, playerCardCount: MutableIntState) {
        cardEffectSystem.playerTargetsPlayer(latestCard)
        playerCardCount.intValue += 1
        // TODO: Add here things needed to discard the card
    }

    fun replaceLatestCard(latestCard: Int) {

    }
}