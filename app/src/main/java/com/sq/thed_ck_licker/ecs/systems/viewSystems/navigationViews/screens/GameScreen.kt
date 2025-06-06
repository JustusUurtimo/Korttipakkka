package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sq.thed_ck_licker.ecs.systems.helperSystems.TickingSystem
import com.sq.thed_ck_licker.ecs.systems.viewSystems.CardDeck
import com.sq.thed_ck_licker.ecs.systems.viewSystems.DeathScreen
import com.sq.thed_ck_licker.ecs.systems.viewSystems.HoleView
import com.sq.thed_ck_licker.ecs.systems.viewSystems.PlayerHandView
import com.sq.thed_ck_licker.ecs.systems.viewSystems.PullCardButton
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.areRealTimeThingsEnabled
import com.sq.thed_ck_licker.player.HealthBar
import com.sq.thed_ck_licker.player.ScoreDisplay
import com.sq.thed_ck_licker.viewModels.GameViewModel
import com.sq.thed_ck_licker.viewModels.PlayerViewModel
import kotlinx.coroutines.delay

@Composable
fun Game(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    gameViewModel: GameViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel(),
) {
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val playerCardCount = rememberSaveable { mutableIntStateOf(0) }
    val isPlayerDead by gameViewModel.isPlayerDead.collectAsState()
    val isShovelUsed by gameViewModel.isShovelUsed.collectAsState()
    val playerState by playerViewModel.playerState.collectAsState()

    RealtimeEffects()

    Column(modifier.fillMaxWidth()) {
        HealthBar(playerState.health, playerState.maxHealth, modifier.padding(innerPadding))
        ScoreDisplay(playerState.score)
        if (isPlayerDead) {
            DeathScreen(
                onRetry = { gameViewModel.restartGame() })
        }
        if (isShovelUsed) {
            HoleView(
                modifier,
                onClickListener = { gameViewModel.dropCardInHole(playerState.latestCard) })
        }

        Box(modifier.fillMaxSize()) {
            CardDeck(navigationBarPadding) { playerViewModel.onPullNewCard(playerState.latestCard) }
            Box(modifier.align(Alignment.BottomCenter)) {

                Column(modifier.padding(35.dp, 0.dp, 0.dp, 0.dp)) {

                    if (playerState.latestCard != -1) {
                        PlayerHandView(
                            playerCardCount,
                            modifier,
                            playerState.latestCard,
                            activateCard = {
                                playerViewModel.onActivateCard(
                                    playerCardCount
                                )
                            }
                        )
                    }
                    PullCardButton(
                        navigationBarPadding,
                        modifier.offset((-15).dp),
                        pullNewCard = { playerViewModel.onPullNewCard(playerState.latestCard) }
                    )
                }

            }
        }
    }
}

@Composable
private fun RealtimeEffects() {
    var realTimeTickingEnabled by remember { areRealTimeThingsEnabled }
    val tickSystem = TickingSystem()
    val tickSize = 100
    LaunchedEffect(realTimeTickingEnabled) {
        while (realTimeTickingEnabled) {
            delay(tickSize.toLong())
            tickSystem.tick(tickSize)
        }
    }
}