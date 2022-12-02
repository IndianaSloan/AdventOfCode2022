package day01

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val elves = mapElves(input)
        return elves.maxBy { it.calCount }.calCount
    }

    fun part2(input: List<String>): Int {
        val elves = mapElves(input)
        return elves.sortedByDescending { it.calCount }.take(3).sumOf { it.calCount }
    }

    // test input
    val testInput = readInput("day01", isSample = true)
    check(part1(testInput) == 24000) { "Part1 Check failed. Result was: ${part1(testInput)}, expected: 24000" }
    check(part2(testInput) == 45000) { "Part1 Check failed. Result was: ${part1(testInput)}, expected: 45000" }

    val input = readInput("day01")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

data class Elf(val calCount: Int)

private fun mapElves(input: List<String>): List<Elf> {
    val elves = mutableListOf<Elf>()
    var currentCount = 0
    input.forEachIndexed { index, calorie ->
        if (calorie.isEmpty()) {
            elves.add(Elf(currentCount))
            currentCount = 0
        } else {
            currentCount += calorie.toInt()
            if (index == input.lastIndex) {
                elves.add(Elf(currentCount))
                currentCount = 0
            }
        }
    }
    return elves
}