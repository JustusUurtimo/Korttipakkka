package com.sq.thed_ck_licker.ecs.systems.cardSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.TargetComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
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
        effects.onDrawn.action(entityId)
        onDeathSystem()
        return theCard

    }

    fun cardActivation(
        ownerId: Int
    ) {
        Log.v("CardsSystem", "Card activation started. Turn started.")
        onTurnStart()
        onTurnStartEffectStackSystem()
        activateCard(ownerId)
        multiSystem.multiplyEntityAgainstOldItself(ownerId)
        multiSystem.addHistoryComponentOfItself(ownerId)
        onDeathSystem()
        Log.v("CardsSystem", "Card activation finished. Turn finished.")
    }

    private fun activateCard(ownerId: EntityId) {
        val ownerInfo = (ownerId get LatestCardComponent::class)
        val latestCard = ownerInfo.getLatestCard()
        if (latestCard == -1) return

        ownerInfo.increaseCardCounter()
        var latestCardHp: HealthComponent? = null

        try {
            latestCardHp = (latestCard get HealthComponent::class)
        } catch (_: IllegalStateException) {
            Log.i("CardsSystem", "No health component found for $latestCard in activateCard")
        }
        var target = try {
            (latestCard get TargetComponent::class).target
        } catch (_: Exception) {
            ownerId
        }


        try {
            (latestCard get EffectComponent::class).onPlay.action(target)
        } catch (_: IllegalStateException) {
            Log.i("CardsSystem", "No effect component found for $latestCard in activateCard")
        }

        latestCardHp?.apply {
            damage(1f)
            Log.i("CardsSystem", "Health is now ${latestCardHp.getHealth()}")
            if (latestCardHp.getHealth() <= 0) {
               ownerInfo.setLatestCard(-1)
            }
        } ?: Log.i("CardsSystem", "No health component found for $latestCard in activateCard")

        try {
            (latestCard get ActivationCounterComponent::class).activate()
        } catch (_: IllegalStateException) {
            Log.i("CardsSystem", "No actCounter component found for $latestCard in activateCard")
        }
        discardSystem(ownerId = ownerId, cardId = latestCard)
        ownerInfo.setLatestCard(-1)
    }

}