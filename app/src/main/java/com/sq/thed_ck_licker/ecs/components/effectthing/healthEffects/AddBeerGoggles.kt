package com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems2

data class AddBeerGoggles(override val amount: Float) : HealthEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Equip Beer Goggles that will heal you bit (up to $modifiedAmount health points)"
    }

    override fun execute(context: EffectContext): Float {
        CardCreationHelperSystems2.addLimitedSupplyAutoHealToEntity(
            context.target, this.amount
        )
        return this.amount
    }
}