import java.io.File

data class Coordinate(val row: Int, val col: Int)

val DIRECTIONS = listOf(
    Coordinate(-1, -1), // Top Left
    Coordinate(-1, 0), // Top
    Coordinate(-1, 1), // Top Right
    Coordinate(0, -1), // Left
    Coordinate(0, 1), // Right
    Coordinate(1, -1), // Bottom Left
    Coordinate(1, 0), // Bottom
    Coordinate(1, 1), // Bottom Right
)

fun isAccessible(maze: List<CharArray>, coordinate: Coordinate) : Boolean {
    val row = coordinate.row
    val col = coordinate.col
    if (maze[row][col] != '@') {
        throw IllegalArgumentException("Must be a roll.")
    }

    var totalRollsInVicinity = 0
    for (dir in DIRECTIONS) {
        val nextRow = row + dir.row
        val nextCol = col + dir.col
        if (nextRow in 0..<maze.size && nextCol in 0..<maze[nextRow].size && maze[nextRow][nextCol] == '@') {
            totalRollsInVicinity++
        }
    }

    return totalRollsInVicinity < 4
}

fun calculateAccessibleRolls(maze: List<CharArray>) : Int {
    var totalAccessible = 0

    for (row in maze.indices) {
        for (col in maze[row].indices) {
            if (maze[row][col] == '@' && isAccessible(maze, Coordinate(row, col)))  {
                totalAccessible += 1
            }
        }
    }

    return totalAccessible
}

fun getMaze(mazeFile: File) : List<CharArray> {
    val maze = mutableListOf<CharArray>()

    mazeFile.forEachLine {
        maze.add(it.toCharArray())
    }

    return maze.toList()
}

fun calculateAccessibleRolls(mazeFile: File) : Int {
    return calculateAccessibleRolls(getMaze(mazeFile))
}

fun removeRolls(maze: List<CharArray>, coordinate: Coordinate) : Int {
    val row = coordinate.row
    val col = coordinate.col
    if (row !in 0..<maze.size || col !in 0..<maze[row].size || maze[row][col] == '.') return 0

    var removed = 0
    if (isAccessible(maze, coordinate)) {
        maze[row][col] = '.'
        removed++
        for (dir in DIRECTIONS) {
            removed += removeRolls(maze, Coordinate(row + dir.row, col + dir.col))
        }
    }

    return removed
}

fun calculateRollsRemoved(maze: List<CharArray>) : Int{
    var removed = 0

    for (row in maze.indices) {
        for (col in maze[row].indices) {
            removed += removeRolls(maze, Coordinate(row, col))
        }
    }
    return removed
}

fun calculateRollsRemoved(mazeFile: File): Int {
    return calculateRollsRemoved(getMaze(mazeFile))
}

fun main() {
    val inputFile = getFileForDay(4)
    println(calculateAccessibleRolls(inputFile))
    println(calculateRollsRemoved(inputFile))
}