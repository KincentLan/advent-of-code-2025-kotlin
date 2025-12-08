import java.io.File
import kotlin.math.max

fun getSumOfOperations(file: File) : Long {
    val lines = file.readLines()
    val tokens = mutableListOf<List<String>>()

    for (line in lines) {
        val curTokens = line.trim().split("\\s+".toRegex())
        tokens.add(curTokens.map { it.trim() })
    }

    val values = tokens[0].map { it.toLong() }.toMutableList()
    val operators = tokens[tokens.size - 1]
    for (i in 1..<tokens.size-1) {
        for (j in 0..<tokens[i].size) {
            if (operators[j] == "*") {
                values[j] = values[j] * tokens[i][j].toLong()
            } else if (operators[j] == "+") {
                values[j] += tokens[i][j].toLong()
            }
        }
    }

    return values.sum()
}

fun getSumOfOperationsCephalopod(file: File): Long {
    val lines = file.readLines()
    val numbers = mutableListOf<List<Long>>()
    val curNumbers = mutableListOf<Long>()

    var maxSize = 0
    for (line in lines) {
        maxSize = max(line.length, maxSize)
    }

    for (i in 0..<maxSize) {
        var number: Long = 0
        var isSet = false

        for (j in 0..<lines.size) {
            val line = lines[j]
            if (i >= lines[j].length) continue
            if (line[i].isDigit()) {
                val curDigit = line[i].digitToInt()
                isSet = true
                number = number * 10 + curDigit
            }

        }
        if (isSet) {
            curNumbers.add(number)
        } else {
            numbers.add(curNumbers.toList())
            curNumbers.clear()
        }
    }

    numbers.add(curNumbers.toList())

    var sum: Long = 0


    val operators = lines[lines.size - 1].trim().split("\\s+".toRegex()).reversed()

    val reversed = numbers.map { it.reversed() }.reversed()

    for (i in operators.indices) {
        var num = reversed[i][0]
        val multiply = operators[i] == "*"
        for (j in 1..<reversed[i].size) {
            if (multiply) {
                num *= reversed[i][j]
            } else {
                num += reversed[i][j]
            }
        }
        sum += num
    }

    return sum
}

fun main() {
    val inputFile = getFileForDay(6)
    println((getSumOfOperations(inputFile)))
    println(getSumOfOperationsCephalopod(inputFile))
}