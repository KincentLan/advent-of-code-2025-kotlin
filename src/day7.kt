import java.io.File

fun manifoldPosition(graph: List<CharArray>): Int {
    for (i in 0..<graph[0].size) {
        if (graph[0][i] == 'S') {
            return i
        }
    }
    throw IllegalStateException("Should have to be a state.")
}

fun calculateSplits(graph: List<CharArray>, coordinate: Coordinate): Int {
    val (row, col) = coordinate
    if (row !in 0..<graph.size || col !in 0..<graph[row].size) {
        return 0
    }
    val curChar = graph[row][col]
    if (curChar == '^') {
        val left = Coordinate(row, col - 1)
        val right = Coordinate(row, col + 1)
        return 1 + calculateSplits(graph, left) + calculateSplits(graph, right)
    } else if (curChar == '.') {
        graph[row][col] = '|'
        return calculateSplits(graph, Coordinate(row + 1, col))
    }
    return 0
}

fun calculateSplits(file: File): Int {
    val graph = file.readLines().map { it.toCharArray() }
    val manifoldPos = manifoldPosition(graph)
    return calculateSplits(graph, Coordinate(1, manifoldPos))
}

fun calculateQuantumSplits(graph: List<CharArray>, coordinate: Coordinate, map: HashMap<Coordinate, Long>): Long {
    val (row, col) = coordinate
    if (row == graph.size - 1 && col in 0..<graph[0].size) {
        return 1
    }
    if (row !in 0..<graph.size || col !in 0..<graph[row].size) {
        return 0
    }
    val curChar = graph[row][col]
    if (coordinate in map) {
        return map.get(coordinate)!!
    }
    if (curChar == '^') {
        val left = Coordinate(row, col - 1)
        val right = Coordinate(row, col + 1)
        val sum = calculateQuantumSplits(graph, left, map) + calculateQuantumSplits(graph, right, map)
        map[coordinate] = sum
        return sum
    }
    val sum = calculateQuantumSplits(graph, Coordinate(row + 1, col), map)
    map[coordinate] = sum
    return sum
}

fun calculateQuantumSplits(file: File): Long {
    val graph = file.readLines().map { it.toCharArray() }
    val manifoldPos = manifoldPosition(graph)
    return calculateQuantumSplits(graph, Coordinate(1, manifoldPos), HashMap())
}

fun main() {
    val inputFile = getFileForDay(7)
    println(calculateSplits(inputFile))
    println(calculateQuantumSplits(inputFile))
}