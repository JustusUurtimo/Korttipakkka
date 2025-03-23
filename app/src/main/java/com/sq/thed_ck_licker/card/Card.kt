package com.sq.thed_ck_licker.card

import androidx.compose.runtime.MutableFloatState
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.components.CardClassification
import com.sq.thed_ck_licker.ecs.components.CardEffect
import com.sq.thed_ck_licker.ecs.components.CardEffectType
import com.sq.thed_ck_licker.ecs.components.CardEffectValue
import com.sq.thed_ck_licker.ecs.components.CardIdentity
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
        initializeCards(componentManager, allCards)
    }

    // Function to pull a random card
    fun pullRandomCard(): Pair<CardIdentity, CardEffect> {
        if (allCards.isEmpty()) {
            throw IllegalStateException("No cards available")
        }
        val randomCard = allCards.getRandomElement()
        val cardIdentity = componentManager.getComponent(randomCard, CardIdentity::class)
        val cardEffect = componentManager.getComponent(randomCard, CardEffect::class)
        return Pair(cardIdentity, cardEffect)
    }

    // Function to pull a specific card by its ID
    fun getCardById(cardId: Int): Pair<CardIdentity, CardEffect> {
        val card = allCards.find {
            componentManager.getComponent(it, CardIdentity::class).id == cardId
        }
            ?: throw IllegalArgumentException("Card with ID $cardId not found")
        val cardIdentity = componentManager.getComponent(card, CardIdentity::class)
        val cardEffect = componentManager.getComponent(card, CardEffect::class)
        return Pair(cardIdentity, cardEffect)
    }

    // Function to get the number of cards
    fun getNumberOfCards(): Int {
        return allCards.size
    }

    // Function to apply a card's effect (optional)
    fun applyCardEffect(
        newCard: Pair<CardIdentity, CardEffect>,
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

//private function that initializes cards
private fun initializeCards(componentManager: ComponentManager, allCards: MutableList<Int>) {

    val card1 = generateEntity()
    componentManager.addComponent(card1, CardIdentity(1, R.drawable.damage_5))
    componentManager.addComponent(
        card1,
        CardEffect(CardClassification.EVIL, CardEffectType.DAMAGE, CardEffectValue.DAMAGE_5)
    )

    val card2 = generateEntity()
    componentManager.addComponent(card2, CardIdentity(3, R.drawable.heal_5))
    componentManager.addComponent(
        card2,
        CardEffect(CardClassification.GOOD, CardEffectType.HEAL, CardEffectValue.HEAL_5)
    )

    val card3 = generateEntity()
    componentManager.addComponent(card3, CardIdentity(4, R.drawable.heal_10))
    componentManager.addComponent(
        card3,
        CardEffect(CardClassification.GOOD, CardEffectType.HEAL, CardEffectValue.HEAL_10)
    )

    val card4 = generateEntity()
    componentManager.addComponent(card4, CardIdentity(2, R.drawable.damage_6))
    componentManager.addComponent(
        card4,
        CardEffect(CardClassification.EVIL, CardEffectType.DAMAGE, CardEffectValue.DAMAGE_6)
    )

    val card5 = generateEntity()
    componentManager.addComponent(card5, CardIdentity(5, R.drawable.heal_2))
    componentManager.addComponent(
        card5,
        CardEffect(CardClassification.GOOD, CardEffectType.HEAL, CardEffectValue.HEAL_2)
    )

    val card6 = generateEntity()
    componentManager.addComponent(card6, CardIdentity(6, R.drawable.max_hp_2))
    componentManager.addComponent(
        card6,
        CardEffect(CardClassification.GOOD, CardEffectType.MAX_HP, CardEffectValue.MAX_HP_2)
    )

    val card7 = generateEntity()
    componentManager.addComponent(card7, CardIdentity(7, R.drawable.double_trouble))
    componentManager.addComponent(
        card7,
        CardEffect(
            CardClassification.MISC,
            CardEffectType.DOUBLE_TROUBLE,
            CardEffectValue.DOUBLE_TROUBLE
        )
    )

    val card8 = generateEntity()
    componentManager.addComponent(card8, CardIdentity(8, R.drawable.reverse_damage))
    componentManager.addComponent(
        card8,
        CardEffect(
            CardClassification.MISC,
            CardEffectType.REVERSE_DAMAGE,
            CardEffectValue.REVERSE_DAMAGE
        )
    )

    val card9 = generateEntity()
    componentManager.addComponent(card9, CardIdentity(9, R.drawable.shop_coupon))
    componentManager.addComponent(
        card9,
        CardEffect(CardClassification.MISC, CardEffectType.SHOP_COUPON, CardEffectValue.SHOP_COUPON)
    )

    allCards.addAll(
        intArrayOf(
            card1,
            card2,
            card3,
            card4,
            card5,
            card6,
            card7,
            card8,
            card9
        ).toList()
    )

}