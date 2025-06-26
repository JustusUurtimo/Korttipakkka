package com.sq.thed_ck_licker.helpers

/**
 * @param action is same as our old effect (Int) -> Unit thing, so you can use it as you want
 *
 * @param describe is string from same idea, as we need dynamic string to description for the effects
 *  So instead of just "moi" you wrap it in {"moi"} and invoke it when needed.
 *  This makes the strings dynamic and similar to lazy init.
 *
 */
@Deprecated("As of 0.1.2.146, The TriggerEffect thing is so much better, this will be reworked out of commission")
data class DescribedEffect(
    val action: (Int) -> Unit,
    val describe: (Int) -> String
) {
    companion object {
        val EMPTY = DescribedEffect({}, { "" })
    }


    fun combine(other: DescribedEffect): DescribedEffect {
        val isThisEmpty = this == EMPTY
        val isOtherEmpty = other == EMPTY

        if (isThisEmpty && isOtherEmpty) return EMPTY

        if (isThisEmpty) return other
        if (isOtherEmpty) return this

    return DescribedEffect(
        action = { id -> this.action(id); other.action(id) },
        describe = { id ->
            "${this.describe(id)}\n${other.describe(id)}"
        }
    )
}
}
