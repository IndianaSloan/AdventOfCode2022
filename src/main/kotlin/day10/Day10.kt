package day10

import readInput
import java.lang.StringBuilder

fun main() {

    fun part1(input: List<String>): Long {
        var operationCount = 0
        var signalStrength = 1L
        val amplifiedStrengths = mutableListOf<Long>()

        input.forEach {
            val operation = it.split(" ")

            if (operation.first() == "noop") {
                operationCount += 1
                if (operationCount in listOf(20, 60, 100, 140, 180, 220, 260, 300)) {
                    amplifiedStrengths.add(signalStrength * operationCount)
                }
            } else if (operation.first() == "addx") {
                repeat(2) { index ->
                    operationCount += 1
                    if (operationCount in listOf(20, 60, 100, 140, 180, 220, 260, 300)) {
                        amplifiedStrengths.add(signalStrength * operationCount)
                    }
                    if (index == 1) {
                        signalStrength += operation.last().toInt()
                    }
                }
            }
        }
        return amplifiedStrengths.sum()
    }

    fun part2(input: List<String>): String {
        var spritePosition = 1
        var operationCount = 1
        val builder = StringBuilder()
        builder.appendLine()
        fun checkLineBreak() {
            if (operationCount % 40 == 0) {
                builder.appendLine()
                operationCount = 1
            } else {
                operationCount++
            }
        }
        input.forEach {
            val inst = it.split(" ")
            builder.append(getPixel(operationCount, spritePosition))
            checkLineBreak()
            if (inst.first() == "addx") {
                builder.append(getPixel(operationCount, spritePosition))
                spritePosition += inst.last().toInt()
                checkLineBreak()
            }
        }
        return builder.toString()
    }

    val testInput = readInput("day10", isSample = true)
    val testResult = part1(testInput)
    check(testResult == 13140L) { "Part1 Check failed. Result was: $testResult, expected: 13140" }

    val input = readInput("day10")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

fun getPixel(opCount: Int, spritePos: Int): Char =
    if (opCount - 1 in listOf(spritePos - 1, spritePos, spritePos + 1)) '#' else '.'