package com.sq.thed_ck_licker.ecs.components

import com.sq.thed_ck_licker.ecs.managers.EntityId

/**
 * @param history is reference to some entity, hopefully the same in past but no guarantees
 */
data class HistoryComponent(val history: EntityId): Component
