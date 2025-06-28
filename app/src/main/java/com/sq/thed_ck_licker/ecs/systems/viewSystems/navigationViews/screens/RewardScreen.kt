package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.systems.viewSystems.CardRow
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import com.sq.thed_ck_licker.viewModels.RewardViewModel

@Composable
fun RewardScreen(
    modifier: Modifier,
    rewardViewModel: RewardViewModel,
    gameNavigator: GameNavigator
) {

    val rewardCards by rewardViewModel.rewardSelection.collectAsState()
    val zoomedCardId = rememberSaveable { mutableIntStateOf(-1) }
    val isZoomed = zoomedCardId.intValue != -1

    if (rewardCards.isEmpty()) {
        rewardViewModel.getRewardCards()
    }

    fun selectReward(chosenCard: Int) {
        rewardViewModel.selectReward(chosenCard)
        gameNavigator.navigateBack()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .wrapContentSize(Alignment.Center)
        ) {
            Row {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .pointerInput(isZoomed) {
                            if (isZoomed) {
                                detectTapGestures { zoomedCardId.intValue = -1 }
                            }
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Reward Screen",
                            modifier = modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Choose a card from reward cards",
                            softWrap = true,
                            modifier = modifier
                                .fillMaxWidth()
                        )
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CardRow(
                            cardSize = DpSize(100.dp, 150.dp),
                            zoomedCardId,
                            rewardCards,
                            onCardClick = { zoomedCardId.intValue = it },
                            onZoomChange = { zoom ->
                                zoomedCardId.intValue = zoom
                            },
                            modifier
                        )

                        if (isZoomed) {
                            Spacer(modifier = Modifier.height(100.dp))
                            Button(
                                modifier = modifier.align(Alignment.CenterHorizontally),
                                onClick = { selectReward(zoomedCardId.intValue) }
                            ) { Text("Select Reward") }

                        }
                    }

                }

            }
        }
    }


}