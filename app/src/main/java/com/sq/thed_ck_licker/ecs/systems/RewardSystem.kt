package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getRegularRewardID
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getSpecialRewardID
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import javax.inject.Inject

class RewardSystem @Inject constructor(
    private val cardsSystem: CardsSystem,
    private val cardCreationSystem: CardCreationSystem,
    private val playerSystem: PlayerSystem
) {

    fun initRewards() {
        initRegularRewardSelection()
        initSpecialRewardSelection()
    }

    private fun initRegularRewardSelection() {
        getRegularRewardID() add DrawDeckComponent(initRegularRewardDeck().toMutableList())
    }

    private fun initSpecialRewardSelection() {
        getSpecialRewardID() add DrawDeckComponent(initSpecialRewardDeck().toMutableList())
    }

    private fun initSpecialRewardDeck(): List<Int> {
        val playerHealingCards = cardCreationSystem.addHealingCards(5)
        val scoreGainerCards = cardCreationSystem.addScoreGainerTestCards(5)
        val multiplierCards = cardCreationSystem.addTempMultiplierTestCards(2)

        return emptyList<Int>() +
                playerHealingCards +
                scoreGainerCards +
                multiplierCards +
                emptyList<Int>()
    }

    private fun initRegularRewardDeck(): List<Int> {

        val playerHealingCards = cardCreationSystem.addHealingCards(5)
        val deactivationCards = cardCreationSystem.addDeactivationTestCards(2)

        return emptyList<Int>() +
                playerHealingCards +
                deactivationCards +
                emptyList<Int>()
    }

    fun getRewardCards(): List<Int> {
        val playerScore = playerSystem.getPlayerScore()
        val rewardTier = when {
            playerScore in 100..199 -> {getRegularRewardID()}
            else -> { getSpecialRewardID() }
        }
        val deck = (rewardTier get DrawDeckComponent::class).getDrawCardDeck()
        if (deck.size < 3) {
            deck.addAll(initRegularRewardDeck())
        }
        val newHand = List(3) { cardsSystem.pullRandomCardFromEntityDeck(rewardTier) }
        return newHand
    }

    fun selectReward(chosenCard: Int) {
        playerSystem.setLatestCard(chosenCard)
    }

}
