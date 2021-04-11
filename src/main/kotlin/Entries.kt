/**
 * BedEntry data class
 */
data class BedEntry(
    val chromosome: String,
    val start: Int,
    val end: Int,
    val other: List<Any>
)

/**
 * IndexEntry data class
 * @property compare used for index file sort
 * @see IndexEntrySorter.preSort
 */
data class IndexEntry(
    val start: Int,
    val end: Int,
    val offset: Long
) {
    companion object : Comparator<IndexEntry> {
        override fun compare(p0: IndexEntry, p1: IndexEntry): Int {
            return p0.start - p1.start
        }
    }
}
