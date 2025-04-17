package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.systems.helperSystems.onDeathSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.onTurnStartEffectStackSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem.Companion.instance as cardsSystem

fun activationSystem(
    latestCard: MutableIntState,
    playerCardCount: MutableIntState
): () -> Unit = {
    onTurnStartEffectStackSystem()
    cardsSystem.activateCard(latestCard, playerCardCount)
    onDeathSystem()
}