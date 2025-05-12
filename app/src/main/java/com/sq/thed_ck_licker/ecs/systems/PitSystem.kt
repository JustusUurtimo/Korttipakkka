package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.runtime.MutableIntState
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
    fun dropCardInHole(latestCard: MutableIntState) {
        if (latestCard.intValue == -1) return
        val latestCardId = latestCard.intValue
        val tagsComponent = latestCardId get TagsComponent::class

        if (tagsComponent.cardIsMerchant()) {
            handleMerchantCard(latestCard)
        } else {
            handleRegularCard(latestCard, bonusScore = 200)
        }
    }

    // Helper Methods
    private fun handleMerchantCard(latestCard: MutableIntState) {
        val cardId = latestCard.intValue
        if (MyRandom.getRandomInt() <= 30) {
            val merchantId: Int? = (cardId get IdentificationComponent::class).getCharacterId()
            merchantId?.let {
                merchantSystem.updateMerchantAffinity(-500, it)
            }
            cardPullingSystem.pullNewCard(latestCard)
        } else {
            handleRegularCard(latestCard, bonusScore = 500)
        }
    }

    private fun handleRegularCard(latestCard: MutableIntState, bonusScore: Int) {
        val cardId = latestCard.intValue
        playerSystem.removeCardFromDrawDeck(cardId)
        latestCard.intValue = -1
        playerSystem.updateScore(bonusScore)
    }
}