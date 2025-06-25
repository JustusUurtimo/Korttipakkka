package com.sq.thed_ck_licker.ecs.components.effectthing

sealed class Effect {
    data class GainScore(val amount: Int) : Effect()
}