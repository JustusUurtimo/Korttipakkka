package com.sq.thed_ck_licker.ecs.components

/**
 * Used to make more complex activations
 * The functions take the target entity id as a parameter.
 * Then you just make what you want in the function body.
 * And maaaagic
 */
data class EffectComponent(
    val onDeath: (Int) -> Unit = {},
    val onSpawn: (Int) -> Unit = {},
    val onTurnStart: (Int) -> Unit = {},
    val onPlay: (Int, Int) -> Unit = { _, _ -> },
    val onDeactivate: (Int, Int) -> Unit = { _, _ -> },
)

