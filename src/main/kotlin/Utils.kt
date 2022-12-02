import java.io.File

fun readInput(name: String, isSample: Boolean = false) = File(
    "src/main/kotlin/${name}",
    "${if (isSample) "sample_input" else "challenge_input"}.txt"
).readLines()
