package com.sq.thed_ck_licker.ecs.systems.characterSystems

import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityManager.getRegularMerchantID
import com.sq.thed_ck_licker.ecs.add
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem.Companion.instance as cardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem.Companion.instance as cardsSystem


class MerchantSystem private constructor(@Suppress("unused") private val componentManager: ComponentManager) {
    companion object {
        val instance: MerchantSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MerchantSystem(ComponentManager.componentManager)
        }
    }

    //I know this is duplicate code, but we have to unify deck building to a one system
    // its on issue #48 atm
    fun initRegularMerchant() {
        getRegularMerchantID() add DrawDeckComponent(initRegularMerchantDeck() as MutableList<Int>)
    }

    private fun initRegularMerchantDeck(): List<Int> {

        val playerHealingCards = cardCreationSystem.addHealingCards(5)
        val trapCards = cardCreationSystem.addTrapTestCard()
        val scoreGainerCards = cardCreationSystem.addScoreGainerTestCard()
        val maxHpCards = cardCreationSystem.addMaxHpTrapCards()

        return emptyList<Int>() +
                playerHealingCards +
                trapCards +
                scoreGainerCards +
                maxHpCards +
                emptyList<Int>()
    }


    fun reRollMerchantHand(merchantId: Int): List<Int> {
        val newHand = List(3) { cardsSystem.pullRandomCardFromEntityDeck(merchantId) }
        return newHand
    }

    fun getReRollCount(merchantCardId: Int): MutableIntState {
        return (merchantCardId get ActivationCounterComponent::class).activations
    }

}