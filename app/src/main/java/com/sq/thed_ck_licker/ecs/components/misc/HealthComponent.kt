package com.sq.thed_ck_licker.ecs.components.misc

import android.util.Log
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents

data class HealthComponent(
    private var health: MutableFloatState,
    private val maxHealth: MutableFloatState
) {
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

    fun getMaxHealth(): Float {
        return this.maxHealth.floatValue
    }

    fun increaseMaxHealth(amount: Float) {
        this.maxHealth.floatValue += amount
    }
    fun add(amount: Float) {
        var logging = "This is going to be modified $this"
        if ((this.health.floatValue + amount) > this.maxHealth.floatValue) {
            this.health.floatValue = this.maxHealth.floatValue
        } else {
            this.health.floatValue += amount
        }
        logging += "and the end result is $this"
        Log.i("HealthComponent", logging)
    }

    fun heal(amount: Float) = add(amount)
    fun damage(amount: Float) = add(-amount)

    fun combineHealthComponents(other: HealthComponent): HealthComponent {
        return HealthComponent(
            this.getHealth() + other.getHealth(),
            this.getMaxHealth() + other.getMaxHealth()
        )
    }
}