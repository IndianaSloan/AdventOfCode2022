package day06

import readInput

fun main() {

    fun part1(input: List<String>): Int = input.first().decrypt(4)
    fun part2(input: List<String>): Int = input.first().decrypt(14)

    val testInput = readInput("day06", isSample = true)
    check(part1(testInput) == 7) { "Part1 Check failed. Result was: ${part1(testInput)}, expected: 7" }

    val input = readInput("day06")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

private fun String.decrypt(chunkSize: Int, step: Int = 1): Int =
    windowed(chunkSize, step).indexOfFirst { it.toSet().size == chunkSize } + chunkSize
