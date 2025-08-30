package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.OnTick
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeSelfDamage
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.TickComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TickingSystemTest {

    var system: TickingSystem =TickingSystem


    @Test
    fun `Tick once`() {
        val ent = EntityManager.createNewEntity()
        val healthComponent = HealthComponent(10f)
        ent add healthComponent
        ent add OwnerComponent(ent)
        ent add TickComponent(tickThreshold = 100)
        ent add TriggeredEffectsComponent(OnTick, TakeSelfDamage(1f))
        system.tick(realTimePlayerDamageEnabled = true)
        assertEquals(9f, healthComponent.getHealth())
    }

    @Test
    fun `Tick 10 times at once`() {
        val ent = EntityManager.createNewEntity()
        val healthComponent = HealthComponent(11f)
        ent add healthComponent
        ent add OwnerComponent(ent)
        ent add TickComponent(tickThreshold = 100)
        ent add TriggeredEffectsComponent(OnTick, TakeSelfDamage(1f))
        system.tick(1000, true)
        assertEquals(1f, healthComponent.getHealth())
    }
}
