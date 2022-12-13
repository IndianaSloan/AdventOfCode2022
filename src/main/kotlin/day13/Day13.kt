package day13

import day13.Packet.PacketData
import day13.Packet.PacketList
import readInput

const val RIGHT_ORDER = -1
const val WRONG_ORDER = 1
const val MATCHING = 0
fun main() {

    fun part1(input: List<String>): Int {
        return input.asSequence().chunked(3).map { parseInput(it[0]) to parseInput(it[1]) }
            .map { (pack1, pack2) -> compare(pack1, pack2) }
            .mapIndexed { index, order -> index to order }
            .filter { (_, order) -> order == RIGHT_ORDER }
            .sumOf { (index, _) -> index + 1 }
    }

    fun part2(input: List<String>): Int {
        val dividerPackets = listOf(
            PacketList(listOf(PacketList(listOf(PacketData(2))))),
            PacketList(listOf(PacketList(listOf(PacketData(6)))))
        )
        val packets = input.chunked(3).flatMap { listOf(parseInput(it[0]), parseInput(it[1])) }.toMutableList()
        packets.addAll(dividerPackets)

        val sorted = packets.sortedWith { p1, p2 -> compare(p1, p2) }
        val i1 = sorted.indexOf(dividerPackets[0])
        val i2 = sorted.indexOf(dividerPackets[1])
        return (i1 + 1) * (i2 + 1)
    }

    val testInput = readInput("day13", isSample = true)
    val testResult = part1(testInput)
    check(testResult == 13) { "Part1 Check failed. Result was: $testResult, expected: 13" }

    val input = readInput("day13")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

sealed class Packet {
    data class PacketList(val packets: List<Packet>) : Packet()
    data class PacketData(val size: Int) : Packet()
}

fun parseInput(input: String): Packet {
    if (input[0].isDigit()) {
        return PacketData(input.toInt())
    }

    var brackets = 0
    var commaIndex = 0
    val packets = mutableListOf<Packet>()

    fun parseInnerInput(startIndex: Int) {
        val innerInput = input.take(startIndex).drop(commaIndex + 1)
        if (innerInput.isNotEmpty()) packets.add(parseInput(innerInput))
    }

    input.withIndex().forEach { (index, char) ->
        when (char) {
            '[' -> brackets++
            ']' -> {
                brackets--
                if (brackets == 0) {
                    parseInnerInput(index)
                }
            }

            ',' -> {
                if (brackets == 1) {
                    parseInnerInput(index)
                    commaIndex = index
                }
            }
        }
    }
    return PacketList(packets)
}

fun compare(packet1: Packet, packet2: Packet): Int {
    return when {
        packet1 is PacketData && packet2 is PacketData -> {
            packet1.size.compareTo(packet2.size)
        }

        packet1 is PacketList && packet2 is PacketData -> {
            compare(packet1, PacketList(listOf(packet2)))
        }

        packet1 is PacketData && packet2 is PacketList -> {
            compare(PacketList(listOf(packet1)), packet2)
        }

        else -> compare(packet1 as PacketList, packet2 as PacketList)
    }
}

fun compare(packet1: PacketList, packet2: PacketList): Int {
    val packetPairs = packet1.packets.zip(packet2.packets)
    packetPairs.forEach { (pack1, pack2) ->
        val order = compare(pack1, pack2)
        if (order != MATCHING) {
            return order
        }
    }
    return when {
        packet1.packets.count() == packet2.packets.count() -> MATCHING
        packet1.packets.count() < packet2.packets.count() -> RIGHT_ORDER
        else -> WRONG_ORDER
    }
}