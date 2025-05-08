package com.sq.thed_ck_licker.ecs.systems.characterSystems

import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.MerchantComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getRegularMerchantID
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import javax.inject.Inject


class MerchantSystem @Inject constructor(
    private val cardCreationSystem: CardCreationSystem,
    private val playerSystem: PlayerSystem,
    private val cardsSystem: CardsSystem
) {

    fun initRegularMerchant() {
        getRegularMerchantID() add DrawDeckComponent(initRegularMerchantDeck().toMutableList())
    }

    private fun initRegularMerchantDeck(): List<Int> {

        val playerHealingCards = cardCreationSystem.addHealingCards(5)
        val scoreGainerCards = cardCreationSystem.addScoreGainerTestCards()
        val maxHpCards = cardCreationSystem.addMaxHpTrapCards()
        val shovelCards = cardCreationSystem.addShovelCards(10)

        return emptyList<Int>() +
                shovelCards +
                playerHealingCards +
                scoreGainerCards +
                maxHpCards +
                emptyList<Int>()
    }

    fun reRollMerchantHand(): List<Int> {
        val merchantId = (getPlayerID() get MerchantComponent::class).getMerchantId()
        val deck = (merchantId get DrawDeckComponent::class).getDrawCardDeck()
        if (deck.size < 3) {
            deck.addAll(initRegularMerchantDeck())
        }
        val newHand = List(3) { cardsSystem.pullRandomCardFromEntityDeck(merchantId) }
        return newHand
    }

    fun getReRollCount(merchantCardId: Int): Int {
        return (merchantCardId get ActivationCounterComponent::class).getActivations()
    }
    fun addReRollCount(merchantCardId: Int) {
        (merchantCardId get ActivationCounterComponent::class).activate()
    }

    fun chooseMerchantCard(latestCard: MutableIntState, newCard: Int) {
        playerSystem.updateScore(-100)
        latestCard.intValue = newCard
        playerSystem.updateMerchantId(-1)
    }

}