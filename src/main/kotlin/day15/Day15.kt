package day15

import allInts
import readInput
import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    fun manhattanDistance(other: Point) = abs(x - other.x) + abs(y - other.y)
}

fun main() {

    fun getSensorPairs(input: List<String>): List<Pair<Point, Point>> {
        return input.map(String::allInts).map { (i1, i2, i3, i4) -> Point(i1, i2) to Point(i3, i4) }
    }

    fun part1(input: List<String>, targetY: Int): Int {
        val pairs = getSensorPairs(input)

        val signalPoints = mutableSetOf<Int>()

        pairs.forEach { (sensor, beacon) ->
            val manhattan = sensor.manhattanDistance(beacon)

            val distanceToTarget = abs(sensor.y - targetY)
            val signalWidth = manhattan - distanceToTarget

            (sensor.x - signalWidth..sensor.x + signalWidth).forEach { x ->
                val point = Point(x, targetY)
                if (point != beacon) {
                    signalPoints.add(x)
                }
            }
        }
        return signalPoints.count()
    }

    fun part2(input: List<String>): Long {
        val maxSize = 4_000_000
        val pairs = getSensorPairs(input)

        val lineSignalRanges = Array<MutableList<IntRange>>(maxSize + 1) { mutableListOf() }

        pairs.forEach { (sensor, beacon) ->
            for (y in 0..maxSize) {
                val manhattan = sensor.manhattanDistance(beacon)
                val distanceToY = abs(sensor.y - y)
                val signalWidth = manhattan - distanceToY
                if (signalWidth > 0) {
                    lineSignalRanges[y].add(sensor.x - signalWidth..sensor.x + signalWidth)
                }
            }
        }

        lineSignalRanges.forEachIndexed { index, ranges ->
            val sortedRanges = ranges.sortedBy { it.first }
            var largest = sortedRanges.first().last
            sortedRanges.drop(1).forEach {
                if (it.first > largest) {
                    return (it.first - 1).toLong() * maxSize.toLong() + index.toLong()
                }
                if (it.last > largest) {
                    largest = it.last
                }
            }
        }
        return 0L
    }

    val testInput = readInput("day15", isSample = true)
    val testResult = part1(testInput, 10)
    val testResult2 = part2(testInput)
    check(testResult == 26) { "Part1 Check failed. Result was: $testResult, expected: 26" }
    check(testResult2 == 56000011L) { "Part1 Check failed. Result was: $testResult2, expected: 56000011" }

    val input = readInput("day15")
    println("Part1: ${part1(input, 2_000_000)}")
    println("Part2: ${part2(input)}")
}
