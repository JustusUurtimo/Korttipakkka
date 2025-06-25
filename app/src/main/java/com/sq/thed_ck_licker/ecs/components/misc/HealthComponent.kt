package com.sq.thed_ck_licker.ecs.components.misc

import android.util.Log
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import com.sq.thed_ck_licker.ecs.components.Component

data class HealthComponent(
    private var health: MutableFloatState,
    private var maxHealth: MutableFloatState
) : Component {
    /**
     * This can be used in cases where you want thing to be not on full hp at the start
     */
    constructor(health: Number = 100f, maxHealth: Number = 100f) : this(
        mutableFloatStateOf(health.toFloat()),
        mutableFloatStateOf(maxHealth.toFloat())
    )

    /**
     * This can be used to construct Health component with health same as max health
     */
    constructor(maxHealth: Number = 100f) : this(
        mutableFloatStateOf(maxHealth.toFloat()),
        mutableFloatStateOf(maxHealth.toFloat())
    )
    fun setHealth(health: Number) {
        this.health.floatValue = health.toFloat()
    }

    fun getHealth(): Float {
        return this.health.floatValue
    }

    fun getMaxHealth(): Float {
        return this.maxHealth.floatValue
    }

    fun increaseMaxHealth(amount: Number) {
        this.maxHealth.floatValue += amount.toFloat()
    }

    private var lastTime = -1L
    fun add(amount: Number) {
        var amountFloat = amount.toFloat()
        var logging = "This is going to be modified $this"
        if ((this.health.floatValue + amountFloat) > this.maxHealth.floatValue) {
            this.health.floatValue = this.maxHealth.floatValue
        } else {
            this.health.floatValue += amountFloat
        }
        logging += "and the end result is $this"

        val now = System.currentTimeMillis()
        if (lastTime + 5000 < now) {
            lastTime = now
            Log.i("HealthComponent", logging)
        }
    }

    fun heal(amount: Number) = add(amount)
    fun damage(amount: Number) = add(-amount.toFloat())

    fun combineHealthComponents(other: HealthComponent): HealthComponent {
        return HealthComponent(
            this.getHealth() + other.getHealth(),
            this.getMaxHealth() + other.getMaxHealth()
        )
    }
}