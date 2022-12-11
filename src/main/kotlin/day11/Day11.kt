package day11

import readInput

fun main() {

    fun parseMonkeys(input: List<String>, relief: Int): List<Monkey> {
        return input.windowed(6, 7).map { monkeyInput ->
            val items = monkeyInput[1].trim()
                .replace("Starting items: ", "")
                .split(", ")
                .map(String::toLong)
                .toMutableList()

            val operationProps = monkeyInput[2].trim()
                .replace("Operation: new = old ", "")
                .split(" ")

            val operation = operationProps.first().first()
            val testValue = monkeyInput[3].trim().replace("Test: divisible by ", "").toLong()
            val testPassIndex = monkeyInput[4].trim().takeLast(1).toInt()
            val testFailIndex = monkeyInput[5].trim().takeLast(1).toInt()
            Monkey(
                items = items,
                performOperation = { item ->
                    when (val opValue = operationProps.last()) {
                        "old" -> {
                            if (operation == '*') item * item
                            else item + item
                        }
                        else -> {
                            if (operation == '*') item * opValue.toLong()
                            else item + opValue.toLong()
                        }
                    } / relief.toLong()
                },
                performTest = { item -> if (item % testValue == 0L) testPassIndex else testFailIndex },
            )
        }
    }

    fun List<String>.simulate(relief: Int, rounds: Int): Long {
        val monkeyList = parseMonkeys(this, relief)
        repeat(rounds) {
            monkeyList.forEach { monkey ->
                monkey.items.forEach { item ->
                    val newValue = monkey.performOperation(item)
                    val throwIndex = monkey.performTest(newValue)
                    monkeyList[throwIndex].items.add(newValue)
                    monkey.inspectionCount++
                }
                monkey.items.clear()
            }
        }
        return monkeyList.map { it.inspectionCount.toLong() }
            .sortedDescending()
            .take(2)
            .reduce { i, i2 -> i * i2}
    }

    fun part1(input: List<String>): Long {
        return input.simulate(3, 20)
    }

    fun part2(input: List<String>): Long {
        return input.simulate(1, 10000)
    }

    val testInput = readInput("day11", isSample = true)
    val testResult = part1(testInput)
    check(testResult == 10605L) { "Part1 Check failed. Result was: $testResult, expected: 10605" }

    val input = readInput("day11")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

private data class Monkey(
    val items: MutableList<Long> = mutableListOf(),
    val performOperation: (item: Long) -> Long,
    val performTest: (item: Long) -> Int,
    var inspectionCount: Int = 0,
)
