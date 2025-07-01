package com.sq.thed_ck_licker.ecs.systems.cardSystems

import android.util.Log
import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.handleTriggerEffect
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DeathSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DiscardSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.TurnStartSystem
import com.sq.thed_ck_licker.helpers.getRandomElement
import javax.inject.Inject

class CardsSystem @Inject constructor(
    private val playerSystem: PlayerSystem
) {

    fun pullRandomCardFromEntityDeck(entityId: Int): Int {
        val drawDeckComponent = (entityId get DrawDeckComponent::class)
        val deck = drawDeckComponent.getDrawCardDeck()
        return if (deck.isEmpty()) {
            GameEvents.tryEmit(GameEvent.PlayerDied)
            -1
        } else {
            val theCard = deck.getRandomElement()
            drawDeckComponent.removeCard(theCard)
            theCard
        }
    }

    fun cardActivation(playerCardCount: MutableIntState) {
        Log.v("CardsSystem", "Card activation started. Turn started.")
        TurnStartSystem.onTurnStart()
        activateCard(playerCardCount)
        DeathSystem.checkForDeath()
        Log.v("CardsSystem", "Card activation finished. Turn finished.")
    }

    private fun activateCard(playerCardCount: MutableIntState) {
        val latestCard = playerSystem.getLatestCard()

        playerCardCount.intValue += 1
        var latestCardHp: HealthComponent? = null

        try {
            latestCardHp = (latestCard get HealthComponent::class)
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem", "No health component found for $latestCard"
            )
        }

        // The New Era:
        try {
            val context =
                EffectContext(trigger = Trigger.OnPlay, source = latestCard, target = getPlayerID())
            handleTriggerEffect(context)

        } catch (_: IllegalStateException) {
            Log.i(
                "CardSystem",
                "Entity $latestCard has not been converted to new way as they have no TriggerEffect"
            )
        }


        latestCardHp?.apply {
            damage(1f)
            Log.i(
                "CardsSystem",
                "Health is now ${latestCardHp.getHealth()}"
            )
            if (latestCardHp.getHealth() <= 0) {
                playerSystem.setLatestCard(-1)
            }
        } ?: Log.i("CardsSystem", "No health component found for activation")

        try {
            (latestCard get ActivationCounterComponent::class).activate()
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem",
                "No actCounter component found for activation \n" +
                        "Yeah yeah, we get it, you are so cool there was no actCounter component"
            )
        }
        DiscardSystem.handleDiscard(ownerId = getPlayerID(), cardId = latestCard)
        playerSystem.setLatestCard(-1)
    }
}