import java.nio.file.Path

class BedIndexClass(indexPath: Path) : BedIndex {
    private val chromosomeToIndexPath = mutableMapOf<String, Path>()

    init {
        for (fileName in indexPath.resolve(".index").toFile().readLines()) {
            val file = Path.of(fileName)
            chromosomeToIndexPath[file.toFile().nameWithoutExtension] = file
        }
    }

    override fun indexPathByChromosome(chromosome: String): Path {
        return chromosomeToIndexPath[chromosome]!!
    }
}