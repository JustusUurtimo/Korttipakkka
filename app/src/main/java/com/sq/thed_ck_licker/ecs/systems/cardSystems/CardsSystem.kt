package com.sq.thed_ck_licker.ecs.systems.cardSystems

import android.util.Log
import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.MultiplierSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.discardSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.onDeathSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.onTurnStart
import com.sq.thed_ck_licker.ecs.systems.helperSystems.onTurnStartEffectStackSystem
import com.sq.thed_ck_licker.helpers.getRandomElement
import javax.inject.Inject

class CardsSystem @Inject constructor(
    private var multiSystem: MultiplierSystem,
    private val playerSystem: PlayerSystem
) {

    fun pullRandomCardFromEntityDeck(entityId: Int): Int {
        val drawDeckComponent = (entityId get DrawDeckComponent::class)
        val deck = drawDeckComponent.getDrawCardDeck()

        if (deck.isEmpty()) {
            GameEvents.tryEmit(GameEvent.PlayerDied)
            return -1
        }

        val theCard = deck.getRandomElement()
        drawDeckComponent.removeCard(theCard)
        val effects = (theCard get EffectComponent::class)
        effects.onDrawn(entityId)
        return theCard

    }

    fun cardActivation(
        playerCardCount: MutableIntState
    ) {
        Log.v("CardsSystem", "Card activation started. Turn started.")
        onTurnStart()
        onTurnStartEffectStackSystem()
        activateCard(playerCardCount)
        try {
        multiSystem.multiplyEntityAgainstOldItself(getPlayerID())
        multiSystem.addHistoryComponentOfItself(getPlayerID())
        } catch (e: IllegalStateException) {
            Log.i("CardsSystem", "No multiplier component found for activation")
            Log.i("CardsSystem", e.message.toString())
        }
        onDeathSystem()
        Log.v("CardsSystem", "Card activation finished. Turn finished.")
    }

    private fun activateCard(playerCardCount: MutableIntState) {
        val latestCard = playerSystem.getLatestCard()
        if (latestCard == -1) return

        playerCardCount.intValue += 1
        var latestCardHp: HealthComponent? = null

        try {
            latestCardHp = (latestCard get HealthComponent::class)
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem",
                "No health component found for activation \n" +
                        "Yeah yeah, we get it, you are so cool there was no health component"
            )
        }


        try {
            (latestCard get EffectComponent::class).onPlay.action.invoke(getPlayerID())
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem",
                "No effect component found for activation \n" +
                        "Yeah yeah, we get it, you are so cool there was no effect component"
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
        discardSystem(ownerId = getPlayerID(), cardId = latestCard)
        playerSystem.setLatestCard(-1)
    }

}