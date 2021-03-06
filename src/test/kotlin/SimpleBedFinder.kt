import parsers.BedEntryParser
import java.nio.file.Path

class SimpleBedFinder {

    /**
     * Works as [BedReader.findWithIndex] but does not use index files.
     * Search for required [BedEntry]s by reading the whole [bedPath] file
     */
    fun findWithOutIndex(
        bedPath: Path,
        chromosome: String,
        start: Int,
        end: Int
    ): List<BedEntry> {
        val reader = bedPath.toFile().bufferedReader(Charsets.UTF_8)
        var line: String? = reader.readLine()
        val res = arrayListOf<BedEntry>()
        while (line != null) {
            val params = line.split(Regex(" +"))
            if (params[0] == "browser" || params[0] == "track") {
                line = reader.readLine()
                continue
            }
            val entry = BedEntryParser(line).parse()
            if (entry.chromosome == chromosome
                && entry.start >= start
                && entry.end <= end
            ) {
                res.add(entry)
            }
            line = reader.readLine()
        }
        reader.close()
        return res
    }
}