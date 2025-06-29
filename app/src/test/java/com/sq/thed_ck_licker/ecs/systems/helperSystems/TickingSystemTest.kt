package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.misc.TickComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.systems.helperSystems.TickingSystem
import com.sq.thed_ck_licker.helpers.DescribedEffect
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates

class TickingSystemTest {

    var system: TickingSystem =TickingSystem


    @Test
    fun `Tick once`() {
        val ent = EntityManager.createNewEntity()
        var counter = 0
        val tickAction: (Int) -> Unit = { targetId: Int ->
            counter++
        }
        val describedEffect = DescribedEffect(tickAction) { "Increase counter by 1 foreach tick" }
        ent add TickComponent(tickAction = describedEffect)



        system.tick()
        assert(counter == 1) { "Counter should be 1, but was $counter" }
    }

    @Test
    fun `Tick 10 times at once`() {
        val ent = EntityManager.createNewEntity()
        var counter = 0
        val tickAction: (Int) -> Unit = { targetId: Int ->
            counter++
        }
        val describedEffect = DescribedEffect(tickAction) { "Increase counter by 1 foreach tick" }
        ent add TickComponent(tickAction = describedEffect)

        system.tick(1000)

        assert(counter == 10) { "Counter should be 10, but was $counter" }
    }
}
