package days

import java.io.File
import java.util.Stack

fun main() {
    val input = File("src/main/kotlin/days/input/input_day_5.txt").readText()

    val initialStacksString = input.split("\n\n").first()
    val commandsString = input.split("\n\n").last()

    val commands = mapToCommands(commandsString)

    task1(
        commands = commands,
        stacks = mapToStacks(initialStacksString)
    )
    println()
    task2(
        commands = commands,
        stacks = mapToStacks(initialStacksString)
    )
}

private fun task1(
    commands: List<Command>,
    stacks: List<Stack<Char>>
) {
    commands.forEach { (amount, from, to) ->
        repeat(amount) {
            val crate = stacks[from - 1].pop()
            stacks[to - 1].push(crate)
        }
    }
    print("Result for task1: ")
    stacks.forEach {
        print(it.peek())
    }
}

private fun task2(
    commands: List<Command>,
    stacks: List<Stack<Char>>
) {
    commands.forEach { (amount, from, to) ->
        val carry = mutableListOf<Char>()
        repeat(amount) {
            carry.add(
                stacks[from - 1].pop()
            )
        }
        carry.reversed().forEach {
            stacks[to - 1].push(it)
        }
    }
    print("Result for task2: ")
    stacks.forEach {
        print(it.peek())
    }
}

private fun mapToStacks(input: String): List<Stack<Char>> {
    val lines = input.lines()
    val stringStacks = lines.dropLast(1)
    val numbers = lines.last()
    val numbersWithoutWhitespace = numbers.filter { it.isWhitespace().not() }

    val realStacks = mutableListOf<Stack<Char>>().apply {
        repeat(numbersWithoutWhitespace.length) {
            add(Stack())
        }
    }

    numbers.forEachIndexed { index, char ->
        if (char.isWhitespace().not()) {
            stringStacks.reversed().forEach { stackLine ->
                val relevantChar = stackLine.getOrNull(index) ?: ' '
                if (relevantChar.isWhitespace().not()) {
                    realStacks[numbersWithoutWhitespace.indexOf(char)].push(relevantChar)
                }
            }
        }
    }
    return realStacks
}

//VJSFHWGFT
private fun mapToCommands(input: String): List<Command> {
    val sanitized = input
        .replace("move ", "")
        .replace(" from ", ",")
        .replace(" to ", ",")
    return sanitized.lines().map { line ->
        val (amount, from, to) = line.split(",")
        val commandsPack = mutableListOf<Command>()
        commandsPack.add(
            Command(
                amount = amount.toInt(),
                from = from.toInt(),
                to = to.toInt()
            )
        )
        commandsPack
    }.flatten()
}

data class Command(
    val amount: Int,
    val from: Int,
    val to: Int
)