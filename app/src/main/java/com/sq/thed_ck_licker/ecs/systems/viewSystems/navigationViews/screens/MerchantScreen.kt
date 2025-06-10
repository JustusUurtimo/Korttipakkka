package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.systems.viewSystems.MerchantHandView
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import com.sq.thed_ck_licker.helpers.navigation.Screen
import com.sq.thed_ck_licker.viewModels.MerchantViewModel

@Composable
fun MerchantScreen(
    modifier: Modifier,
    merchantViewModel: MerchantViewModel,
    gameNavigator: GameNavigator
) {
    val activeMerchant by merchantViewModel.activeMerchantId.collectAsState()
    val merchantSummonCard by merchantViewModel.merchantSummonCard.collectAsState()
    val merchantHand by merchantViewModel.merchantHand.collectAsState()
    val merchantState by merchantViewModel.merchantState.collectAsState()

    fun chooseMerchantCard(newCard: Int) {
        merchantViewModel.onChooseMerchantCard(newCard, activeMerchant)
        gameNavigator.navigateBack()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Column(modifier = modifier
            .padding(16.dp)
            .wrapContentSize(Alignment.Center)) {
            Row {
                MerchantHandView(
                    modifier,
                    merchantHand,
                    chooseMerchantCard = { chooseMerchantCard(it) },
                    onReRollShop = {
                        merchantViewModel.onReRollShop(
                            merchantSummonCard,
                            activeMerchant
                        )
                    },
                    onOpenShop = { merchantViewModel.onOpenShop(activeMerchant) }
                )
                if (merchantState.affinity < -50) {
                    Text(text = "MERCHANT BIG MAD :D")
                    Text(text = "Everything more expensive :D Affinity: ${merchantState.affinity}")
                }
            }

            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = { gameNavigator.navigateBack() }
            ) { Text("Leave shop") }
        }

    }
}