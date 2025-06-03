package com.sq.thed_ck_licker.ecs.managers

import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.systems.helperSystems.onDeathSystem

object TickingMethodsManager {


        fun healthTicker(amountOfDamage:Float = 1f): (Int) -> Unit {
        val theAction = { target: Int ->
            val targetHealth = target get HealthComponent::class
            println("Ticking")
            println("Health is ${targetHealth.getHealth()}")
            println("Target is $target")
            targetHealth.damage(amountOfDamage)
            if (targetHealth.getHealth() <= 0) {
                onDeathSystem()
            }
        }
        return theAction
    }
//    val healthTicker = { target: Int ->
//        val targetHealth = target get HealthComponent::class
//        targetHealth.damage(1f)
//        if (targetHealth.getHealth() <= 0) {
//            onDeathSystem()
//        }
//    }
}