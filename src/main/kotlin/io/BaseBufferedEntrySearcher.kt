package io

import B
import IndexEntry
import parsers.IndexEntryParser
import java.lang.Long.max
import java.nio.file.Path
import java.util.function.Predicate

class BaseBufferedEntrySearcher(
    path: Path,
    private val bufferSize: Int = B
) : BaseBufferedReader(path, bufferSize) {

    private fun charLeft(): Char {
        if (index < bufferOffsetStart || index >= bufferOffsetEnd) {
            read(max(0L, index - bufferSize + 1))
        }
        return char()
    }

    private fun indexEntryAt(newIndex: Long): IndexEntry {
        setOffset(newIndex)
        return IndexEntryParser(nextEntry()).parse()
    }

    private fun nearestEntry(newIndex: Long): Long {
        setOffset(newIndex)
        skipWithPredicate(Char::isWhitespace)
        skipWithPredicate { ch: Char -> ch != 'I' }
        return index
    }

    private fun skipWithPredicate(predicate: Predicate<Char>) {
        while (index >= 0 && predicate.test(charLeft())) {
            index--
        }
    }

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
