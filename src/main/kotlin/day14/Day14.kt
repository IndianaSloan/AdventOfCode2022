package day14

import day14.Object.*
import readInput
import java.awt.Point

sealed class Object {
    object Air : Object()
    object Rock : Object()
    object Sand : Object()
}

fun main() {

    val sandPour = Point(500, 0)

    fun setupGrid(input: List<String>, addFloor: Boolean = false): MutableList<MutableList<Object>> {
        // Map input and generate a grid of Air
        val lines = input.map { line ->
            line.split(" -> ").map {
                val coords = it.split(",")
                Point(coords[0].toInt(), coords[1].toInt())
            }.toMutableList()
        }.toMutableList()
        val x = lines.maxOf { it.maxOf { it.x } }
        val y = lines.maxOf { it.maxOf { it.y } }
        val xSize = if(addFloor) x + 1000 else x + 1
        val grid: MutableList<MutableList<Object>> = MutableList(y + 1) { MutableList(xSize) { Air } }

        // Draw each line
        lines.forEach { line ->
            line.forEachIndexed { index, fromPoint ->
                if (index != line.lastIndex) {
                    val toPoint = line[index + 1]
                    val xPoints = toPoint.x - fromPoint.x
                    val yPoints = toPoint.y - fromPoint.y
                    if (xPoints != 0) {
                        val range = if (xPoints > 0) (0..xPoints) else (0 downTo xPoints)
                        range.forEach { offset ->
                            grid[fromPoint.y][fromPoint.x + offset] = Rock
                        }
                    } else if (yPoints != 0) {
                        val range = if (yPoints > 0) (0..yPoints) else (0 downTo yPoints)
                        range.forEach { offset ->
                            grid[fromPoint.y + offset][fromPoint.x] = Rock
                        }
                    }
                }
            }
        }

        if (addFloor) {
            grid.add(MutableList(xSize) { Air })
            grid.add(MutableList(xSize) { Rock })
        }
        return grid
    }

    fun pourSand(grid: MutableList<MutableList<Object>>): Int {
        var count = 0
        var currentSandPoint = sandPour
        try {
            while (true) {
                if (grid[currentSandPoint.y][currentSandPoint.x] == Sand) {
                    break
                }
                when (grid[currentSandPoint.y + 1][currentSandPoint.x]) {
                    is Air -> {
                        currentSandPoint = Point(currentSandPoint.x, currentSandPoint.y + 1)
                    }
                    is Rock, is Sand -> {
                        val bottomLeftPoint = Point(currentSandPoint.x - 1, currentSandPoint.y + 1)
                        val bottomRightPoint = Point(currentSandPoint.x + 1, currentSandPoint.y + 1)
                        val bottomLeft = grid[bottomLeftPoint.y][bottomLeftPoint.x]
                        val bottomRight = grid[bottomRightPoint.y][bottomRightPoint.x]
                        when {
                            bottomLeft is Air -> {
                                currentSandPoint = bottomLeftPoint
                            }

                            bottomLeft !is Air && bottomRight is Air -> {
                                currentSandPoint = bottomRightPoint
                            }

                            bottomLeft !is Air && bottomRight !is Air -> {
                                grid[currentSandPoint.y][currentSandPoint.x] = Sand
                                currentSandPoint = sandPour
                                count++
                            }
                        }
                    }
                }
            }
        } catch (_: IndexOutOfBoundsException) { }
        return count
    }

    fun part1(input: List<String>): Int {
        val grid = setupGrid(input, addFloor = false)
        return pourSand(grid)
    }

    fun part2(input: List<String>): Int {
        val grid = setupGrid(input, addFloor = true)
        return pourSand(grid)
    }

    val testInput = readInput("day14", isSample = true)
    val testResult = part1(testInput)
    check(testResult == 24) { "Part1 Check failed. Result was: $testResult, expected: 24" }

    val input = readInput("day14")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}
