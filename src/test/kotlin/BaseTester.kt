import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertTrue

class BaseTester {
    private val bedReader = BedReaderClass()
    private val dummyReader = DummyBedFinder()

    companion object {
        fun deleteIndex(indexPath: Path) {
            if (Files.exists(indexPath.resolve(".index"))) {
                indexPath.toFile().deleteRecursively()
            }
        }
    }

    private fun listEquals(
        first: List<BedEntry>,
        second: List<BedEntry>
    ): Boolean {
        if (first.size != second.size) {
            return false
        }
        for (entry1 in first) {
            var flag = true
            for (entry2 in second) {
                if (entry1 == entry2) {
                    flag = false
                    break
                }
            }
            if (flag) {
                return false
            }
        }
        return true
    }

    private fun testFindWithIndex(
        bedPath: Path,
        bedIndex: BedIndex,
        chromosome: String,
        start: Int,
        end: Int
    ) {
        assertTrue(
            listEquals(
                bedReader.findWithIndex(
                    bedIndex,
                    bedPath,
                    chromosome,
                    start,
                    end
                ),
                dummyReader.findWithIndex(
                    bedPath,
                    chromosome,
                    start,
                    end
                )
            )
        )
    }

    fun test(
        bedPath: Path,
        indexPath: Path,
        simpleEntries: List<BedEntry>
    ) {
        deleteIndex(indexPath)
        bedReader.createIndex(bedPath, indexPath)
        for (simpleEntry in simpleEntries) {
            testFindWithIndex(
                bedPath,
                bedReader.loadIndex(indexPath),
                simpleEntry.chromosome,
                simpleEntry.start,
                simpleEntry.end
            )
        }
        deleteIndex(indexPath)
    }

}