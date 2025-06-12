package com.sq.thed_ck_licker.ecs.components

import com.sq.thed_ck_licker.ecs.managers.EntityId

/**
 * @param target is reference to some entity that this entity wants to target.
 *  Use cases are such places that not naturally know what to target.
 */
data class TargetComponent(val target: EntityId)
