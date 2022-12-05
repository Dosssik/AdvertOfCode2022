package days

import java.io.File

private val priorityIndexes = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

fun main() {
    val bags = File("src/main/kotlin/days/input/input_day_3.txt").readText().lines()
    task1(bags)
    println()
    task2(bags)
}

private fun task1(bags: List<String>) {
    val sumOfPriorities = bags.sumOf { bag ->
        calculatePriority(bag)
    }
    print(sumOfPriorities)
}


fun calculatePriority(bag: String): Int {
    val half = (bag.lastIndex / 2) + 1
    val firstSection = bag.dropLast(half)
    val lastSection = bag.drop(half)
    val intersection = firstSection.toSet().intersect(lastSection.toSet())
    return priorityIndexes.indexOf(intersection.first())
}

private fun task2(bags: List<String>) {
    val groups = bags.chunked(3)
    val sumOfBadges = groups.sumOf { group ->
        calculateBadgePriorityForGroup(group)
    }
    println("sumOfBadges = $sumOfBadges")
}

fun calculateBadgePriorityForGroup(group: List<String>): Int {
    val intersection = group.reduce { acc, elf ->
        (acc.toSet() intersect elf.toSet()).toString()
    }
        .replace("[", "")
        .replace("]", "")
    return priorityIndexes.indexOf(intersection.first())
}

