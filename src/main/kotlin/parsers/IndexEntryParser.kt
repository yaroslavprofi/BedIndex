package parsers

import IndexEntry

class IndexEntryParser(
    source: String
) : BaseEntryParser(source) {

    fun parse(): IndexEntry {
        expect("IndexEntry(")
        val start = parseArg("start").toInt()
        expect(", ")
        val end = parseArg("end").toInt()
        expect(", ")
        val offset = parseArg("offset").toLong()
        expect(")")
        return IndexEntry(start, end, offset)
    }
}