package com.sq.thed_ck_licker.ecs.systems.characterSystems

import androidx.compose.runtime.snapshotFlow
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.misc.EntityMemoryComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getRegularMerchantID
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.states.MerchantState
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
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

        val playerHealingCards = cardCreationSystem.addHealingCards(1)
        val scoreGainerCards = cardCreationSystem.addScoreGainerTestCards()
        val maxHpCards = cardCreationSystem.addMaxHpTrapCards()
        val shovelCards = cardCreationSystem.addShovelCards(2)
        val shuffleCards = cardCreationSystem.addShuffleTestCards(3)
        val breakingCards = cardCreationSystem.addBreakingDefaultCards(5)

        return emptyList<Int>() +
                shovelCards +
                playerHealingCards +
                scoreGainerCards +
                maxHpCards +
                shuffleCards +
                breakingCards

    }

    fun rollMerchantHand(merchantId: Int): List<Int> {
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

    fun chooseMerchantCard(newCard: Int, activeMerchant: Int, ownerId: EntityId = getPlayerID()) {
        val merchantAffinity : Int = (activeMerchant get(EntityMemoryComponent::class)).getAffinity()
        val scoreComp = (ownerId get ScoreComponent::class)
        when {
            (merchantAffinity >= 500) -> {
                scoreComp.addScore(-50)
            }

            (merchantAffinity <= -100) -> {
                scoreComp.addScore(-500)
            }

            else -> {
                scoreComp.addScore(-100)
            }
        }
        updateMerchantAffinity(10, activeMerchant)
        (ownerId get LatestCardComponent::class).setLatestCard(newCard)
    }

    fun updateMerchantAffinity(amount: Int, merchantId: Int) {
        (merchantId get EntityMemoryComponent::class).updateAffinity(amount)
    }

    fun getMerchantAffinity(activeMerchantId: StateFlow<Int>): Int {
        return try {
            (activeMerchantId.value get EntityMemoryComponent::class).getAffinity()
        } catch (_: Exception) {
            0
        }
    }

    fun merchantUpdates(activeMerchantId: StateFlow<Int>): Flow<MerchantState> {
        return combine(
            snapshotFlow { getMerchantAffinity(activeMerchantId) },
        ) { affinity ->
            MerchantState(
                affinity = affinity.first()
            )
        }
    }

}