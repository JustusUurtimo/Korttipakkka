package com.sq.thed_ck_licker.card

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableFloatState
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.helpers.getRandomElement
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass


// Enums
enum class CardClassification { EVIL, GOOD, MISC }
enum class CardEffectType { HP_REGEN, HEAL, DAMAGE, MAX_HP, DOUBLE_TROUBLE, REVERSE_DAMAGE, SHOP_COUPON }
enum class CardEffectValue(val value: Float) {
    DAMAGE_5(5f), DAMAGE_6(6f), HEAL_5(5f), HEAL_2(2f), HEAL_10(10f),
    MAX_HP_2(2f), DOUBLE_TROUBLE(0f), REVERSE_DAMAGE(0f), SHOP_COUPON(100f)
}

@Parcelize
data class CardIdentity(
    val id: Int,
    @DrawableRes val cardImage: Int
) : Parcelable

@Parcelize
data class CardEffect(
    val classification: CardClassification,
    val effectType: CardEffectType,
    val effectValue: CardEffectValue
) : Parcelable

// Component Manager
class ComponentManager {
    private val components = mutableMapOf<KClass<*>, MutableMap<Int, Any>>()

    fun <T : Any> addComponent(entity: Int, component: T) {
        components.getOrPut(component::class) { mutableMapOf() }[entity] = component
    }

    fun <T : Any> getComponent(entity: Int, componentClass: KClass<T>): T {
        val componentMap = components[componentClass]
            ?: throw IllegalStateException("No components of type ${componentClass.simpleName} found")

        val component = componentMap[entity]
            ?: throw IllegalStateException("Component of type ${componentClass.simpleName} not found for entity $entity")

        // Check the type of the component before casting
        if (componentClass.isInstance(component)) {
            @Suppress("UNCHECKED_CAST") // Safe cast after type check
            return component as T
        } else {
            throw IllegalStateException("Component for entity $entity is not of type ${componentClass.simpleName}")
        }
    }
}

// Systems
class CardEffectSystem {
    fun applyEffect(
        newCard: Pair<CardIdentity, CardEffect>,
        playerHealth: MutableFloatState,
        components: ComponentManager,
        reverseDamage: Boolean,
        doubleTrouble: Boolean
    ) {
        val effect = newCard.second
        when (effect.effectType) {
            CardEffectType.DAMAGE -> applyDamage(
                playerHealth,
                effect.effectValue.value,
                reverseDamage,
                doubleTrouble
            )

            CardEffectType.HEAL -> applyHeal(playerHealth, effect.effectValue.value, doubleTrouble)
            // Handle other effect types...
            else -> println("Unknown effect type")
        }
    }

    private fun applyDamage(
        playerHealth: MutableFloatState,
        amount: Float,
        reverseDamage: Boolean,
        doubleTrouble: Boolean
    ) {
        if (reverseDamage) {
            playerHealth.floatValue -= (amount)
        } else if (doubleTrouble) {
            playerHealth.floatValue += (amount * 2)
        } else {
            playerHealth.floatValue += (amount)
        }
    }

    private fun applyHeal(playerHealth: MutableFloatState, amount: Float, doubleTrouble: Boolean) {
        if (doubleTrouble) {
            playerHealth.floatValue -= (amount * 2)
        } else {
            playerHealth.floatValue -= (amount)
        }
    }
}

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

    val card1 = 1
    componentManager.addComponent(card1, CardIdentity(1, R.drawable.damage_5))
    componentManager.addComponent(
        card1,
        CardEffect(CardClassification.EVIL, CardEffectType.DAMAGE, CardEffectValue.DAMAGE_5)
    )

    val card4 = 2
    componentManager.addComponent(card4, CardIdentity(2, R.drawable.damage_6))
    componentManager.addComponent(
        card4,
        CardEffect(CardClassification.EVIL, CardEffectType.DAMAGE, CardEffectValue.DAMAGE_6)
    )

    val card2 = 3
    componentManager.addComponent(card2, CardIdentity(3, R.drawable.heal_5))
    componentManager.addComponent(
        card2,
        CardEffect(CardClassification.GOOD, CardEffectType.HEAL, CardEffectValue.HEAL_5)
    )

    val card3 = 4
    componentManager.addComponent(card3, CardIdentity(4, R.drawable.heal_10))
    componentManager.addComponent(
        card3,
        CardEffect(CardClassification.GOOD, CardEffectType.HEAL, CardEffectValue.HEAL_10)
    )

    val card5 = 5
    componentManager.addComponent(card5, CardIdentity(5, R.drawable.heal_2))
    componentManager.addComponent(
        card5,
        CardEffect(CardClassification.GOOD, CardEffectType.HEAL, CardEffectValue.HEAL_2)
    )

    val card6 = 6
    componentManager.addComponent(card6, CardIdentity(6, R.drawable.max_hp_2))
    componentManager.addComponent(
        card6,
        CardEffect(CardClassification.GOOD, CardEffectType.MAX_HP, CardEffectValue.MAX_HP_2)
    )

    val card7 = 7
    componentManager.addComponent(card7, CardIdentity(7, R.drawable.double_trouble))
    componentManager.addComponent(
        card7,
        CardEffect(
            CardClassification.MISC,
            CardEffectType.DOUBLE_TROUBLE,
            CardEffectValue.DOUBLE_TROUBLE
        )
    )

    val card8 = 8
    componentManager.addComponent(card8, CardIdentity(8, R.drawable.reverse_damage))
    componentManager.addComponent(
        card8,
        CardEffect(
            CardClassification.MISC,
            CardEffectType.REVERSE_DAMAGE,
            CardEffectValue.REVERSE_DAMAGE
        )
    )

    val card9 = 9
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