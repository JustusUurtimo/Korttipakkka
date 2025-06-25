package com.sq.thed_ck_licker.ecs.components.effectthing

import com.sq.thed_ck_licker.ecs.components.Component

data class TriggeredEffectsComponent(
    val effectsByTrigger: MutableMap<Trigger, MutableList<Effect>> = mutableMapOf()
) : Component