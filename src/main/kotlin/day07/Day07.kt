package day07

import readInput
import java.io.File

const val MAX_FILE_SIZE = 100_000L
const val TOTAL_DISK_SIZE = 70_000_000L
const val UNUSED_SPACE = 30_000_000L
fun main() {

    val root = File("src/main/kotlin/day07/root")

    fun constructFileSystem(input: List<String>) {
        var fileSystem = root
        if (fileSystem.exists()) {
            fileSystem.deleteRecursively()
        }
        input.forEach { command ->
            val commandParts = command.split(" ")
            when (commandParts[0].startsWith("$")) {
                true -> {
                    if (commandParts[1] == "cd") {
                        when (val dirName = commandParts[2]) {
                            ".." -> fileSystem = fileSystem.parentFile
                            "/" -> fileSystem.mkdir()
                            else -> fileSystem = fileSystem.resolve(dirName)
                        }
                    }
                }

                false -> {
                    val fileName = commandParts[1]
                    when (commandParts[0]) {
                        "dir" -> fileSystem.resolve(fileName).mkdir()
                        else -> {
                            val fileSize = ByteArray(commandParts[0].toInt())
                            fileSystem.resolve(fileName).writeBytes(fileSize)
                        }
                    }
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        constructFileSystem(input)
        return root.getDirs()
            .filter { it.size() < MAX_FILE_SIZE }
            .sumOf { it.size() }
    }

    fun part2(input: List<String>): Long {
        constructFileSystem(input)

        val currentFree = TOTAL_DISK_SIZE - root.size()
        val requiredFree = UNUSED_SPACE - currentFree

        return root.getDirs()
            .filter { it.size() >= requiredFree }
            .minBy { it.size() }
            .size()
    }

    val testInput = readInput("day07", isSample = true)
    check(part1(testInput) == 95437L) { "Part1 Check failed. Result was: ${part1(testInput)}, expected: 95437" }

    val input = readInput("day07", isSample = false)
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

private fun File.size(): Long {
    return if (isDirectory) {
        walk().filter { it.isFile }.sumOf { it.length() }
    } else {
        length()
    }
}

private fun File.getDirs(): List<File> {
    return walk().filter { it.isDirectory }.toList()
}