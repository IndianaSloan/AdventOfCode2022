package day08

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val treeGrid = input.map { it.map { it.digitToInt() } }
        val result = treeGrid.indices.flatMap { rowIndex ->
            treeGrid.indices.mapNotNull { columnIndex ->
                val tree = treeGrid[rowIndex][columnIndex]
                val leftTrees = treeGrid[rowIndex].subList(0, columnIndex)
                val rightTrees = treeGrid[rowIndex].subList(columnIndex + 1, treeGrid.size)
                val aboveTrees = treeGrid.subList(0, rowIndex).map { it[columnIndex] }
                val belowTrees = treeGrid.subList(rowIndex + 1, treeGrid.size).map { it[columnIndex] }
                val isVisible = leftTrees.canSeeTree(tree) || rightTrees.canSeeTree(tree) || aboveTrees.canSeeTree(tree) || belowTrees.canSeeTree(tree)
                if (isVisible) 1 else null
            }
        }
        return result.count()
    }

    fun part2(input: List<String>): Int {
        val treeGrid = input.map { it.map { it.digitToInt() } }
        val result = treeGrid.indices.flatMap { rowIndex ->
            treeGrid.indices.map { columnIndex ->
                val tree = treeGrid[rowIndex][columnIndex]
                val leftTrees = treeGrid[rowIndex].subList(0, columnIndex)
                val rightTrees = treeGrid[rowIndex].subList(columnIndex + 1, treeGrid.size)
                val aboveTrees = treeGrid.subList(0, rowIndex).map { it[columnIndex] }
                val belowTrees = treeGrid.subList(rowIndex + 1, treeGrid.size).map { it[columnIndex] }
                val leftScore = leftTrees.scoreOfTree(tree, true)
                val rightScore = rightTrees.scoreOfTree(tree)
                val aboveScore = aboveTrees.scoreOfTree(tree, true)
                val belowScore = belowTrees.scoreOfTree(tree)
                leftScore.times(rightScore).times(aboveScore).times(belowScore)
            }
        }
        return result.max()
    }

    val testInput = readInput("day08", isSample = true)
    val testResult = part1(testInput)
    check(testResult == 21) { "Part1 Check failed. Result was: $testResult, expected: 21" }

    val input = readInput("day08")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

fun List<Int>.canSeeTree(tree: Int): Boolean {
  return if (isEmpty()) true
  else (maxOrNull() ?: 0) < tree
}

fun List<Int>.scoreOfTree(tree: Int, reversed: Boolean = false): Int {
    val index = (if (reversed) reversed() else this).indexOfFirst { it >= tree }
    if (isEmpty()) return 0
    return if (index == -1) size else (index + 1)
}
