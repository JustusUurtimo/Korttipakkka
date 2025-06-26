package com.sq.thed_ck_licker.ecs.components

import com.sq.thed_ck_licker.ecs.managers.EntityId

data class OwnerComponent(val ownerId: EntityId) : Component
