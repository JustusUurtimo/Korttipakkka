package com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems2

data class AddTempMultiplier(override val amount: Float) : MultiplierEffect() {
    override fun describe(modifiedAmount: Float?): String {
        return "Inject steroids and make more every time you do any thing ($amount times)"
    }

    override fun execute(context: EffectContext): Float {
        CardCreationHelperSystems2.addTemporaryMultiplierTo(
            context.target, this.amount
        )
        return this.amount
    }
}