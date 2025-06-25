package com.sq.thed_ck_licker.ecs.components.effectthing

data class TriggerAndEffectsComponent(
    val trigger: Trigger,
    val effects: List<Effect>
)