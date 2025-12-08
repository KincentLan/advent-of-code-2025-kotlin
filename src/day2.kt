import java.io.File
import kotlin.math.log10
import kotlin.math.pow

fun getTopHalf(numStr: String) : Long {
    return numStr.slice(0..<numStr.length / 2).toLong()
}

fun getPowerOfTen(exponent: Int) : Long {
    return (10.0).pow(exponent).toLong()
}

fun makeRepeated(num: Long) : Long {
    val numDigits = log10(num.toDouble()).toInt()
    return num * 10.0.pow(numDigits + 1).toLong() + num
}

fun getDuplicateSum(min: String, max: String) : Long {
    var duplicateSum: Long = 0

    var curMin = min

    val minNum = min.toLong()
    val maxNum = max.toLong()

    while (curMin.length < max.length) {
        val size = curMin.length
        if (size % 2 == 0) {
            for (num in getTopHalf(curMin)..<getPowerOfTen(size / 2)) {
                val repeatedNum = makeRepeated(num)
                if (repeatedNum in minNum..maxNum) {
                    duplicateSum += repeatedNum
                }
            }
        }
        curMin = "${getPowerOfTen(size)}"
    }

    if (max.length % 2 == 0) {
        for (num in getTopHalf(curMin)..getTopHalf(max)) {
            val repeatedNum = makeRepeated(num)
            if (repeatedNum in minNum..maxNum) {
                duplicateSum += repeatedNum
            }
        }

    }

    return duplicateSum
}

fun getAllInvalidIds(idList: File) : Long {
    var duplicateSum: Long = 0

    val text = idList.readText().replace("\\s+".toRegex(), "")
    val numberRanges = text.split(",")

    for (numberRange in numberRanges) {
        val numbers = numberRange.split('-')
        val min = numbers[0]
        val max = numbers[1]
        duplicateSum += getDuplicateSum(min, max)
    }

    return duplicateSum
}

fun getTopDigits(numDigits: Int, numStr: String): String {
    return numStr.slice(0..<numDigits)
}

fun getRepeatedNumToSize(num: Long, size: Int): Long {
    val numStr = "$num"
    if (size % numStr.length != 0) {
        throw IllegalArgumentException("Number should be evenly divisible to size.")
    }
    return numStr.repeat(size / numStr.length).toLong()
}

fun getDuplicateSumTwo(min: String, max: String) : Long {
    var duplicateSum: Long = 0

    var curMin = min

    val minNum = min.toLong()
    val maxNum = max.toLong()

    val seen = HashSet<Long>()

    while (curMin.length < max.length) {
        val size = curMin.length
        for (numDigits in 1..size/2) {
            if (curMin.length % numDigits != 0) continue
            val bottomThreshold = getTopDigits(numDigits, curMin).toLong()
            for (curNum in bottomThreshold..<getPowerOfTen(numDigits)) {
                val repeated = getRepeatedNumToSize(curNum, curMin.length)
                if (repeated in minNum..maxNum && repeated !in seen) {
                    seen.add(repeated)
                    duplicateSum += repeated
                }
            }
        }
        curMin = "${getPowerOfTen(size)}"
    }


    for (numDigits in 1..max.length/2) {
        if (max.length % numDigits != 0) continue
        val bottomThreshold = getTopDigits(numDigits, curMin).toLong()
        for (curNum in bottomThreshold..getTopDigits(numDigits, max).toLong()) {
            val repeated = getRepeatedNumToSize(curNum, max.length)
            if (repeated in minNum..maxNum && repeated !in seen) {
                seen.add(repeated)
                duplicateSum += repeated
            }
        }
    }

    return duplicateSum
}

fun getAllInvalidIdsTwo(idList: File): Long {
    var duplicateSum: Long = 0

    val text = idList.readText().replace("\\s+".toRegex(), "")
    val numberRanges = text.split(",")

    for (numberRange in numberRanges) {
        val numbers = numberRange.split('-')
        val min = numbers[0]
        val max = numbers[1]
        duplicateSum += getDuplicateSumTwo(min, max)
    }

    return duplicateSum
}


fun main() {
    val inputFile = getFileForDay(2)
    println("Sum of all invalids: ${getAllInvalidIds(inputFile)}")
    println("Sum of all invalids (pt2): ${getAllInvalidIdsTwo(inputFile)}")
}