package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.navigation.compose.hiltViewModel
import com.sq.thed_ck_licker.ecs.systems.helperSystems.TickingSystem
import com.sq.thed_ck_licker.ecs.systems.viewSystems.CardDeck
import com.sq.thed_ck_licker.ecs.systems.viewSystems.PlayerHandView
import com.sq.thed_ck_licker.ecs.systems.viewSystems.PullCardButton
import com.sq.thed_ck_licker.player.AdditionalInfoDisplay
import com.sq.thed_ck_licker.player.HealthBar
import com.sq.thed_ck_licker.player.ScoreDisplay
import com.sq.thed_ck_licker.viewModels.PlayerViewModel
import com.sq.thed_ck_licker.viewModels.SettingsViewModel
import kotlinx.coroutines.delay

@Composable
fun Game(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    playerViewModel: PlayerViewModel = hiltViewModel(),
) {
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val playerCardCount = rememberSaveable { mutableIntStateOf(0) }
    val playerState by playerViewModel.playerState.collectAsState()

    val zoomedCardId = rememberSaveable { mutableIntStateOf(-1) }
    val isZoomed = zoomedCardId.intValue != -1

    RealtimeEffects()

    Column(modifier.fillMaxWidth()) {
        HealthBar(playerState.health, playerState.maxHealth, modifier.padding(innerPadding))
        ScoreDisplay(playerState.score)
        AdditionalInfoDisplay(playerState.latestCard)

        Box {
            CardDeck(
                navigationBarPadding,
                playerCardCount.intValue
            ) { playerViewModel.onPullNewCard(playerState.latestCard) }
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .pointerInput(isZoomed) {
                        if (isZoomed) {
                            detectTapGestures { zoomedCardId.intValue = -1 }
                        }
                    }
            ) {
                Box(
                    modifier
                        .align(if (isZoomed) Alignment.Center else Alignment.BottomCenter)
                ) {
                    Column {
                        if (playerState.latestCard != -1) {
                            PlayerHandView(
                                isZoomed,
                                modifier,
                                playerState.latestCard,
                                activateCard = {
                                    playerViewModel.onActivateCard(
                                        playerCardCount
                                    )
                                },
                                onZoomChange = { zoom ->
                                    zoomedCardId.intValue = zoom
                                }
                            )
                        }
                        if (!isZoomed) {

                            PullCardButton(
                                navigationBarPadding,
                                modifier,
                                pullNewCard = { playerViewModel.onPullNewCard(playerState.latestCard) }
                            )

                        }
                    }

                }
                if (isZoomed) {
                    Box(
                        modifier.align(Alignment.BottomCenter)

                    ) {
                        PullCardButton(
                            navigationBarPadding,
                            modifier,
                            pullNewCard = { playerViewModel.onPullNewCard(playerState.latestCard) }
                        )
                    }
                }

            }
        }
    }
}


@Composable
private fun RealtimeEffects(settingsViewModel: SettingsViewModel = hiltViewModel()) {
    val realTimePlayerDamageEnabled by settingsViewModel.realTimePlayerDamageEnabled.collectAsState()
    val tickSize = 100
    LaunchedEffect(true) {
        while (true) {
            delay(tickSize.toLong())
            TickingSystem.tick(tickSize, realTimePlayerDamageEnabled)
        }
    }
}