package com.sq.thed_ck_licker.ecs.components.misc

import org.junit.jupiter.api.Test

class HealthComponentTest {

    @Test
    fun `Add two health components together`() { //Ed...ward...Eed...ward....
        val hp = HealthComponent(100f)
        val hpComp2 = HealthComponent(200f)
        val combinedComponent = hp.combineHealthComponents(hpComp2)
        assert(combinedComponent.getHealth() == 300f) { "Health should be 300 but was ${combinedComponent.getHealth()}" }
        assert(combinedComponent.getMaxHealth() == 300f) { "Max health should be 300 but was ${combinedComponent.getMaxHealth()}" }
    }

}