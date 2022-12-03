package day02

import day02.Play.*
import day02.WinResult.*
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { gameInput ->
            val (opponentKey, userKey) = gameInput.split(" ")
            val userPlay = when (userKey) {
                "X" -> ROCK
                "Y" -> PAPER
                "Z" -> SCISSOR
                else -> throw IllegalArgumentException("Unknown user input $userKey")
            }
            val game = Game(Play.values().first { it.key == opponentKey }, userPlay)
            val winResult = when (game.userPlay) {
                ROCK -> when (game.opponentPlay) {
                    ROCK -> DRAW
                    PAPER -> LOSS
                    SCISSOR -> WIN
                }
                PAPER -> when (game.opponentPlay) {
                    ROCK -> WIN
                    PAPER -> DRAW
                    SCISSOR -> LOSS
                }
                SCISSOR -> when (game.opponentPlay) {
                    ROCK -> LOSS
                    PAPER -> WIN
                    SCISSOR -> DRAW
                }
            }
            winResult.value + game.userPlay.value
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { gameInput ->
            val (opponentKey, resultKey) = gameInput.split(" ")
            val opponentPlay = Play.values().first { it.key == opponentKey }
            val winResult = WinResult.values().first { it.key == resultKey }
            val userPlay = when (winResult) {
                WIN -> when (opponentPlay) {
                    ROCK -> PAPER
                    PAPER -> SCISSOR
                    SCISSOR -> ROCK
                }
                LOSS -> when (opponentPlay) {
                    ROCK -> SCISSOR
                    PAPER -> ROCK
                    SCISSOR -> PAPER
                }
                DRAW -> opponentPlay
            }
            userPlay.value + winResult.value
        }
    }

    // test input
    val testInput = readInput("day02", isSample = true)
    check(part1(testInput) == 15) { "Part1 Check failed. Result was: ${part1(testInput)}, expected: 15" }
    check(part2(testInput) == 12) { "Part2 Check failed. Result was ${part2(testInput)}, expected 12" }

    val input = readInput("day02")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

data class Game(
    val opponentPlay: Play,
    val userPlay: Play,
)

enum class Play(
    val value: Int,
    val key: String
) {
    ROCK(1, "A"),
    PAPER(2, "B"),
    SCISSOR(3, "C"),
}

enum class WinResult(
    val value: Int,
    val key: String,
) {
    WIN(6, "Z"),
    DRAW(3, "Y"),
    LOSS(0, "X"),
}
