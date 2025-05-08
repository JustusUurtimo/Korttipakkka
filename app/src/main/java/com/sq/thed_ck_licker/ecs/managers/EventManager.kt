package com.sq.thed_ck_licker.ecs.managers

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

//todo we can expand this later to include events like onGamePaused etc.

sealed class GameEvent {
    object PlayerDied : GameEvent()
    object ShovelUsed : GameEvent()
}

object GameEvents {
    // MutableSharedFlow allows emitting events
    private val _eventStream = MutableSharedFlow<GameEvent>(
        replay = 0, // No replay for new collectors
        extraBufferCapacity = 64 // Buffer up to 64 events
    )

    // Publicly exposed as a read-only SharedFlow
    val eventStream = _eventStream.asSharedFlow()

    suspend fun emitSuspendingEvent(event: GameEvent) {
        _eventStream.emit(event)
    }

    fun tryEmitEvent(event: GameEvent) {
        _eventStream.tryEmit(event)
    }
}