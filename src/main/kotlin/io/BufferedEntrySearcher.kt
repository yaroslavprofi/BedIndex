package io

import IndexEntry
import parsers.IndexEntryParser
import java.lang.Long.max
import java.nio.file.Path
import java.util.function.Predicate


/**
 * Class that performs binary search in file that is supposed to be sorted
 */
class BufferedEntrySearcher(
    pathToSortedEntryFile: Path,
    private val bufferSize: Int = Constants.B
) : BaseBufferedReader(pathToSortedEntryFile, bufferSize) {


    /**
     * Reads [bufferSize] bytes in left if necessary and returns [char]
     */
    private fun charLeft(): Char {
        if (index < bufferOffsetStart || index >= bufferOffsetEnd) {
            read(max(0L, index - bufferSize + 1))
        }
        return char()
    }


    /**
     * Parse nearest entry string into [IndexEntry]
     */
    private fun indexEntryAt(newIndex: Long): IndexEntry {
        setOffset(newIndex)
        return IndexEntryParser(nextEntry()).parse()
    }


    /**
     * Returns nearest to [newIndex] offset that points to the beginning of [IndexEntry] string representation
     */
    private fun nearestEntry(newIndex: Long): Long {
        setOffset(newIndex)
        skipWithPredicate(Char::isWhitespace)
        skipWithPredicate { ch: Char -> ch != 'I' }
        return index
    }


    /**
     * Skips [charLeft] if it matches [predicate] and decrement [index]
     */
    private fun skipWithPredicate(predicate: Predicate<Char>) {
        while (index >= 0 && predicate.test(charLeft())) {
            index--
        }
    }


    /**
     * Returns offset that points to the beginning of first [IndexEntry] string representation that
     * has [IndexEntry.start] >= [value]
     */
    fun firstNoLessThan(value: Int): Long {
        var right: Long = nearestEntry(fileEnd)
        if (indexEntryAt(right).start < value) {
            return -1
        }
        var left: Long = nearestEntry(0)
        var mid: Long = nearestEntry(left + (right - left) / 2)
        while (left != mid && right != mid) {
            if (indexEntryAt(mid).start < value) {
                left = mid
            } else {
                right = mid
            }
            mid = nearestEntry(left + (right - left) / 2)
        }
        if (indexEntryAt(nearestEntry(left)).start >= value) {
            return left
        }
        return right
    }
}
