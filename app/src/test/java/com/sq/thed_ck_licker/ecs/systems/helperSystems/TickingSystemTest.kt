package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.misc.TickComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates

class TickingSystemTest {

    var system: TickingSystem by Delegates.notNull()



    @BeforeEach
    fun setUp() {
        system = TickingSystem()
    }

    @Test
    fun `Tick once`() {
        val ent = EntityManager.createNewEntity()
        var counter = 0
        ent add TickComponent(tickAction = {
            counter++
        })

        system.tick()
        assert(counter == 1) { "Counter should be 1, but was $counter" }
    }

    @Test
    fun `Tick 10 times at once`() {
        val ent = EntityManager.createNewEntity()
        var counter = 0
        ent add TickComponent(tickAction = {
            counter++
        })

        system.tick(1000)

        assert(counter == 10) { "Counter should be 10, but was $counter" }
    }
}
