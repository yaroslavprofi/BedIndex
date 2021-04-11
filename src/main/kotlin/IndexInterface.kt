import java.nio.file.Path

interface BedIndex {

    /**
     * Saves index filenames into map that matches chromosome to its index path
     */
    fun indexPathByChromosome(chromosome: String): Path
}

interface BedReader {

    /**
     * Creates index for [bedPath] and saves it to [indexPath]
     */
    fun createIndex(bedPath: Path, indexPath: Path)

    /**
     * Loads [BedIndex] instance from file [indexPath]
     */
    fun loadIndex(indexPath: Path): BedIndex

    /**
     * Loads list of [BedEntry] from file [bedPath] using [index].
     * All the loaded entries should be located on the given [chromosome],
     * and be inside the range from [start] inclusive to [end] exclusive.
     * E.g. entry [1, 2) is inside [0, 2), but not inside [0, 1).
     */
    fun findWithIndex(
        index: BedIndex, bedPath: Path,
        chromosome: String, start: Int, end: Int
    ): List<BedEntry>

}