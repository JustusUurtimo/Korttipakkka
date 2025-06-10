package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.components.IdentificationComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
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
    private val cardsSystem: CardsSystem
) {

    fun buyShovel() {
        playerSystem.updateScore(-500)
    }

    fun getPitCards(): List<Int> {
        return List(3) { cardsSystem.pullRandomCardFromEntityDeck(getPlayerID()) }
    }

    fun dropCardInPit(latestCard: Int) {
        if (latestCard == -1) return
        val tagsComponent = latestCard get TagsComponent::class

        if (tagsComponent.cardIsMerchant()) {
            handleMerchantCard(latestCard)
        } else {
            handleCardDrop(latestCard, bonusScore = 200)
        }
    }

    // Helper Methods
    private fun handleMerchantCard(latestCard: Int) {
        if (MyRandom.getRandomInt() <= 3) {
            val merchantId: Int? = (latestCard get IdentificationComponent::class).getCharacterId()
            merchantId?.let {
                merchantSystem.updateMerchantAffinity(-500, it)
            }
        } else {
            handleCardDrop(latestCard, bonusScore = 500)
        }
    }

    private fun handleCardDrop(latestCard: Int, bonusScore: Int) {
        playerSystem.removeCardFromDrawDeck(latestCard)
        playerSystem.updateScore(bonusScore)
    }
}