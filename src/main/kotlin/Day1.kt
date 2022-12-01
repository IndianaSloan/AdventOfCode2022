import Section.ONE
import Section.TWO

class Day1(
    private val input: String,
    private val section: Section,
) : AdventChallenge {

    override fun execute() {
        when (section) {
            ONE -> sectionOne()
            TWO -> sectionTwo()
        }
    }

    private fun sectionOne() {
        var highestTotal = 0
        var currentTotal = 0
        val calories = input.split("\n")
        calories.forEachIndexed { index, calorie ->
            if (calorie.isNotEmpty()) {
                currentTotal += calorie.toInt()
                if (index == calories.lastIndex) {
                    highestTotal = calculateHighestTotal(currentTotal, highestTotal)
                    currentTotal = 0
                }
            } else if (calorie.isEmpty()) {
                highestTotal = calculateHighestTotal(currentTotal, highestTotal)
                currentTotal = 0
            }
        }
        println("Highest Total: $highestTotal")
    }

    private fun sectionTwo() {
        val totals: MutableList<Int> = mutableListOf()
        var currentTotal = 0
        val calories = input.split("\n")
        calories.forEachIndexed { index, calorie ->
            if (calorie.isNotEmpty()) {
                currentTotal += calorie.toInt()
                if (index == calories.lastIndex) {
                    totals.add(currentTotal)
                    currentTotal = 0
                }
            } else if (calorie.isEmpty()) {
                totals.add(currentTotal)
                currentTotal = 0
            }
        }
        totals.sortDescending()
        val sum = totals.take(3).sum()
        println("Top3 Total: $sum")
    }

    private fun calculateHighestTotal(
        currentTotal: Int,
        highestTotal: Int,
    ): Int {
        return if (currentTotal > highestTotal) currentTotal else highestTotal
    }

    companion object {
        val SAMPLE_INPUT = """
                1000
                2000
                3000

                4000

                5000
                6000

                7000
                8000
                9000

                10000
        """.trimIndent()
    }
}
