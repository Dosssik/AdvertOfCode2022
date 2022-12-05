package days

import java.io.File


fun main() {
    val elfPairs = File("src/main/kotlin/days/input/input_day_4.txt").readText().lines().map(::toElfPair)
    task1(elfPairs)
    println()
    task2(elfPairs)
}

private fun task1(elfPairs: List<ElfPair>) {
    val fullyOverlapped = elfPairs.filter(::hasFullOverlap)
    println(fullyOverlapped.size)
}

private fun task2(elfPairs: List<ElfPair>) {
    val fullyOverlapped = elfPairs.filter(::hasPartialOverlap)
    println(fullyOverlapped.size)
}

private fun hasFullOverlap(elfPair: ElfPair): Boolean {
    return with(elfPair) {
        (firstLowerBoundary <= secondLowerBoundary && firstHigherBoundary >= secondHigherBoundary)
                || (firstLowerBoundary >= secondLowerBoundary && firstHigherBoundary <= secondHigherBoundary)
    }
}

private fun hasPartialOverlap(elfPair: ElfPair): Boolean {

    val myApply = elfPair.apply {

    }

    val result = with(elfPair) {
        (firstLowerBoundary..firstHigherBoundary).contains(secondLowerBoundary)
                || (firstLowerBoundary..firstHigherBoundary).contains(secondHigherBoundary)
                || (secondLowerBoundary..secondHigherBoundary).contains(firstLowerBoundary)
                || (secondLowerBoundary..secondHigherBoundary).contains(firstHigherBoundary)
    }
    return result
}

private fun toElfPair(stringPair: String): ElfPair {
    return stringPair.split(",").let { elves ->
        val firstElf = elves.first().split("-")
        val secondElf = elves.last().split("-")
        ElfPair(
            firstLowerBoundary = firstElf.first().toInt(),
            firstHigherBoundary = firstElf.last().toInt(),
            secondLowerBoundary = secondElf.first().toInt(),
            secondHigherBoundary = secondElf.last().toInt(),
        )
    }
}

private data class ElfPair(
    val firstLowerBoundary: Int,
    val firstHigherBoundary: Int,
    val secondLowerBoundary: Int,
    val secondHigherBoundary: Int,
)