package parsers

import BedEntry


/**
 * Parses string into [BedEntry]
 */
class BedEntryParser(
    sourceString: String
) : BaseEntryParser(sourceString) {


    /**
     * Skips whitespaces while they appear
     */
    private fun skipWhitespaces() {
        while (test(' ') || test('\t')) {
        }
    }


    /**
     * Parses [BedEntry.other]
     */
    private fun parseList(): List<Any> {
        val res = arrayListOf<Any>()
        skipWhitespaces()
        while (char != Constants.EOF) {
            res.add(parseString())
            skipWhitespaces()
        }
        return res
    }


    /**
     * Parses whitespace-separated string into [BedEntry]
     */
    fun parse(): BedEntry {
        val chromosome = parseString()
        skipWhitespaces()
        val start = parseString().toInt()
        skipWhitespaces()
        val end = parseString().toInt()
        skipWhitespaces()
        return BedEntry(chromosome, start, end, parseList())
    }
}