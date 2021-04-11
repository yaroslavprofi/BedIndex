package parsers

import IndexEntry


/**
 * Parses string into [IndexEntry]
 */
class IndexEntryParser(
    source: String
) : BaseEntryParser(source) {


    /**
     * Returns string that contains only digits
     */
    private fun parseNumber(): String {
        val res = StringBuilder()
        while (char != Constants.EOF && char.isDigit()) {
            res.append(char)
            nextChar()
        }
        return res.toString()
    }


    /**
     * Expects `<arg name>=` for parameter of entry string representation
     */
    private fun parseCommonArg(arg: String) {
        expect(arg)
        expect('=')
    }


    /**
     * Parses [Int] argument
     */
    private fun parseIntArg(arg: String): Int {
        parseCommonArg(arg)
        return parseNumber().toInt()
    }


    /**
     * Parses [Long] argument
     */
    private fun parseLongArg(): Long {
        parseCommonArg("offset")
        return parseNumber().toLong()
    }


    /**
     * Parses string that expected to be [IndexEntry.toString] into [IndexEntry]
     */
    fun parse(): IndexEntry {
        expect("IndexEntry(")
        val start = parseIntArg("start")
        expect(", ")
        val end = parseIntArg("end")
        expect(", ")
        val offset = parseLongArg()
        expect(')')
        return IndexEntry(start, end, offset)
    }
}