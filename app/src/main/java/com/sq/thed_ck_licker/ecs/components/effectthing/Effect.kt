package com.sq.thed_ck_licker.ecs.components.effectthing

import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import kotlin.reflect.KClass

sealed class Effect {
    /*
     * If we end up having multiple things at same effect, this will allow all of them:
     * abstract fun describe(vararg args: Any?): String
     * until then the single version is good enough
     */
    open fun describe(modifiedAmount: Float?): String? = null

    open fun describe(): String? = null

    /**
     * I am not super happy with this.
     * But the idea is that every effect basically always has some base value.
     * So by implementing this the handling can be simplified.
     *
     * I am open to idea of no value ones too.
     * But for now if those are the minority, thy can have some bogus value there...
     */
    open val amount:Float? = null

    open fun describeWithContext(modifiedAmount: Float?): String? {
        return describe(modifiedAmount) ?: describe()
    }


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

    object StoreDamageDealtAsSelfScore : Effect() {
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

    data class AddTempMultiplier(override val amount: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Inject steroids and make more every time you do any thing ($amount times)"
        }
    }

    object Shovel : Effect() {
        override fun describe(): String {
            return "Open the fiery pit of doom!"
        }
    }

    data class OpenMerchant(override val amount: Float, val gameNavigator: GameNavigator) : Effect() {
        override fun describe(): String {
            return "Gain access to a shop"
        }
    }

    data class OnRepeatActivationGainScore(
        override val amount: Float = 3f,
        var current: Int = 0
    ) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Gain ($modifiedAmount) points if you manage to play this $amount ($current/$amount)"
        }
    }

    data class SelfAddEffectsToTrigger(val trigger: Trigger, var effects: List<Effect>): Effect()

    data class CorruptCards(override val amount: Float, val targetDeck: KClass<*>) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Corrupt $modifiedAmount card(s) in ${targetDeck.simpleName}"
        }
    }

    data class AddFlatMultiplier(override val amount: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Gain ($modifiedAmount) Multiplier"
        }
    }

    data class RemoveFlatMultiplier(override val amount: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Lose ($modifiedAmount) Multiplier"
        }
    }

    object None : Effect(){
        override fun describe(modifiedAmount: Float?): String {
            return "Do nothing"
        }
    }

    /**
     * @param amount How many targets to heal
     */
    data class HealEntitiesInDeckToFull(override val amount: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Heal to full ($modifiedAmount targets)"
        }
    }

    data class MultiplyMaxHp(override val amount: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Multiply max health of card by $modifiedAmount"
        }
    }

    /**
     * @param amount How much % to gain as score
     */
    data class GainSelfHpAsScore(override val amount: Float) : Effect() {
        override fun describe(modifiedAmount: Float?): String {
            return "Gain ${amount * 100}% health as score ($modifiedAmount)" //fuq... again the descriptions are not updating...
        }
    }
}