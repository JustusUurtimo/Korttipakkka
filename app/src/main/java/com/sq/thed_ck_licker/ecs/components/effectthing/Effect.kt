package com.sq.thed_ck_licker.ecs.components.effectthing

sealed class Effect {
    /*
     * If we end up having multiple things at same effect, this will allow all of them:
     * abstract fun describe(vararg args: Any?): String
     * until then the single version is good enough
     */
    abstract fun describe(modifiedAmount: Number): String


    data class GainScore(val amount: Int) : Effect(){
        override fun describe(modifiedAmount: Number): String { //Not super sure about this one...
            return "Gain ($modifiedAmount) points"
        }
    }

    data class GainHealth(val amount: Number) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Heal ($modifiedAmount)"
        }
    }

    data class TakeDamage(val amount: Number) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Take damage ($modifiedAmount)"
        }
    }
}