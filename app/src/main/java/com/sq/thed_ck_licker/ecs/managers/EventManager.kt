package com.sq.thed_ck_licker.ecs.managers

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

//todo we can expand this later to include events like onGamePaused etc.


sealed class GameEvent {
    object PlayerDied : GameEvent()
    object ShovelUsed : GameEvent()
}

sealed class MerchantEvent {
    data class MerchantShopOpened(val merchantId: Int, val cardEntity: Int) : MerchantEvent()
    object MerchantShopClosed : MerchantEvent()
}


// Event Buses
object GameEvents {
    private val _eventStream = MutableSharedFlow<GameEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )
    val eventStream = _eventStream.asSharedFlow()

    fun tryEmit(event: GameEvent) {
        _eventStream.tryEmit(event)
    }

    suspend fun emit(event: GameEvent) {
        _eventStream.emit(event)
    }
}

object MerchantEvents {
    private val _eventStream = MutableSharedFlow<MerchantEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )
    val eventStream = _eventStream.asSharedFlow()

    fun tryEmit(event: MerchantEvent) {
        _eventStream.tryEmit(event)
    }

    suspend fun emit(event: MerchantEvent) {
        _eventStream.emit(event)
    }
}