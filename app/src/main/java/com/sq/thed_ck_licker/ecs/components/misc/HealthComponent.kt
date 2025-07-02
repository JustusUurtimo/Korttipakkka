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
    constructor(health: Float = 100f, maxHealth: Float = 100f) : this(
        mutableFloatStateOf(health),
        mutableFloatStateOf(maxHealth)
    )

    /**
     * This can be used to construct Health component with health same as max health
     */
    constructor(maxHealth: Float = 100f) : this(
        mutableFloatStateOf(maxHealth),
        mutableFloatStateOf(maxHealth)
    )

    fun setHealth(health: Float) {
        this.health.floatValue = health
    }

    fun getHealth(): Float {
        return this.health.floatValue
    }

    fun setMaxHealth(health: Float): Float {
        this.maxHealth.floatValue = health
        return this.maxHealth.floatValue
    }
    fun getMaxHealth(): Float {
        return this.maxHealth.floatValue
    }

    fun increaseMaxHealth(amount: Float): Float {
        this.maxHealth.floatValue += amount.toFloat()
        return this.maxHealth.floatValue
    }

    private var lastTime = -1L

    /**
     * @return amount of health gained
     */
    fun add(amount: Float): Float {
        var logging = "Hp: ${this.health}/${this.maxHealth}"
        var healthGained = amount
        if ((this.health.floatValue + amount) > this.maxHealth.floatValue) {
            healthGained = this.maxHealth.floatValue - this.health.floatValue
            this.health.floatValue = this.maxHealth.floatValue
        } else {
            this.health.floatValue += amount
        }
        logging += "and the end result is Hp: ${this.health}/${this.maxHealth}"

        val now = System.currentTimeMillis()
        if (lastTime + 5000 < now) {
            lastTime = now
            Log.i("HealthComponent", logging)
        }
        return healthGained
    }

    fun heal(amount: Float) = add(amount)
    fun damage(amount: Float) = add(-amount)
    fun kill() = setHealth(0f)

    fun combineHealthComponents(other: HealthComponent): HealthComponent {
        return HealthComponent(
            this.getHealth() + other.getHealth(),
            this.getMaxHealth() + other.getMaxHealth()
        )
    }
}