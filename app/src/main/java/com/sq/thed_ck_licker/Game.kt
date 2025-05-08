package com.sq.thed_ck_licker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sq.thed_ck_licker.ecs.systems.viewSystems.CardDeck
import com.sq.thed_ck_licker.ecs.systems.viewSystems.PullCardButton
import com.sq.thed_ck_licker.player.HealthBar
import com.sq.thed_ck_licker.player.ScoreDisplay
import com.sq.thed_ck_licker.viewModels.GameViewModel
import com.sq.thed_ck_licker.viewModels.MerchantViewModel
import com.sq.thed_ck_licker.viewModels.PlayerViewModel
import com.sq.thed_ck_licker.ecs.systems.viewSystems.DeathScreen
import com.sq.thed_ck_licker.ecs.systems.viewSystems.MerchantHandView
import com.sq.thed_ck_licker.ecs.systems.viewSystems.PlayerHandView

@Composable
fun Game(
    innerPadding: PaddingValues,
    gameViewModel: GameViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel(),
    merchantViewModel: MerchantViewModel = hiltViewModel(),
) {
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val playerCardCount = rememberSaveable { mutableIntStateOf(0) }
    val latestCard = rememberSaveable { mutableIntStateOf(-1) }
    val isPlayerDead by gameViewModel.isPlayerDead.collectAsState()
    val isShovelUsed by gameViewModel.isShovelUsed.collectAsState()
    val playerState by playerViewModel.playerState.collectAsState()
    val merchantHand by merchantViewModel.merchantHand.collectAsState()
    val modifier = Modifier

    Column(modifier.fillMaxWidth()) {

        HealthBar(playerState.health, playerState.maxHealth, modifier.padding(innerPadding))
        ScoreDisplay(playerState.score)
        if (playerState.merchantId != -1) {
            MerchantHandView(
                modifier,
                merchantHand,
                chooseMerchantCard = { merchantViewModel.onChooseMerchantCard(latestCard, it) },
                onReRollShop = { merchantViewModel.onReRollShop() },
                onOpenShop = { merchantViewModel.onOpenShop() }
            )
        }

        if (isPlayerDead) {
            DeathScreen(
                onRetry = { gameViewModel.restartGame() },
                onQuit = { gameViewModel.exitToMenu() })
        }
        if (isShovelUsed) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .background(color = Color.Magenta)
                    .paint(
                        painterResource(R.drawable.placeholder),
                        contentScale = ContentScale.FillBounds
                    )
                    .clickable { gameViewModel.dropCardInHole(latestCard) }
            ) {
                Column(
                    modifier = Modifier
                        .align(BiasAlignment(0f, 0.7f))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "hole",
                        modifier = Modifier
                            .background(color = Color.Cyan)
                            .fillMaxWidth()
                    )
                    Text(
                        text = "Drop card in hole",
                        softWrap = true,
                        modifier = Modifier
                            .background(color = Color.Yellow)
                            .fillMaxWidth()
                    )
                }
            }
        }

        Box(modifier.fillMaxSize()) {
            CardDeck(navigationBarPadding) { playerViewModel.onPullNewCard(latestCard) }
            Box(modifier.align(Alignment.BottomCenter)) {

                Column(modifier.padding(35.dp, 0.dp, 0.dp, 0.dp)) {

                    if (latestCard.intValue != -1) {
                        PlayerHandView(
                            playerCardCount,
                            modifier,
                            latestCard,
                            activateCard = {
                                playerViewModel.onActivateCard(
                                    latestCard,
                                    playerCardCount
                                )
                            }
                        )
                    }
                    PullCardButton(
                        navigationBarPadding,
                        modifier.offset((-15).dp),
                        pullNewCard = { playerViewModel.onPullNewCard(latestCard) }
                    )
                }

            }
        }
    }
}