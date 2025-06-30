package com.sq.thed_ck_licker.ecs.components

data class MultiplierComponent(var multiplier: Float = 1f): Component {

    fun timesMultiplier(multiplierToTimes: Float) {
        this.multiplier *= multiplierToTimes
    }

    fun removeMultiplier(multiplierToRemove: Float) {
        this.multiplier *= 1 / multiplierToRemove
    }

    fun increaseMultiplier(multiplierToAdd: Float) {
        this.multiplier += multiplierToAdd
    }

    fun decreaseMultiplier(multiplierToAdd: Float) = increaseMultiplier(-multiplierToAdd)
}