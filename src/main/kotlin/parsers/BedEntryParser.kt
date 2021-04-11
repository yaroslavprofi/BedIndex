package parsers

import BedEntry

class BedEntryParser(
    source: String
) : EntryBaseParser(source) {

    private fun skipWhitespaces() {
        while (test(' ') || test('\t')) {
        }
    }

    fun parse(): BedEntry {
        val chromosome = parseString()
        skipWhitespaces()
        val start = parseString().toInt()
        skipWhitespaces()
        val end = parseString().toInt()
        skipWhitespaces()
        val other = listOf<Any>(ch + source.substring(index, source.length - 2))
        return BedEntry(chromosome, start, end, other)
    }
}