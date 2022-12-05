package day05

import readInput

fun main() {

    fun buildStacks(input: List<String>): MutableMap<Int, MutableList<Char>> {
        val headerIndex = input.indexOfFirst { it.startsWith(" 1 ") }
        val stacks = mutableMapOf<Int, MutableList<Char>>()
        input.take(headerIndex).forEach { s ->
            val chars = s.toList().chunked(4).map {
                val lastDrop = if (it.count() > 3) 2 else 1
                it.dropLast(lastDrop).drop(1)
            }.flatten()
            chars.forEachIndexed { i, c ->
                val stack = stacks[i + 1] ?: mutableListOf()
                if (c.isLetter()) {
                    stack.add(0, c)
                }
                stacks[i + 1] = stack
            }
        }
        return stacks
    }

    fun buildInstructions(input: List<String>): List<List<Int>> {
        val headerIndex = input.indexOfFirst { it.startsWith(" 1 ") }
        return input.drop(headerIndex + 2).map { s ->
            s.replace("move ", "")
                .replace(" from ", ",")
                .replace(" to ", ",")
                .split(",")
                .map { it.toInt() }
        }
    }

    fun moveStacks(stacks: MutableMap<Int, MutableList<Char>>, from: Int, to: Int, moveQty: Int): MutableMap<Int, MutableList<Char>> {
        val fromStack = stacks[from]!!
        val toStack = stacks[to]!!
        toStack.addAll(fromStack.takeLast(moveQty))
        stacks[from] = fromStack.dropLast(moveQty).toMutableList()
        stacks[to] = toStack
        return stacks
    }

    fun part1(input: List<String>): String {
        val stacks = buildStacks(input)
        buildInstructions(input).forEach { inst ->
            (1..inst[0]).forEach { _ ->
                moveStacks(stacks, inst[1], inst[2], 1)
            }
        }
        return stacks.map { (_, stack) -> stack.last() }.joinToString(separator = "")
    }

    fun part2(input: List<String>): String {
        val stacks = buildStacks(input)
        buildInstructions(input).forEach { inst ->
            moveStacks(stacks, inst[1], inst[2], inst[0])
        }
        return stacks.map { (_, stack) -> stack.last() }.joinToString(separator = "")
    }

    // test input
    val testInput = readInput("day05", isSample = true)
    check(part1(testInput) == "CMZ") { "Part1 Check failed. Result was: ${part1(testInput)}, expected: \"CMZ\"" }

    val input = readInput("day05")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}
