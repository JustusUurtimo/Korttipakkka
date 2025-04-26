package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityId
import com.sq.thed_ck_licker.ecs.add
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.combineHealthComponents
import com.sq.thed_ck_licker.ecs.components.combineScoreComponents
import com.sq.thed_ck_licker.ecs.difference
import com.sq.thed_ck_licker.ecs.get
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.isSubclassOf

fun multiplyEntityValues(oldEntityId: EntityId, targetEntityId: EntityId) {
    if (oldEntityId == targetEntityId) return

    val difference = targetEntityId difference oldEntityId
    val diffComponents = ComponentManager.componentManager.getAllComponentsOfEntity(difference)

    val multiplier = (targetEntityId get MultiplierComponent::class).multiplier - 1


    //Beyond here lays mad max style wasteland with out any sensibilities left
    for (component in diffComponents) {
        val declaredMembers = component::class.declaredMembers
        for (member in declaredMembers) {
            // Skip if not a property or not mutable
            if (member !is KMutableProperty<*>) continue

            // Check if the property type is a subclass of Number
            val propertyType = member.returnType.classifier as? KClass<*>
            if (propertyType?.isSubclassOf(Number::class) != true) continue

            try {
                // Get the current value
                val currentValue = member.getter.call(component) as Number

                val newValue = currentValue.toFloat() * multiplier
                when (propertyType) {
                    Int::class -> member.setter.call(component, newValue.toInt())
                    Float::class -> member.setter.call(component, newValue.toFloat())
                    else -> member.setter.call(component, newValue.toFloat())
                }

            } catch (e: Exception) {
                Log.e("multiplyEntityValues", "Failed to modify ${member.name}: ${e.message}")
            }
        }

        val targetComponent = (targetEntityId get component::class)
        when (component) {
            is HealthComponent -> {
                targetComponent as HealthComponent
                targetEntityId add (component.combineHealthComponents(targetComponent))
            }

            is ScoreComponent -> {
                targetComponent as ScoreComponent
                targetEntityId add (component.combineScoreComponents(targetComponent))
            }
        }
    }
}