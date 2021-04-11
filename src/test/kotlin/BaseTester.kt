import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertTrue


/**
 * Base tester for [IndexTest]
 */
class BaseTester {
    private val bedReader = BedReaderClass()
    private val simpleBedFinder = SimpleBedFinder()


    /**
     * Deletes directory with index files
     */
    companion object {
        fun deleteIndex(indexPath: Path) {
            if (Files.exists(indexPath.resolve(".index"))) {
                indexPath.toFile().deleteRecursively()
            }
        }
    }


    /**
     * Checks if content of [first] is equal to content of [second]
     */
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


    /**
     * Test equality of [SimpleBedFinder.findWithOutIndex] and [BedReaderClass.findWithIndex] with given arguments
     */
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
                simpleBedFinder.findWithOutIndex(
                    bedPath,
                    chromosome,
                    start,
                    end
                )
            )
        )
    }


    /**
     * Runs [testFindWithIndex] on [simpleEntries].
     * [simpleEntries] contains [BedEntry] that has only [BedEntry.chromosome], [BedEntry.start] and [BedEntry.end]
     * for running [testFindWithIndex]
     */
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