package com.sq.thed_ck_licker.ecs.components.effectthing

sealed class Effect {
    /*
     * If we end up having multiple things at same effect, this will allow all of them:
     * abstract fun describe(vararg args: Any?): String
     * until then the single version is good enough
     */
    open fun describe(modifiedAmount: Float?): String? = null

    /**
     * I am not super happy with this.
     * But the idea is that every effect basically always has some base value.
     * So by implementing this the handling can be simplified.
     *
     * I am open to idea of no value ones too.
     * But for now if those are the minority, thy can have some bogus value there...
     */
    open val amount:Float? = null


    data class GainScore(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Float?): String { //Not super sure about this one...
            return "Gain ($modifiedAmount) points"
        }
    }
    object GainScoreFromScoreComp: Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Gain ($modifiedAmount) points"
        }
    }

    data class GainHealth(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Float?): String {
            return "Heal ($modifiedAmount)"
        }
    }

    data class TakeDamage(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Float?): String {
            return "Take damage ($modifiedAmount)"
        }
    }

    data class GainMaxHealth(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Float?): String {
            return "Gain ($modifiedAmount) max health"
        }
    }

    data class TakeDamagePercentage(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Float?): String {
            return "Take damage ($modifiedAmount%) of your current health"
        }
    }

    data class TakeDamageOrGainMaxHP(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Float?): String {
            return "Gain ($modifiedAmount) max health or might explode"
        }
    }

    data class HealOnUnderThreshold(override val amount: Float, val threshold: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Heal ($modifiedAmount) if health is under $threshold"
        }

    }

    /**
     * Differs from the Take damage as the source takes this damage instead of the target
     */
    data class TakeSelfDamage(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Float?): String {
            return "Take self damage ($modifiedAmount)"
        }
    }

    data class AddMultiplier(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Float?): String {
            return "Gain ($modifiedAmount) Multiplier"
        }
    }

    data class RemoveMultiplier(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Float?): String {
            return "Lose ($modifiedAmount) Multiplier"
        }
    }

    data class TakeRisingDamage(override var amount: Float, var risingAmount: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Take ($modifiedAmount) rising damage."
        }
    }

    data class TakeRisingScore(override var amount: Float, var risingAmount: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Take ($modifiedAmount) rising score damage."
        }
    }

    class StoreDamageDealtAsSelfScore() : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Store ($modifiedAmount) points to self."
        }
    }

    data class GainScalingScore(override val amount: Float, var scalingFactor: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Gain ${scalingFactor}x($modifiedAmount) points"
        }
    }

    data class ResetSelfScore(override val amount: Float = 0f) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Set Score to $modifiedAmount"
        }
    }

    data class AddSelfMultiplier(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Float?): String {
            return "Gain ($modifiedAmount) self multiplier"
        }
    }

    data class RemoveSelfMultiplier(override val amount: Float) : Effect(){
        override fun describe(modifiedAmount: Float?): String {
            return "Lose ($modifiedAmount) self multiplier"
        }
    }

    data class ResetTakeRisingDamage(override var amount: Float, var risingAmount: Float) : Effect() { //Not happy about this one... at all
        override fun describe(modifiedAmount: Float?): String {
            return "Reset rising damage to $modifiedAmount"
        }
    }

    data class AddScoreGainer(override val amount: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Gain score gainer"
        }
    }

    data class AddBeerGoggles(override val amount: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Equip Beer Goggles that will heal you bit (up to $modifiedAmount health points)"
        }
    }

}