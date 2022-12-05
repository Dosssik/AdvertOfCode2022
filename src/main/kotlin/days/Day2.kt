import java.io.File

fun main() {
    val data = File("src/main/kotlin/days/input/input_day_2.txt").readText()
    val totalScore = data.lines().sumOf { round ->
        calculateScore(round.first(), round.last())
    }
    print(totalScore)
}

fun calculateScore(opponentShape: Char, myShape: Char): Int {
    val scoreForShape = when (myShape) {
        'X' -> 1
        'Y' -> 2
        'Z' -> 3
        else -> throw IllegalArgumentException()
    }
    val scoreForResult = when {
        isDraw(opponentShape = opponentShape, myShape = myShape) -> 3
        isVictory(opponentShape = opponentShape, myShape = myShape) -> 6
        else -> 0 // Loser!
    }
    return scoreForShape + scoreForResult
}

fun isDraw(opponentShape: Char, myShape: Char): Boolean {
    return (opponentShape == 'A' && myShape == 'X') ||
            (opponentShape == 'B' && myShape == 'Y') ||
            (opponentShape == 'C' && myShape == 'Z')
}

fun isVictory(opponentShape: Char, myShape: Char): Boolean {
    return (opponentShape == 'A' && myShape == 'Y') ||
            (opponentShape == 'B' && myShape == 'Z') ||
            (opponentShape == 'C' && myShape == 'X')
}