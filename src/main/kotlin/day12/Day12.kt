package day12

import readInput
import java.awt.Point

fun main() {

    val startChar = 'S'
    val finishChar = 'E'

    fun iterate(rawInput: List<String>, startPoint: Point): Int {
        val visitedPoints = mutableSetOf<Point>()
        val mappedInput = rawInput.map { it.replace(startChar, 'a').replace(finishChar, 'z') }
        val finishRow = rawInput.indexOfFirst { it.contains(finishChar) }
        val finishColumn = rawInput[finishRow].indexOf(finishChar)
        val finishPoint = Point(finishColumn, finishRow)
        val map = MutableList(mappedInput.count()) { MutableList(mappedInput.first().count()) { Char.MAX_VALUE.code } }
        map.set(startPoint.x, startPoint.y, 0)

        val pointsToValidate = mutableListOf(startPoint)
        val left = Point(-1, 0)
        val right = Point(1, 0)
        val up = Point(0, -1)
        val down = Point(0, 1)
        val offsets = listOf(left, right, up, down)

        while (pointsToValidate.isNotEmpty()) {
            val point = pointsToValidate.removeAt(0)
            offsets
                .map { neighbourOffset -> Point(neighbourOffset.x + point.x, neighbourOffset.y + point.y) }
                .forEach { neighbourPoint ->
                    val neighbourChar = mappedInput.getOrNull(neighbourPoint.y)?.getOrNull(neighbourPoint.x)
                    if (neighbourChar != null) {
                        if (neighbourChar - mappedInput[point.y][point.x] <= 1) {
                            val currentNeighbourValue = map[neighbourPoint.y][neighbourPoint.x]
                            val currentPointValue = map[point.y][point.x] + 1
                            map.set(neighbourPoint.x, neighbourPoint.y, minOf(currentNeighbourValue, currentPointValue))

                            if (neighbourPoint !in visitedPoints && neighbourPoint !in pointsToValidate) {
                                pointsToValidate.add(neighbourPoint)
                            }
                        }
                    }
                }
            visitedPoints.add(point)
        }
        return map[finishPoint.y][finishPoint.x]
    }

    fun part1(input: List<String>): Int {
        val startRow = input.indexOfFirst { it.contains(startChar) }
        val startColumn = input[startRow].indexOf(startChar)
        val startPoint = Point(startColumn, startRow)
        return iterate(input, startPoint)
    }

    fun part2(input: List<String>): Int {
        val rowIndexes = mutableListOf<Int>()
        val startPoints = mutableListOf<Point>()
        input.forEachIndexed { index, row ->
            if (row.contains('a')) {
                rowIndexes.add(index)
            }
        }
        rowIndexes.forEach { rowIndex ->
            input[rowIndex].forEachIndexed { columnIndex, char ->
                if (char == 'a') {
                    startPoints.add(Point(columnIndex, rowIndex))
                }
            }
        }
        return startPoints.minOf { startPoint -> iterate(input, startPoint) }
    }

    val testInput = readInput("day12", isSample = true)
    val testResult = part1(testInput)
    check(testResult == 31) { "Part1 Check failed. Result was: $testResult, expected: 31" }

    val input = readInput("day12")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

private fun MutableList<MutableList<Int>>.set(x: Int, y: Int, value: Int) {
    this[y][x] = value
}