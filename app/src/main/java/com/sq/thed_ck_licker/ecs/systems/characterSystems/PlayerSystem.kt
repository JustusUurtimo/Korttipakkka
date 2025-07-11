package com.sq.thed_ck_licker.ecs.systems.characterSystems

import android.util.Log
import androidx.compose.runtime.snapshotFlow
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.HistoryComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeDamage
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.misc.TickComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getRegularMerchantID
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.managers.locationmanagers.ForestManager
import com.sq.thed_ck_licker.ecs.states.PlayerState
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.helpers.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject


class PlayerSystem @Inject constructor(private val cardCreationSystem: CardCreationSystem) {

    fun initPlayer() {
        getPlayerID() add HealthComponent(100f)
        getPlayerID() add ScoreComponent()
        getPlayerID() add DiscardDeckComponent(mutableListOf())
        getPlayerID() add MultiplierComponent()
        getPlayerID() add LatestCardComponent()
        getPlayerID() add HistoryComponent(getPlayerID())
        getPlayerID() add ActivationCounterComponent()
        getPlayerID() add ImageComponent()
        getPlayerID() add OwnerComponent(getPlayerID())
        val deck = (getPlayerID() add DrawDeckComponent(mutableListOf()))

        if (Settings.isRealTimePlayerDamageEnabled.value) {
            getPlayerID() add TickComponent(tickThreshold = 1000)
            getPlayerID() add TriggeredEffectsComponent(Trigger.OnTick, TakeDamage(1f))
        }
        if (Settings.addBaseTestPackage.value) {
            deck.addCards(buildBasicTestingPlayerDeck())
        }
        if (Settings.addForestPackage.value) {
            deck.addCards(ForestManager.getForestPackage(getPlayerID()))
        }

        if (deck.getSize() == 0){
            Log.i("initPlayer"," Player has no deck for some reason? Adding basic cards as lohdutus palkinto.")
            deck.addCards(cardCreationSystem.addBasicScoreCards(5))
        }
        deck.shuffle()
    }

    private fun buildBasicTestingPlayerDeck(): List<Int> {

        val playerHealingCards = cardCreationSystem.addHealingCards(1)
        val playerDamageCards = cardCreationSystem.addDamageCards(5)
        val defaultCards = cardCreationSystem.addBreakingDefaultCards(1)
        val deactivationCards = cardCreationSystem.addDeactivationTestCards(1)
        val trapCards = cardCreationSystem.addTrapTestCards()
        val scoreGainerCards = cardCreationSystem.addScoreGainerTestCards()
        val beerGogglesCards = cardCreationSystem.addBeerGogglesTestCards()
        val maxHpCards = cardCreationSystem.addMaxHpTrapCards()
        val merchantCards = cardCreationSystem.addMerchantCards(5, getRegularMerchantID())
        val multiplierCards = cardCreationSystem.addTempMultiplierTestCards(2)
        val corruptionCards = cardCreationSystem.addShuffleTestCards(2)
        val timeBoundCards = cardCreationSystem.addTimeBoundTestCards(1)
        val basicsV3 = cardCreationSystem.addBasicScoreCards(5)


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
                multiplierCards +
                corruptionCards +
                timeBoundCards +
                basicsV3 +
                emptyList()
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

    fun getPlayerRewardTier(): Int {
        return (getPlayerID() get ScoreComponent::class).getRewardTier()
    }

    fun updateRewardTier(tier: Int) {
        (getPlayerID() get ScoreComponent::class).setRewardTier(tier)
    }

    fun removeCardFromDrawDeck(cardId: Int) {
        (getPlayerID() get DrawDeckComponent::class).removeCard(cardId)
    }

    fun getLatestCard(): Int {
        return (getPlayerID() get LatestCardComponent::class).getLatestCard()
    }

    fun setLatestCard(cardId: Int) {
        (getPlayerID() get LatestCardComponent::class).setLatestCard(cardId)
    }

    fun playerUpdates(): Flow<PlayerState> {
        return combine(
            snapshotFlow { getPlayerHealth() },
            snapshotFlow { getPlayerMaxHealth() },
            snapshotFlow { getPlayerScore() },
            snapshotFlow { getLatestCard() },
            snapshotFlow { getPlayerRewardTier() }
        ) { health, maxHealth, score, latestCard, rewardTier ->
            PlayerState(
                health = health,
                maxHealth = maxHealth,
                score = score,
                latestCard = latestCard,
                rewardTier = rewardTier
            )
        }
    }
}