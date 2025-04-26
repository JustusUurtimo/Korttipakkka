package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.systems.helperSystems.multiplyEntityValues
import com.sq.thed_ck_licker.ecs.systems.helperSystems.onDeathSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.onTurnStartEffectStackSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem.Companion.instance as cardsSystem

fun activationSystem(
    latestCard: MutableIntState,
    playerCardCount: MutableIntState
): () -> Unit = {
    val oldPlayer = ComponentManager.componentManager.copy(getPlayerID())
    onTurnStartEffectStackSystem()
    cardsSystem.activateCard(latestCard, playerCardCount)
    onDeathSystem()
    multiplyEntityValues(oldEntityId = oldPlayer, targetEntityId = getPlayerID())
}