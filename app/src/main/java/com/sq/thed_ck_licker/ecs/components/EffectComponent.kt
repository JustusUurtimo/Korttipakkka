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
) {
    fun combineEffectComponents(other: EffectComponent): EffectComponent {
        val onPlay: (Int, Int) -> Unit = { targetId, latestCard ->
            this.onPlay.invoke(targetId, latestCard)
            other.onPlay.invoke(targetId, latestCard)
        }
        val onDeath: (Int) -> Unit = {
            this.onDeath.invoke(it)
            other.onDeath.invoke(it)
        }
        val onSpawn: (Int) -> Unit = {
            this.onSpawn.invoke(it)
            other.onSpawn.invoke(it)
        }
        val onTurnStart: (Int) -> Unit = {
            this.onTurnStart.invoke(it)
            other.onTurnStart.invoke(it)
        }
        val onDeactivate: (Int, Int) -> Unit = { targetId, latestCard ->
            this.onDeactivate.invoke(targetId, latestCard)
            other.onDeactivate.invoke(targetId, latestCard)
        }
        return EffectComponent(
            onPlay = onPlay,
            onDeath = onDeath,
            onSpawn = onSpawn,
            onTurnStart = onTurnStart,
            onDeactivate = onDeactivate
        )
    }
}

