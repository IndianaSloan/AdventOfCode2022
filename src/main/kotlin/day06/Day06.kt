package day06

import readInput

fun main() {

    fun part1(input: List<String>): Int = input.first().decrypt(4)
    fun part2(input: List<String>): Int = input.first().decrypt(14)

    val input = readInput("day06")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

fun String.decrypt(chunkSize: Int, step: Int = 1): Int =
    windowed(chunkSize, step).indexOfFirst { it.toSet().size == chunkSize } + chunkSize
