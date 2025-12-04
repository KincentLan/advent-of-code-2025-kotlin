import kotlin.io.path.Path
import java.io.File
import kotlin.io.path.absolutePathString

fun getFileForDay(day: Int): File {
    return File(Path("src/inputs/day$day.txt").absolutePathString())
}