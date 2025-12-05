import java.io.File
import kotlin.math.max
import kotlin.text.isEmpty
import kotlin.text.split

data class Range(val min: Long, val max: Long)

fun sortAndCombineRanges(ranges: List<Range>) : List<Range> {
    if (ranges.isEmpty()) {
        return emptyList()
    }

    val result = mutableListOf<Range>()
    val sortedRanges = ranges.sortedBy { it.min }
    result.add(sortedRanges[0])

    for (i in 1..<sortedRanges.size) {
        val lastRange = result[result.size - 1]
        val (prevMin, prevMax) = lastRange
        val (curMin, curMax) = sortedRanges[i]

        if (curMin <= prevMax) {
            result[result.size - 1] = Range(prevMin, max(prevMax, curMax))
        } else {
            result.add(sortedRanges[i])
        }
    }

    return result.toList()
}

fun isFresh(sortedRanges: List<Range>, ingredient: Long, leftBound: Int, rightBound: Int): Boolean {
    if (rightBound <= leftBound) {
        return false
    }

    val mid = leftBound + (rightBound - leftBound) / 2
    val curRange = sortedRanges[mid]
    if (ingredient in curRange.min..curRange.max) {
        return true
    }

    if (ingredient < curRange.min) {
        return isFresh(sortedRanges, ingredient, leftBound, mid)
    }

    return isFresh(sortedRanges, ingredient, mid + 1, rightBound)
}

fun isFresh(sortedRanges: List<Range>, ingredient: Long): Boolean {
    return isFresh(sortedRanges, ingredient, 0, sortedRanges.size)
}

fun getNumFreshIngredients(ranges: List<Range>, ingredients: List<Long>) : Int {
    var freshTotal = 0
    val sortedRanges = sortAndCombineRanges(ranges)

    for (ingredient in ingredients) {
        if (isFresh(sortedRanges, ingredient)) {
            freshTotal++
        }
    }
    return freshTotal
}

fun getNumFreshIngredients(ingredientsFile: File) : Int {
    val ranges = mutableListOf<Range>()
    val ingredients = mutableListOf<Long>()

    val lines = ingredientsFile.readLines()

    var index = 0

    while (index < lines.size && !lines[index].isEmpty()) {
        val numRangeStr = lines[index].split('-')
        ranges.add(Range(numRangeStr[0].toLong(), numRangeStr[1].toLong()))
        index++
    }

    index++

    while (index < lines.size) {
        ingredients.add(lines[index].toLong())
        index++
    }

    return getNumFreshIngredients(ranges.toList(), ingredients.toList())
}

fun getTotalPossibleFreshIngredients(ingredientsFile: File) : Long {
    val ranges = mutableListOf<Range>()

    val lines = ingredientsFile.readLines()

    var index = 0
    while (index < lines.size && !lines[index].isEmpty()) {
        val numRangeStr = lines[index].split('-')
        ranges.add(Range(numRangeStr[0].toLong(), numRangeStr[1].toLong()))
        index++
    }

    val sortedRanges = sortAndCombineRanges(ranges.toList())

    var sum: Long = 0
    for (range in sortedRanges) {
        sum += (range.max - range.min + 1)
    }

    return sum
}

fun main() {
    val inputFile = getFileForDay(5)
    println(getNumFreshIngredients(inputFile))
    println(getTotalPossibleFreshIngredients(inputFile))
}