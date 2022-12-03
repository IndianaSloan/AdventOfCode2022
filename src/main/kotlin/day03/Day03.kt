package day03

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return input.map { string ->
            val middleIndex = string.length / 2
            string.substring(0, middleIndex) to string.substring(middleIndex, string.length)
        }.mapNotNull { (first, second) -> first.find { c1 -> second.any { c2 -> c1 == c2 } } }
            .sumOf { char -> (if (char.isUpperCase()) char - 'A' + 26 else char - 'a') + 1 }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { rucksacks ->
            val char = rucksacks.first().filter { rucksacks[1].contains(it) }.first { rucksacks[2].contains(it) }
            (if (char.isUpperCase()) char - 'A' + 26 else char - 'a') + 1
        }
    }

    // test input
    val testInput = readInput("day03", isSample = true)
    check(part1(testInput) == 157) { "Part1 Check failed. Result was: ${part1(testInput)}, expected: 157" }

    val input = readInput("day03")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}