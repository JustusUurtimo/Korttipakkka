package com.sq.thed_ck_licker.ecs.components

import com.sq.thed_ck_licker.helpers.MyRandom.random

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
    val onPlay: (Int) -> Unit = { _ -> },
    val onDeactivate: (Int) -> Unit = { _ -> },
) {
    fun combineEffectComponents(other: EffectComponent): EffectComponent {
        val onPlay: (Int) -> Unit = { targetId ->
            this.onPlay.invoke(targetId)
            other.onPlay.invoke(targetId)
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
        val onDeactivate: (Int) -> Unit = { targetId ->
            this.onDeactivate.invoke(targetId)
            other.onDeactivate.invoke(targetId)
        }
        return EffectComponent(
            onPlay = onPlay,
            onDeath = onDeath,
            onSpawn = onSpawn,
            onTurnStart = onTurnStart,
            onDeactivate = onDeactivate
        )
    }

    fun shuffleToNew(): EffectComponent {
        val effectHandlers = mutableListOf<(Int) -> Unit>()
        effectHandlers.add(onDeath)
        effectHandlers.add(onSpawn)
        effectHandlers.add(onTurnStart)
        effectHandlers.add(onPlay)
        effectHandlers.add(onDeactivate)

        return EffectComponent(
            onDeath = effectHandlers.removeAt(random.nextInt(effectHandlers.size)),
            onSpawn = effectHandlers.removeAt(random.nextInt(effectHandlers.size)),
            onTurnStart = effectHandlers.removeAt(random.nextInt(effectHandlers.size)),
            onPlay = effectHandlers.removeAt(random.nextInt(effectHandlers.size)),
            onDeactivate = effectHandlers.removeAt(random.nextInt(effectHandlers.size))
        )
    }
}

