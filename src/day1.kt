import java.io.File
import kotlin.math.abs

fun rotate(dial: Int, magnitude: Int): Int {
     val diff = dial + magnitude
     val nextDial = diff % 100
     return if(nextDial < 0) { 100 + nextDial } else { nextDial }
}

fun calculateMagnitude(rotation: String): Int {
    val sign = if (rotation[0] == 'L') { -1 } else { 1 }
    val number = rotation.slice(1 until rotation.length).toInt()
    return sign * number
}

fun getPassword(rotationList: File): Int {
    var zeroCounter = 0
    var dial = 50
    rotationList.forEachLine {
        val magnitude = calculateMagnitude(it)
        dial = rotate(dial, magnitude)
        if (dial == 0) zeroCounter++
    }
    return zeroCounter
}

fun getPasswordWithMethod0x434(rotationList: File): Int {
    var zeroCounter = 0
    var dial = 50

    rotationList.forEachLine {
        val magnitude = calculateMagnitude(it)
        val prevDial = dial
        dial = rotate(dial, magnitude)

        zeroCounter += abs(magnitude / 100)

        if ((magnitude < 0 && prevDial < dial && prevDial != 0) ||
            (magnitude > 0 && prevDial > dial && prevDial != 0) ||
            dial == 0) {
            zeroCounter++
        }
    }

    return zeroCounter
}

fun main() {
    val inputFile = getFileForDay(1)
    val attemptedPassword = getPassword(inputFile)
    val password = getPasswordWithMethod0x434(inputFile)

    println("Attempted password: $attemptedPassword")
    println("Password: $password")
}