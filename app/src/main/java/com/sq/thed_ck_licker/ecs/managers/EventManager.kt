package com.sq.thed_ck_licker.ecs.managers

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


sealed class GameEvent {
    object PlayerDied : GameEvent()
    object ShovelUsed : GameEvent()
}

sealed class MerchantEvent {
    data class MerchantShopOpened(val merchantId: Int, val cardEntity: Int) : MerchantEvent()
}


// Event Buses
object GameEvents {
    private val _eventStream = MutableSharedFlow<GameEvent>(
        replay = 1,
        extraBufferCapacity = 10
    )
    val eventStream = _eventStream.asSharedFlow()

    fun tryEmit(event: GameEvent) {
        _eventStream.tryEmit(event)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun resetEventStream() {
        _eventStream.resetReplayCache()
    }
}

object MerchantEvents {
    private val _eventStream = MutableSharedFlow<MerchantEvent>(
        replay = 1,
        extraBufferCapacity = 10
    )
    val eventStream = _eventStream.asSharedFlow()

    fun tryEmit(event: MerchantEvent) {
        _eventStream.tryEmit(event)
    }

    suspend fun emit(event: MerchantEvent) {
        _eventStream.emit(event)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun resetEventStream() {
        _eventStream.resetReplayCache()
    }
}