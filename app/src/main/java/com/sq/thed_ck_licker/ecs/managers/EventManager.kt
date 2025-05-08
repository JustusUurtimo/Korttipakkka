package com.sq.thed_ck_licker.ecs.managers

import kotlinx.coroutines.flow.MutableSharedFlow

//todo we can expand this later to include events like onGamePaused etc.
object GameEvents {
    val onPlayerDied = MutableSharedFlow<Unit>(replay = 1)
    val onShovelUsed = MutableSharedFlow<Unit>(replay = 1)
}