import java.io.File
import java.util.Objects

data class Edge(val c1: Coordinate3d, val c2: Coordinate3d) {
    override fun equals(other: Any?): Boolean {
        if (this == other) return true
        if (other !is Edge) return false
        return (this.c1 == other.c1 && this.c2 == other.c2) ||
                (this.c1 == other.c2 && this.c2 == other.c1)
    }

    override fun hashCode(): Int {
        return Objects.hash(c1, c2)
    }
}

data class EdgeDistance(val edge: Edge, val distance: Long)

fun calculateDistance(c1: Coordinate3d, c2: Coordinate3d): Long {
    val xDist: Long = (c2.x - c1.x).toLong()
    val yDist: Long = (c2.y - c1.y).toLong()
    val zDist: Long = (c2.z - c1.z).toLong()

    return xDist * xDist + yDist * yDist + zDist * zDist
}

fun calculateEdgesSortedByDistance(graph: List<Coordinate3d>): List<Edge> {
    val edgeAndDistances = mutableListOf<EdgeDistance>()
    for (i in graph.indices) {
        val curNode = graph[i]
        for (j in i+1..<graph.size) {
            val nextNode = graph[j]
            val distance = calculateDistance(curNode, nextNode)
            edgeAndDistances.add(EdgeDistance(Edge(curNode, nextNode), distance))
        }
    }
    return edgeAndDistances.sortedBy { it.distance }.map { it.edge }.toList()
}

fun find(identities: IntArray, position: Int): Int {
    var parentPos = position
    while (identities[parentPos] != parentPos) {
        parentPos = identities[parentPos]
    }
    return parentPos
}

fun calculateJunctionProduct(graphFile: File): Long {
    val graph = graphFile.readLines().map { line ->
        val numbers = line.split(",")
        Coordinate3d(numbers[0].toInt(), numbers[1].toInt(), numbers[2].toInt())
    }.toList()

    val positions = HashMap<Coordinate3d, Int>()
    val identities = IntArray(graph.size)
    for (i in identities.indices) {
        identities[i] = i
        positions[graph[i]] = i
    }

    val edges = calculateEdgesSortedByDistance(graph)
    for (i in 0..<1000) {
        val edge = edges[i]
        val (c1, c2) = edge
        val c1Parent = find(identities, positions[c1]!!)
        val c2Parent = find(identities, positions[c2]!!)
        if (c1Parent == c2Parent) continue
        identities[c2Parent] = c1Parent
    }

    val counts = mutableMapOf<Int, Long>()
    for (i in identities.indices) {
        val group = find(identities, i)
        counts[group] = counts.getOrDefault(group, 0) + 1
    }

    val sortedCounts = counts.values.sorted().reversed()
    return sortedCounts[0] * sortedCounts[1] * sortedCounts[2]
}

fun calculateJunctionProductForLastEdge(graphFile: File): Long {
    val graph = graphFile.readLines().map { line ->
        val numbers = line.split(",")
        Coordinate3d(numbers[0].toInt(), numbers[1].toInt(), numbers[2].toInt())
    }.toList()

    val positions = HashMap<Coordinate3d, Int>()
    val identities = IntArray(graph.size)
    for (i in identities.indices) {
        identities[i] = i
        positions[graph[i]] = i
    }

    val edges = calculateEdgesSortedByDistance(graph)
    var lastEdgeXProd: Long = 0
    for (i in edges.indices) {
        val edge = edges[i]
        val (c1, c2) = edge
        val c1Parent = find(identities, positions[c1]!!)
        val c2Parent = find(identities, positions[c2]!!)
        if (c1Parent == c2Parent) continue
        identities[c2Parent] = c1Parent
        lastEdgeXProd = c1.x.toLong() * c2.x.toLong()
    }

    return lastEdgeXProd
}


fun main() {
    val inputFile = getFileForDay(8)
    println(calculateJunctionProduct(inputFile))
    println(calculateJunctionProductForLastEdge(inputFile))
}