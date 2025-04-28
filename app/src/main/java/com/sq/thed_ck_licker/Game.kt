package com.sq.thed_ck_licker

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sq.thed_ck_licker.ecs.systems.activationSystem
import com.sq.thed_ck_licker.ecs.systems.pullNewCardSystem
import com.sq.thed_ck_licker.ecs.systems.viewSystems.CardDeck
import com.sq.thed_ck_licker.ecs.systems.viewSystems.PullCardButton
import com.sq.thed_ck_licker.player.HealthBar
import com.sq.thed_ck_licker.player.ScoreDisplay
import com.sq.thed_ck_licker.ui.theme.viewModels.GameViewModel
import com.sq.thed_ck_licker.ui.theme.viewModels.MerchantViewModel
import com.sq.thed_ck_licker.ui.theme.viewModels.PlayerViewModel
import com.sq.thed_ck_licker.ecs.systems.viewSystems.CardDisplaySystem.Companion.instance as cardDisplaySystem
import com.sq.thed_ck_licker.ecs.systems.viewSystems.DeathViewSystem.Companion.instance as deathViewSystem

@Composable
fun Game(
    innerPadding: PaddingValues,
    gameViewModel: GameViewModel = viewModel(),
    playerViewModel: PlayerViewModel = viewModel(),
    merchantViewModel: MerchantViewModel = viewModel(),
) {
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val playerCardCount = rememberSaveable { mutableIntStateOf(0) }
    val latestCard = rememberSaveable { mutableIntStateOf(-1) }
    val isPlayerDead by gameViewModel.isPlayerDead.collectAsState()
    val playerState by playerViewModel.playerState.collectAsState()
    val merchantHand by merchantViewModel.merchantHand.collectAsState()
    val modifier = Modifier

    Column(modifier.fillMaxWidth()) {

        HealthBar(playerState.health, playerState.maxHealth, modifier.padding(innerPadding))
        ScoreDisplay(playerState.score)
        if (playerState.merchantId != -1) {
            cardDisplaySystem.CardsOnMerchantHandView(
                playerState.merchantId,
                modifier,
                latestCard,
                playerState.score,
                onRerollShop = { merchantViewModel.onReRollShop() }
            )
        }

        if (isPlayerDead) {
            deathViewSystem.DeathScreen(
                onRetry = { gameViewModel.restartGame() },
                onQuit = { gameViewModel.exitToMenu() })
        }

        Box(modifier.fillMaxSize()) {
            CardDeck(navigationBarPadding, pullNewCardSystem(latestCard, playerState.merchantId))
            Box(modifier.align(Alignment.BottomCenter)) {

                Column(modifier.padding(35.dp, 0.dp, 0.dp, 0.dp)) {

                    if (latestCard.intValue != -1) {
                        cardDisplaySystem.CardsOnPlayerHandView(
                            playerCardCount,
                            modifier,
                            latestCard,
                            activationSystem(latestCard, playerCardCount)
                        )
                    }
                    PullCardButton(
                        navigationBarPadding,
                        modifier.offset((-15).dp),
                        pullNewCardSystem(latestCard, playerActiveMerchant)
                    )
                }

            }
        }
    }
}