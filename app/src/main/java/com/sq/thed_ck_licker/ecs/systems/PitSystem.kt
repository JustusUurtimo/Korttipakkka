package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.components.IdentificationComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import com.sq.thed_ck_licker.helpers.MyRandom
import javax.inject.Inject

class PitSystem @Inject constructor(
    private val playerSystem: PlayerSystem,
    private val merchantSystem: MerchantSystem,
    private val cardPullingSystem: CardPullingSystem,
) {
    fun dropCardInHole(latestCard: Int) {
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
            cardPullingSystem.pullNewCard(latestCard)
        } else {
            handleCardDrop(latestCard, bonusScore = 500)
        }
    }

    private fun handleCardDrop(latestCard: Int, bonusScore: Int) {
        playerSystem.removeCardFromDrawDeck(latestCard)
        playerSystem.setLatestCard(-1)
        playerSystem.updateScore(bonusScore)
    }
}