import io.BufferedEntrySearcher
import io.BaseBufferedReader
import parsers.BedEntryParser
import parsers.IndexEntryParser
import java.io.RandomAccessFile
import java.nio.file.*


/**
 * Implement class for [BedReader]
 */
class BedReaderClass : BedReader {


    /**
     * Creates index files in [indexPath] directory and sorts them.
     * File `[indexPath].index` contains all index files names
     *
     * @see IndexEntrySorter
     */
    override fun createIndex(bedPath: Path, indexPath: Path) {
        val bed = RandomAccessFile(bedPath.toFile(), "r")
        if (Files.exists(indexPath)) {
            System.err.println("Directory $indexPath already exists. Cannot create index files in non empty directory")
            return
        }
        Files.createDirectory(indexPath)
        val fileNames = Files.createFile(indexPath.resolve(".index"))

        var offset = 0L
        var line = bed.readLine()
        while (line != null) {
            val params = line.split(Regex(" +"))
            if (params[0] == "browser" || params[0] == "track") {
                offset = bed.filePointer
                line = bed.readLine()
                continue
            }
            val indexFile = indexPath.resolve("${params[0]}.index")
            if (!indexFile.toFile().exists()) {
                Files.createFile(indexFile)
                Files.write(
                    fileNames,
                    "${indexFile.toAbsolutePath()}${Constants.LINE_SEPARATOR}".toByteArray(),
                    StandardOpenOption.APPEND
                )
            }
            val entry = IndexEntry(
                params[1].toInt(),
                params[2].toInt(),
                offset
            )
            Files.write(
                indexPath.resolve("${params[0]}.index"),
                "$entry ".toByteArray(),
                StandardOpenOption.APPEND
            )
            offset = bed.filePointer
            line = bed.readLine()
        }
        for (fileName in fileNames.toFile().readLines()) {
            IndexEntrySorter(Path.of(fileName)).sort()
        }
    }


    /**
     * Returns instance of [BedIndexClass]
     */
    override fun loadIndex(indexPath: Path): BedIndex {
        return BedIndexClass(indexPath)
    }


    /**
     * Finds all [BedEntry] that matches pattern: [BedEntry.chromosome] equals to [chromosome],
     * [BedEntry.start] >= [start] and [BedEntry.end] <= [end] using binary search on sorted file provided by [index]
     *
     * @see BufferedEntrySearcher
     */
    override fun findWithIndex(
        index: BedIndex,
        bedPath: Path,
        chromosome: String,
        start: Int,
        end: Int
    ): List<BedEntry> {
        val chromFile = index.indexPathByChromosome(chromosome)
        val searcher = BufferedEntrySearcher(
            index.indexPathByChromosome(chromosome)
        )
        val offset = searcher.firstNoLessThan(start)
        searcher.close()
        if (offset < 0) {
            System.err.println("no line in file with such start: $start")
            return listOf()
        }

        val bed = RandomAccessFile(bedPath.toFile(), "r")
        val reader = BaseBufferedReader(chromFile)
        reader.setOffset(offset)
        val res = arrayListOf<BedEntry>()
        while (reader.hasNext()) {
            val entry = IndexEntryParser(reader.nextEntry()).parse()
            if (entry.start >= end) {
                break
            }
            if (entry.start >= start && entry.end <= end) {
                bed.seek(entry.offset)
                res.add(BedEntryParser(bed.readLine()).parse())
            }
        }
        reader.close()
        return res
    }
}
