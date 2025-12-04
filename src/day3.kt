import java.io.File
import kotlin.math.pow

fun getMaxJolts(bank: String) : Int {
    var maxDigitPos = 0

    for (i in 1..<bank.length - 1) {
        if (bank[maxDigitPos].digitToInt() < bank[i].digitToInt()) {
            maxDigitPos = i
        }
    }

    var rightDigit = bank[bank.length - 1].digitToInt()
    for (i in bank.length - 2 downTo  maxDigitPos + 1) {
        val curDigit = bank[i].digitToInt()
        if (curDigit > rightDigit) {
            rightDigit = curDigit
        }
    }

    val leftDigit = bank[maxDigitPos].digitToInt()

    return leftDigit * 10 + rightDigit
}

fun getJoltsSum(banks: File) : Int {
    var sum = 0
    banks.forEachLine { sum += getMaxJolts(it) }
    return sum
}

fun getMaxJoltsForN(bank: String, batteries: Int, leftBound: Int) : Long {
    if (batteries == 0) return 0

    var maxDigitPos = leftBound

    for (i in leftBound+1..<bank.length-batteries+1) {
        if (bank[maxDigitPos].digitToInt() < bank[i].digitToInt()) {
            maxDigitPos = i
        }
    }
    val digit = bank[maxDigitPos].digitToInt()
    return digit * 10.0.pow(batteries - 1).toLong() + getMaxJoltsForN(bank, batteries-1, maxDigitPos+1)
}

fun getMaxJoltsFor12(bank: File) : Long {
    var sum: Long = 0
    bank.forEachLine { sum += getMaxJoltsForN(it, 12, 0) }
    return sum
}

fun main() {
    val inputFile = getFileForDay(3)
    println(getJoltsSum(inputFile))
    println(getMaxJoltsFor12(inputFile))
}