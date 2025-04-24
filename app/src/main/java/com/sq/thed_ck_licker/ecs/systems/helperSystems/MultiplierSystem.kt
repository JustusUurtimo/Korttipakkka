package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityId
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.difference
import com.sq.thed_ck_licker.ecs.get
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.isSubclassOf

fun multiplyEntityValues(oldEntityId: EntityId, targetEntityId: EntityId) {
    if (oldEntityId == targetEntityId) return

    val difference = targetEntityId difference oldEntityId
    println("Difference: $difference")
    val diffComponents = ComponentManager.componentManager.getAllComponentsOfEntity(difference)
    println("Diff Components: $diffComponents")

    val multiplier = (targetEntityId get MultiplierComponent::class).multiplier - 1

    for (component in diffComponents) {
//        val declaredThings = component::class.declaredMembers
//        println("Declared Things: $declaredThings")
//        for (declaredThing in declaredThings) {
//            println("Declared Thing: $declaredThing")
//            println("Declared Thing Name: ${declaredThing.name}")
//            println("Declared Thing Type: ${declaredThing.returnType}")
//            println("Declared Thing Type Classifier: ${declaredThing.returnType.classifier}")
//            println("Declared Thing Type Classifier Number: ${declaredThing.returnType.classifier == Number::class}")
////            println("Declared Thing Type Classifier String: ${declaredThing.returnType.classifier == String::class}")
//            if (declaredThing.returnType.classifier == Int::class) {
//            println("declared thing value: ${declaredThing.call(component)}")
//
//            }
//        }


        val declaredMembers = component::class.declaredMembers
        for (member in declaredMembers) {
            if (member !is KProperty<*>) continue
            val propertyType = member.returnType.classifier as KClass<*>

            if (!propertyType.isSubclassOf(Number::class)) continue
            val value = member.call(component) as Number
            println("Value: $value")

        }
    }
}