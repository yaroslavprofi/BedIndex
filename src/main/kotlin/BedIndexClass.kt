import java.nio.file.Path


/**
 * Implement class for [BedIndex]
 */
class BedIndexClass(indexPath: Path) : BedIndex {
    private val chromosomeToIndexPath = mutableMapOf<String, Path>()


    /**
     * Saves index filenames into map that matches chromosome to its index path
     */
    init {
        for (fileName in indexPath.resolve(".index").toFile().readLines()) {
            val file = Path.of(fileName)
            chromosomeToIndexPath[file.toFile().nameWithoutExtension] = file
        }
    }


    /**
     * Returns [Path] to chromosome index path
     */
    override fun indexPathByChromosome(chromosome: String): Path {
        return chromosomeToIndexPath[chromosome]!!
    }
}