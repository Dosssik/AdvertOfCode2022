package day2

import java.io.File

fun main() {
    val data = File("src/main/kotlin/day2/input/input_day_2.txt").readText()
    val totalScore = data.lines().sumOf { round ->
        calculateScore(round.first(), round.last())
    }
    print(totalScore)
}

fun calculateScore(opponentShape: Char, gameResult: Char): Int {
    val scoreForResult = when (gameResult) {
        'X' -> 0
        'Y' -> 3
        'Z' -> 6
        else -> throw IllegalArgumentException()
    }
    val scoreForMyShape = calculateScoreForMyShape(
        opponentShape = opponentShape, gameResult = gameResult
    )
    return scoreForMyShape + scoreForResult
}

fun calculateScoreForMyShape(opponentShape: Char, gameResult: Char): Int {
    val myShape = when (gameResult) {
        'X' -> loseCombinations[opponentShape]
        'Y' -> drawCombinations[opponentShape]
        'Z' -> winCombinations[opponentShape]
        else -> throw IllegalArgumentException()
    }
    return when (myShape) {
        'X' -> 1
        'Y' -> 2
        'Z' -> 3
        else -> throw IllegalArgumentException()
    }
}


val winCombinations = mapOf(
    'A' to 'Y',
    'B' to 'Z',
    'C' to 'X'
)

val loseCombinations = mapOf(
    'A' to 'Z',
    'B' to 'X',
    'C' to 'Y'
)

val drawCombinations = mapOf(
    'A' to 'X',
    'B' to 'Y',
    'C' to 'Z'
)
