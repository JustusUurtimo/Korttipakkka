package com.sq.thed_ck_licker.ecs.systems.characterSystems

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.snapshotFlow
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EntityMemoryComponent
import com.sq.thed_ck_licker.ecs.components.MerchantComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getRegularMerchantID
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.states.MerchantState
import com.sq.thed_ck_licker.ecs.states.PlayerState
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject


class MerchantSystem @Inject constructor(
    private val cardCreationSystem: CardCreationSystem,
    private val playerSystem: PlayerSystem,
    private val cardsSystem: CardsSystem
) {

    fun initRegularMerchant() {
        getRegularMerchantID() add DrawDeckComponent(initRegularMerchantDeck().toMutableList())
        getRegularMerchantID() add EntityMemoryComponent()
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
        val merchantId = (getPlayerID() get MerchantComponent::class).getMerchantId()
        val merchantAffinity = (merchantId get EntityMemoryComponent::class).getAffinity()
        when {
            (merchantAffinity >= 500) -> {playerSystem.updateScore(-50)}
            (merchantAffinity <= -100) -> {playerSystem.updateScore(-500)}
            else -> {playerSystem.updateScore(-100)}
        }
        updateMerchantAffinity(10, merchantId)
        latestCard.intValue = newCard
        playerSystem.updateMerchantId(-1)
    }

    fun updateMerchantAffinity(amount: Int, merchantId: Int) {
        (merchantId get EntityMemoryComponent::class).updateAffinity(amount)
    }
    fun getMerchantAffinity(): Int {
        val merchantId = (getPlayerID() get MerchantComponent::class).getMerchantId()
        return try {
            (merchantId get EntityMemoryComponent::class).getAffinity()
        } catch (e: Exception) {
            0
        }

    }

    fun merchantUpdates(): Flow<MerchantState> {
        return combine(
            snapshotFlow { getMerchantAffinity() },
        ) { affinity ->
            MerchantState(
                affinity = affinity.first()
            )
        }
    }

}