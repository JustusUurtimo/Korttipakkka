package com.sq.thed_ck_licker.ecs.components

// TODO this needs to be mutableStateListOf but i cant get the type to work
// mutableStateListOf is part of Compose's runtime and requires the Compose context
// this way we can keep our components free from compose dependencies and make them usable in non-UI logic
data class DrawDeckComponent(val cardIds: List<Int> = listOf(0))

data class DiscardDeckComponent(val cards: List<Int> = listOf(0))


