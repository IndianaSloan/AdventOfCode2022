package day04

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return input.mapNotNull { string ->
            val (first, second) = string.split(",").map { elfRange ->
                val intList = elfRange.split("-").map { it.toInt() }
                IntRange(intList.first(), intList.last()).toSet()
            }
            if (first.isEmpty() && second.isEmpty()) return@mapNotNull null
            if (first.containsAll(second) || second.containsAll(first)) 1 else null
        }.count()
    }

    fun part2(input: List<String>): Int {
        return input.mapNotNull { string ->
            val (first, second) = string.split(",").map { elfRange ->
                val intList = elfRange.split("-").map { it.toInt() }
                IntRange(intList.first(), intList.last()).toSet()
            }
            if (first.isEmpty() && second.isEmpty()) return@mapNotNull null
            if (first.any { second.contains(it) }|| second.any { first.contains(it) }) 1 else null
        }.count()
    }

    // test input
    val testInput = readInput("day04", isSample = true)
    check(part1(testInput) == 2) { "Part1 Check failed. Result was: ${part1(testInput)}, expected: 2" }

    val input = readInput("day04")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}