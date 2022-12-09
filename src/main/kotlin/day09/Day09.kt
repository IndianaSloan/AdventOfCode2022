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
    val knots = mutableSetOf<Knot>()
    val rope = MutableList(length) { Knot(0, 0) }
    instructions.forEach { (direction, steps) ->
        (0 until steps).forEach { _ ->
            rope.forEachIndexed { index, knot ->
                if (index == 0) {
                    rope[index] = knot.moveKnot(direction)
                    knot.moveKnot(direction)
                } else {
                    val leadKnot = rope[index - 1]
                    if (!knot.isTouching(leadKnot)) {
                        rope[index] = knot.moveTowards(leadKnot)
                    }
                }
            }
            knots.add(rope.last())
        }
    }
    return knots.count()
}

private fun parseInstructions(input: List<String>): List<Pair<String, Int>> = input.map {
    val (dir, steps) = it.split(" ")
    dir to steps.toInt()
}

private data class Knot(val x: Int, val y: Int) {

    fun isTouching(lead: Knot): Boolean = x - lead.x in (-1..1) && y - lead.y in (-1..1)

    fun moveKnot(direction: String): Knot {
        return when (direction) {
            "U" -> Knot(x, y + 1)
            "D" -> Knot(x, y - 1)
            "L" -> Knot(x - 1, y)
            "R" -> Knot(x + 1, y)
            "UR" -> Knot(x + 1, y + 1)
            "UL" -> Knot(x - 1, y + 1)
            "DL" -> Knot(x - 1, y - 1)
            "DR" -> Knot(x + 1, y - 1)
            else -> Knot(x, y)
        }
    }

    fun moveTowards(lead: Knot) = moveKnot(getMoveDirectionToLead(lead))

    fun getMoveDirectionToLead(lead: Knot): String {
        val illegalDirection =
            IllegalStateException("Unknown directional for Point($x, $y) to Point(${lead.x}, ${lead.y})")
        val yMove = lead.y - y
        val xMove = lead.x - x
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
}