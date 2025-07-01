package com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.managers.MerchantEvent
import com.sq.thed_ck_licker.ecs.managers.MerchantEvents
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import com.sq.thed_ck_licker.helpers.navigation.Screen

data class OpenMerchant(override val amount: Float, val gameNavigator: GameNavigator) :
    MiscEffect() {
    override fun describe(): String {
        return "Gain access to a shop"
    }

    override fun execute(context: EffectContext): Float {
        MerchantEvents.tryEmit(
            MerchantEvent.MerchantShopOpened(
                this.amount.toInt(), context.source
            )
        )
        this.gameNavigator.navigateTo(Screen.MerchantShop.route)
        return 0f
    }
}