package com.sq.thed_ck_licker.ecs.systems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.IdentificationComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import com.sq.thed_ck_licker.helpers.MyRandom
import javax.inject.Inject

class PitSystem @Inject constructor(
    private val playerSystem: PlayerSystem,
    private val merchantSystem: MerchantSystem,
    private val cardPullingSystem: CardPullingSystem,
    private val cardsSystem: CardsSystem
) {
    fun dropCardInHole(latestCard: Int, ownerId: EntityId = getPlayerID()) {

    fun buyShovel() {
        playerSystem.updateScore(-500)
    }

    fun getPitCards(): List<Int> {
        return List(3) { cardsSystem.pullRandomCardFromEntityDeck(getPlayerID()) }
    }

    fun dropCardInPit(latestCard: Int) {
        if (latestCard == -1) return
        val tagsComponent = latestCard get TagsComponent::class

        try {
            (latestCard get EffectComponent::class).onSpecial.action(latestCard)
        } catch (_: Exception){
            Log.i("PitSystem","Nothing special happened... or did?")
            Log.i("PitSystem","No. nothing happened cuz they don't have cool effect")
        }

        if (tagsComponent.cardIsMerchant()) {
            handleMerchantCard(latestCard)
        } else {
            handleCardDrop(ownerId = ownerId, bonusScore = 200)
        }
    }

    // Helper Methods
    private fun handleMerchantCard(ownerId: EntityId = getPlayerID()) {
        val ownerInfo = (ownerId get LatestCardComponent::class)
        val latestCard = ownerInfo.getLatestCard()

        if (MyRandom.getRandomInt() <= 3) {
            val merchantId: Int? = (latestCard get IdentificationComponent::class).getCharacterId()
            merchantId?.let {
                merchantSystem.updateMerchantAffinity(-500, it)
            }
//            cardPullingSystem.pullNewCard(ownerId)
        } else {
            handleCardDrop(bonusScore = 500, ownerId = ownerId)
        }
    }

    private fun handleCardDrop(bonusScore: Int, ownerId: EntityId = getPlayerID()) {
        val ownerInfo = (ownerId get LatestCardComponent::class)
        val latestCard = ownerInfo.getLatestCard()
        val deck = (ownerId get DrawDeckComponent::class).getDrawCardDeck()
        deck.remove(latestCard)
//        ownerInfo.setLatestCard(-1)
        val score = ownerId get ScoreComponent::class
        score.addScore(bonusScore)
    }
}