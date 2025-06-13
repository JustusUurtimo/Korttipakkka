package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.managers.EntityId

/**
 * @param action is same as our old effect (Int) -> Unit thing, so you can use it as you want
 *
 * @param describe is string from same idea, as we need dynamic string to description for the effects
 *  So instead of just "moi" you wrap it in {"moi"} and invoke it when needed.
 *  This makes the strings dynamic and similar to lazy init.
 *
 */
data class DescribedEffect(
    val action: (Int) -> Unit,
    val describe: (Int) -> String
){
    companion object{
        val EMPTY = DescribedEffect({},{""})
    }

    /**
     * This is fun but might be bit naughty...
     * This allow syntax like onPlay(10)
     * And it will trigger the action and return the description
     */
    operator fun invoke(targetId: EntityId): String {
        action(targetId)
        return describe(targetId)
    }
}


fun DescribedEffect.combine(other: DescribedEffect): DescribedEffect {
    val isThisEmpty = this == DescribedEffect.EMPTY
    val isOtherEmpty  = other == DescribedEffect.EMPTY

    if (isThisEmpty && isOtherEmpty) return DescribedEffect.EMPTY

    if (isThisEmpty) return other
    if (isOtherEmpty) return this

    return DescribedEffect(
        action = { id -> this.action(id); other.action(id) },
        describe = {id ->
            "${this.describe(id)}\n${other.describe(id)}"
        }
    )
}
