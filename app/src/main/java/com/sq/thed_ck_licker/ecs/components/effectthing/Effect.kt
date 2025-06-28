package com.sq.thed_ck_licker.ecs.components.effectthing

sealed class Effect {
    /*
     * If we end up having multiple things at same effect, this will allow all of them:
     * abstract fun describe(vararg args: Any?): String
     * until then the single version is good enough
     */
    abstract fun describe(modifiedAmount: Number): String

    /**
     * I am not super happy with this.
     * But the idea is that every effect basically always has some base value.
     * So by implementing this the handling can be simplified.
     *
     * I am open to idea of no value ones too.
     * But for now if those are the minority, thy can have some bogus value there...
     */
    abstract val amount:Float


    data class GainScore(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String { //Not super sure about this one...
            return "Gain ($modifiedAmount) points"
        }
    }

    data class GainHealth(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Heal ($modifiedAmount)"
        }
    }

    data class TakeDamage(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Take damage ($modifiedAmount)"
        }
    }

    data class GainMaxHealth(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Gain ($modifiedAmount) max health"
        }
    }

    data class TakeDamagePercentage(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Take damage ($modifiedAmount%) of your current health"
        }
    }

    data class TakeDamageOrGainMaxHP(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Gain ($modifiedAmount) max health or might explode"
        }
    }

    data class HealOnUnderThreshold(override val amount: Float, val threshold: Float) : Effect() {
        override fun describe(modifiedAmount: Number): String {
            return "Heal ($modifiedAmount) if health is under $threshold"
        }

    }

    /**
     * Differs from the Take damage as the source takes this damage instead of the target
     */
    data class TakeSelfDamage(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Take self damage ($modifiedAmount)"
        }
    }

    data class AddMultiplier(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Gain ($modifiedAmount) Multiplier"
        }
    }

    data class RemoveMultiplier(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Number): String {
            return "Lose ($modifiedAmount) Multiplier"
        }
    }
}