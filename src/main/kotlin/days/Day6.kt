package days

import java.io.File

private const val THRESHOLD = 4
//private const val THRESHOLD = 14
private var buffer: String = ""

fun main() {
    var result = -1
    File("src/main/kotlin/days/input/input_day_6.txt")
        .readText()
        .forEachIndexed { index, char ->
            val markerDetected = addToBuffer(char)
            if (markerDetected) {
                result = index + 1
                println("result = $result")
                return
            }
        }

}

private fun addToBuffer(char: Char): Boolean {
    if (buffer.length < THRESHOLD) {
        buffer += char
    } else {
        buffer = buffer.drop(1) + char
    }
    return buffer.length == THRESHOLD && buffer.toSet().size == THRESHOLD
}
