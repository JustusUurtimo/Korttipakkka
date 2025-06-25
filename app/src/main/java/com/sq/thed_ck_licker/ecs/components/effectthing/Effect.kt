package com.sq.thed_ck_licker.ecs.components.effectthing

sealed class Effect {
    data class GainScore(val amount: Int) : Effect(){
        override fun toString(): String {
            return "Gain $amount points" // Going back and forth if this is the correct way or should the handler do this too...
        }
    }
}