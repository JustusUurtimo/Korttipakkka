package com.sq.thed_ck_licker.card

import androidx.compose.runtime.MutableFloatState
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.components.CardEffect
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.generateEntity
import com.sq.thed_ck_licker.ecs.systems.CardEffectSystem
import com.sq.thed_ck_licker.helpers.getRandomElement


//TODO this should be moved some where else

// CardGame Class
class Cards {
    // Component Manager
    private val componentManager = ComponentManager()

    // List of all card entities
    private val allCards = mutableListOf<Int>()

    // Initialize the game with some cards
    init {
        initHealCards(componentManager)
    }



    // Function to pull a random card
    fun pullRandomCard(): Pair<ImageComponent, CardEffect> {
        if (allCards.isEmpty()) {
            throw IllegalStateException("No cards available")
        }
        val randomCard = allCards.getRandomElement()
        val cardIdentity = componentManager.getComponent(randomCard, ImageComponent::class)
        val cardEffect = componentManager.getComponent(randomCard, CardEffect::class)
        return Pair(cardIdentity, cardEffect)
    }


    // TODO We should really thrive to only make methods we need
    //  YAGNI = You ain't gonna need it
    //  To truly get into proper headspace we should use TDD
    // Function to get the number of cards
    fun getNumberOfCards(): Int {
        return allCards.size
    }

    // Function to apply a card's effect (optional)
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

private fun initHealCards(componentManager: ComponentManager, amount: Int = 3) {
    for (i in 1..amount) {
        val cardEntity = generateEntity()
        componentManager.addComponent(cardEntity, ImageComponent(R.drawable.heal_10))
        componentManager.addComponent(cardEntity, ScoreComponent(10))
        componentManager.addComponent(
            cardEntity,
            DescriptionComponent("This is simple placeholder description #$i")
        )
        componentManager.addComponent(cardEntity, NameComponent("Default Card #$i"))
        componentManager.addComponent(cardEntity, TagsComponent(listOf(CardTag.Card)))
    }
}

