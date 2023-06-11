package edu.ucu.statisticbuilder.application.entitiesrankprocessing

import java.util.*

class TopTenEntities : Iterable<EntityCount> {
    private val current: MutableMap<String, EntityCount> = HashMap<String, EntityCount>()
    private val top: TreeSet<EntityCount> = TreeSet<EntityCount>(
        compareBy<EntityCount> { it.count }.reversed().thenBy { it.entity })

    override fun toString(): String {
        return current.toString()
    }

    fun add(entity: EntityCount) {
        if (current.containsKey(entity.entity)) {
            top.remove(current.remove(entity.entity))
        }
        top.add(entity)
        current[entity.entity] = entity
        if (top.size > 10) {
            val last = top.last()
            current.remove(last.entity)
            top.remove(last)
        }
    }

    fun remove(value: EntityCount) {
        top.remove(value)
        current.remove(value.entity)
    }

    override fun iterator(): Iterator<EntityCount> {
        return top.iterator()
    }
}