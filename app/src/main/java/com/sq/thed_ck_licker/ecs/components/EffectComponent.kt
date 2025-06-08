package com.sq.thed_ck_licker.ecs.components

import com.sq.thed_ck_licker.helpers.DescribedEffect
import com.sq.thed_ck_licker.helpers.MyRandom.random
import com.sq.thed_ck_licker.helpers.combine

/**
 * Used to make more complex activations
 * The functions take the target entity id as a parameter.
 * Then you just make what you want in the function body.
 * And maaaagic
 */
data class EffectComponent(
    val onDeath: DescribedEffect = DescribedEffect({}, { "" }),
    val onSpawn: DescribedEffect = DescribedEffect({}, { "" }),
    val onTurnStart: DescribedEffect = DescribedEffect({}, { "" }),
    val onPlay: DescribedEffect = DescribedEffect({}, { "" }),
    val onDeactivate: DescribedEffect = DescribedEffect({}, { "" }),
) {
    fun combineEffectComponents(other: EffectComponent): EffectComponent {
        return EffectComponent(
            onPlay = this.onPlay.combine(other.onPlay),
            onDeath = this.onDeath.combine(other.onDeath),
            onSpawn = this.onSpawn.combine(other.onSpawn),
            onTurnStart = this.onTurnStart.combine(other.onTurnStart),
            onDeactivate = this.onDeactivate.combine(other.onDeactivate)
        )
    }

    fun shuffleToNew(): EffectComponent {
        val effectHandlers = mutableListOf<DescribedEffect>()
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


    fun describeTriggers(): List<String> {
        val lines = mutableListOf<String>()

        fun handle(label: String, effect: DescribedEffect?) {
            val text = effect?.describe()?.trim().orEmpty()
            if (text.isNotBlank()) {
                lines.add("$label: $text")
            }
        }

        handle("On Death", onDeath)
        handle("On Spawn", onSpawn)
        handle("On Turn Start", onTurnStart)
        handle("On Play", onPlay)
        handle("On Deactivate", onDeactivate)

        return lines
    }

    override fun toString(): String {
        return this.describeTriggers().joinToString("\n")
    }
}

