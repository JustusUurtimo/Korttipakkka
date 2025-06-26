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

    data class GainHealth(val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Heal ($modifiedAmount)"
        }
    }

    data class TakeDamage(val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Take damage ($modifiedAmount)"
        }
    }

    data class GainMaxHealth(val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Gain ($modifiedAmount) max health"
        }
    }

    data class TakeDamagePercentage(val percentage: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Take damage ($modifiedAmount%) of your current health"
        }
    }

    data class TakeDamageOrGainMaxHP(val maxHp: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Gain ($modifiedAmount) max health or might explode"
        }
    }



}