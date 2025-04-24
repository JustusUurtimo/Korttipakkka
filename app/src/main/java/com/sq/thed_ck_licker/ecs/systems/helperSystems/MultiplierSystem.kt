package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityId
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.difference
import com.sq.thed_ck_licker.ecs.get
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.isSubclassOf



fun multiplyEntityValues2(oldEntityId: EntityId, targetEntityId: EntityId) {
    if (oldEntityId == targetEntityId) return

    val difference = targetEntityId difference oldEntityId
    println("Difference: $difference")
    val diffComponents = ComponentManager.componentManager.getAllComponentsOfEntity(difference)
    println("Diff Components: $diffComponents")

    val multiplier = (targetEntityId get MultiplierComponent::class).multiplier - 1

    for (component in diffComponents) {
        val declaredMembers = component::class.declaredMembers
        for (member in declaredMembers) {
            if (member !is KProperty<*>) continue
            val propertyType = member.returnType.classifier as KClass<*>

            if (!propertyType.isSubclassOf(Number::class)) continue
            val value = member.call(component) as Number
            println("Value: $value")
            val newValue = value.toFloat() * multiplier
            println("New Value: $newValue")


        }
    }
}


fun multiplyEntityValues(oldEntityId: EntityId, targetEntityId: EntityId) {
    if (oldEntityId == targetEntityId) return

    val difference = targetEntityId difference oldEntityId
    println("Difference: $difference")
    val diffComponents = ComponentManager.componentManager.getAllComponentsOfEntity(difference)
    println("Diff Components: $diffComponents")

    val multiplier = (targetEntityId get MultiplierComponent::class).multiplier - 1

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
                println("Current Value: $currentValue (${currentValue::class.simpleName})")

                // Calculate new value
                val newValue = when (currentValue) {
                    is Int -> currentValue * multiplier.toInt()
                    is Float -> currentValue * multiplier.toFloat()
                    is Double -> currentValue * multiplier.toDouble()
                    is Long -> currentValue * multiplier.toLong()
                    is Short -> (currentValue * multiplier.toInt()).toShort()
                    is Byte -> (currentValue * multiplier.toInt()).toByte()
                    else -> currentValue.toFloat() * multiplier.toFloat()
                }

                println("New Value: $newValue")

                // Set the new value (handling different numeric types)
                when (propertyType) {
                    Int::class -> member.setter.call(component, newValue.toInt())
                    Float::class -> member.setter.call(component, newValue.toFloat())
                    Double::class -> member.setter.call(component, newValue.toDouble())
                    Long::class -> member.setter.call(component, newValue.toLong())
                    Short::class -> member.setter.call(component, newValue.toShort())
                    Byte::class -> member.setter.call(component, newValue.toByte())
                    else -> member.setter.call(component, newValue.toFloat())
                }
            } catch (e: Exception) {
                println("Failed to modify ${member.name}: ${e.message}")
            }
        }
    }
}