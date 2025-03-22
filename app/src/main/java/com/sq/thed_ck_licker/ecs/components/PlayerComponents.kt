package com.sq.thed_ck_licker.ecs.components

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf

// TODO this needs to be mutableStateListOf but i cant get the type to work
data class DrawDeckComponent(val cards: MutableList<Int> = mutableListOf())

data class DiscardDeckComponent(val cards: MutableList<Int>)


