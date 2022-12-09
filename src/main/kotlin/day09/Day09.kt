package day09

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val instructions = parseInstructions(input)
        return processRopeMoves(instructions, 2)
    }

    fun part2(input: List<String>): Int {
        val instructions = parseInstructions(input)
        return processRopeMoves(instructions, 10)
    }

    val testInput = readInput("day09", isSample = true)
    val testResult = part1(testInput)
    check(testResult == 13) { "Part1 Check failed. Result was: $testResult, expected: 13" }

    val input = readInput("day09")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

fun processRopeMoves(instructions: List<Pair<String, Int>>, length: Int): Int {
    val tailPoints = mutableSetOf<Point>()
    val rope = MutableList(length) { Point(0, 0) }
    instructions.forEach { (direction, steps) ->
        (0 until steps).forEach { _ ->
            rope.forEachIndexed { index, knot ->
                if (index == 0) {
                    rope[index] = knot.movePoint(direction)
                    knot.movePoint(direction)
                } else {
                    val previousKnot = rope[index - 1]
                    if (!knot.isTouching(previousKnot)) {
                        rope[index] = knot.moveTowards(previousKnot)
                    }
                }
            }
            tailPoints.add(rope.last())
        }
    }
    return tailPoints.count()
}

fun parseInstructions(input: List<String>): List<Pair<String, Int>> = input.map {
    val (dir, steps) = it.split(" ")
    dir to steps.toInt()
}

data class Point(var x: Int, var y: Int)

fun Point.movePoint(direction: String): Point {
    return when (direction) {
        "U" -> Point(x, y + 1)
        "D" -> Point(x, y - 1)
        "L" -> Point(x - 1, y)
        "R" -> Point(x + 1, y)
        "UR" -> Point(x + 1, y + 1)
        "UL" -> Point(x - 1, y + 1)
        "DL" -> Point(x - 1, y - 1)
        "DR" -> Point(x + 1, y - 1)
        else -> Point(x, y)
    }
}

fun Point.isTouching(other: Point): Boolean = x - other.x in (-1..1) && y - other.y in (-1..1)
fun Point.moveTowards(other: Point) = movePoint(getDirection(other))

fun Point.getDirection(other: Point): String {
    val illegalDirection =
        IllegalStateException("Unknown directional for Point($x, $y) to Point(${other.x}, ${other.y})")
    val yMove = other.y - y
    val xMove = other.x - x
    return if (yMove == 2) {
        when (xMove) {
            -1, -2 -> "UL"
            0 -> "U"
            1, 2 -> "UR"
            else -> throw illegalDirection
        }
    } else if (yMove == -2) {
        when (xMove) {
            -1, -2 -> "DL"
            0 -> "D"
            1, 2 -> "DR"
            else -> throw illegalDirection
        }
    } else if (xMove == 2) {
        when (yMove) {
            -1 -> "DR"
            0 -> "R"
            1 -> "UR"
            else -> throw illegalDirection
        }
    } else if (xMove == -2) {
        when (yMove) {
            -1 -> "DL"
            0 -> "L"
            1 -> "UL"
            else -> throw illegalDirection
        }
    } else throw illegalDirection
}

