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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.systems.activationSystem
import com.sq.thed_ck_licker.ecs.systems.pullNewCardSystem
import com.sq.thed_ck_licker.ecs.systems.viewSystems.CardDeck
import com.sq.thed_ck_licker.ecs.systems.viewSystems.PullCardButton
import com.sq.thed_ck_licker.player.HealthBar
import com.sq.thed_ck_licker.player.ScoreDisplayer
import com.sq.thed_ck_licker.ui.theme.viewModels.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem.Companion.instance as playerSystem
import com.sq.thed_ck_licker.ecs.systems.viewSystems.CardDisplaySystem.Companion.instance as cardDisplaySystem
import com.sq.thed_ck_licker.ecs.systems.viewSystems.DeathViewSystem.Companion.instance as deathViewSystem
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun Game(innerPadding: PaddingValues,  gameViewModel: GameViewModel = viewModel()) {
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val playerActiveMerchant = rememberSaveable { playerSystem.getMerchant() }
    val playerCardCount = rememberSaveable { mutableIntStateOf(0) }
    val latestCard = rememberSaveable { mutableIntStateOf(-1) }
    val playerHealth =
        rememberSaveable { playerSystem.getPlayerHealthM() }
    val playerMaxHealth =
        rememberSaveable { playerSystem.getPlayerMaxHealthM() }
    val playerScore = rememberSaveable { playerSystem.getPlayerScoreM() }
    val isPlayerDead by gameViewModel.isPlayerDead.collectAsState()
    val modifier = Modifier

    Column(modifier.fillMaxWidth()) {

        HealthBar(playerHealth, playerMaxHealth, modifier.padding(innerPadding))
        ScoreDisplayer(playerScore.intValue)
        if (playerActiveMerchant.intValue != -1) {
            cardDisplaySystem.CardsOnMerchantHandView(
                playerActiveMerchant,
                modifier,
                latestCard,
                playerScore,
            )
        }

        if (isPlayerDead) {
            deathViewSystem.DeathScreen(
                onRetry = { gameViewModel.restartGame() },
                onQuit = { gameViewModel.exitToMenu() })
        }

        Box(modifier.fillMaxSize()) {
            CardDeck(navigationBarPadding, pullNewCardSystem(latestCard, playerActiveMerchant))
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