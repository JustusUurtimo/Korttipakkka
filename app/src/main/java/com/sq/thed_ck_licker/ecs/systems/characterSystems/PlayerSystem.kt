package com.sq.thed_ck_licker.ecs.systems.characterSystems

import androidx.compose.runtime.snapshotFlow
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.MerchantComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getRegularMerchantID
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.states.PlayerState
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine


class PlayerSystem @Inject constructor(private val cardCreationSystem: CardCreationSystem) {

    fun initPlayer() {
        getPlayerID() add HealthComponent(100f)
        getPlayerID() add ScoreComponent()
        getPlayerID() add DrawDeckComponent(initPlayerDeck() as MutableList<Int>)
        getPlayerID() add EffectStackComponent()
        getPlayerID() add DiscardDeckComponent(mutableListOf<Int>())
    }

    private fun initPlayerDeck(): List<Int> {

        val playerHealingCards = cardCreationSystem.addHealingCards(1)
        val playerDamageCards = cardCreationSystem.addDamageCards(5)
        val defaultCards = cardCreationSystem.addBreakingDefaultCards(10)
        val deactivationCards = cardCreationSystem.addDeactivationTestCards(2)
        val trapCards = cardCreationSystem.addTrapTestCards()
        val scoreGainerCards = cardCreationSystem.addScoreGainerTestCards()
        val beerGogglesCards = cardCreationSystem.addBeerGogglesTestCards()
        val maxHpCards = cardCreationSystem.addMaxHpTrapCards()
        val merchantCards = cardCreationSystem.addMerchantCards(5, getRegularMerchantID())
        val basicScoreCards = cardCreationSystem.addBasicScoreCards(2)

        return emptyList<Int>() +
                playerHealingCards +
                playerDamageCards +
                defaultCards +
                deactivationCards +
                trapCards +
                scoreGainerCards +
                beerGogglesCards +
                maxHpCards +
                merchantCards +
                basicScoreCards +
                emptyList<Int>()
    }


    fun getPlayerHealth(): Float {
        return (getPlayerID() get HealthComponent::class).getHealth()
    }

    fun getPlayerScore(): Int {
        return (getPlayerID() get ScoreComponent::class).getScore()
    }

    fun getPlayerMaxHealth(): Float {
        return (getPlayerID() get HealthComponent::class).getMaxHealth()
    }

    fun updateScore(amount: Int) {
        (getPlayerID() get ScoreComponent::class).addScore(amount)
    }

    fun removeCardFromDrawDeck(cardId: Int) {
        (getPlayerID() get DrawDeckComponent::class).removeCard(cardId)
    }

    fun playerUpdates(): Flow<PlayerState> {
        return combine(
            snapshotFlow { getPlayerHealth() },
            snapshotFlow { getPlayerMaxHealth() },
            snapshotFlow { getPlayerScore() }
        ) { health, maxHealth, score ->
            PlayerState(
                health = health,
                maxHealth = maxHealth,
                score = score
            )
        }
    }
}