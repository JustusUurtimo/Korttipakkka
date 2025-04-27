package com.sq.thed_ck_licker.ecs.systems.characterSystems

import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getRegularMerchantID
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem.Companion.instance as cardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem.Companion.instance as cardsSystem


class MerchantSystem private constructor(@Suppress("unused") private val componentManager: ComponentManager) {
    companion object {
        val instance: MerchantSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MerchantSystem(ComponentManager.componentManager)
        }
    }

    fun initRegularMerchant() {
        getRegularMerchantID() add DrawDeckComponent(initRegularMerchantDeck().toMutableList())
    }

    private fun initRegularMerchantDeck(): List<Int> {

        val playerHealingCards = cardCreationSystem.addHealingCards(5)
        val trapCards = cardCreationSystem.addTrapTestCards()
        val scoreGainerCards = cardCreationSystem.addScoreGainerTestCards()
        val maxHpCards = cardCreationSystem.addMaxHpTrapCards()

        return emptyList<Int>() +
                playerHealingCards +
                trapCards +
                scoreGainerCards +
                maxHpCards +
                emptyList<Int>()
    }


    fun reRollMerchantHand(merchantId: Int): List<Int> {
        val deck = (merchantId get DrawDeckComponent::class).drawCardDeck
        if (deck.size < 3) {
            deck.addAll(initRegularMerchantDeck())
        }
        val newHand = List(3) { cardsSystem.pullRandomCardFromEntityDeck(merchantId) }
        return newHand
    }

    fun getReRollCount(merchantCardId: Int): MutableIntState {
        return (merchantCardId get ActivationCounterComponent::class).activations
    }

}