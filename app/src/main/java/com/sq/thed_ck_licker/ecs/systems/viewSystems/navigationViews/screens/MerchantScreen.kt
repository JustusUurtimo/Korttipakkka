package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sq.thed_ck_licker.ecs.systems.viewSystems.MerchantHandView
import com.sq.thed_ck_licker.viewModels.MerchantViewModel
import com.sq.thed_ck_licker.viewModels.PlayerViewModel

@Composable
fun MerchantScreen(
    modifier: Modifier,
    latestCard: MutableIntState,
    playerViewModel: PlayerViewModel = hiltViewModel(),
    merchantViewModel: MerchantViewModel = hiltViewModel()
) {
    val playerState by playerViewModel.playerState.collectAsState()
    val activeMerchant by merchantViewModel.activeMerchantId.collectAsState()
    val merchantSummonCard by merchantViewModel.merchantSummonCard.collectAsState()
    val merchantHand by merchantViewModel.merchantHand.collectAsState()
    val merchantState by merchantViewModel.merchantState.collectAsState()

    MerchantHandView(
        modifier,
        merchantHand,
        chooseMerchantCard = {
            merchantViewModel.onChooseMerchantCard(
                latestCard,
                it,
                activeMerchant
            )
        },
        onReRollShop = { merchantViewModel.onReRollShop(merchantSummonCard, activeMerchant) },
        onOpenShop = { merchantViewModel.onOpenShop(activeMerchant) }
    )
    if (merchantState.affinity < -50) {
        Text(text = "MERCHANT BIG MAD :D")
        Text(text = "Everything more expensive :D Affinity: ${merchantState.affinity}")
    }
}