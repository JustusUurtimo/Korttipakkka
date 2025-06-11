package com.sq.thed_ck_licker.helpers

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
    val describe: () -> String
){
    companion object{
        val EMPTY = DescribedEffect({},{""})
    }
}


fun DescribedEffect.combine(other: DescribedEffect): DescribedEffect {
    val eka = this == DescribedEffect.EMPTY
    val toka  = other == DescribedEffect.EMPTY

    if (eka && toka) return DescribedEffect.EMPTY

    return DescribedEffect(
        action = { id -> this.action(id); other.action(id) },
        describe = {
            listOf(this.describe(), other.describe())
                .filter { it.isNotBlank() }
                .joinToString(". ")
        }
    )
}
