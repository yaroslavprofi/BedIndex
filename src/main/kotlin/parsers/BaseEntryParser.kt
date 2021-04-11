package parsers


/**
 * Base Parser for parsing whitespace-separated data into entry
 */
open class BaseEntryParser(
    private val sourceString: String
) {
    protected var index = 1
    protected var char: Char = sourceString[0]


    /**
     * Reads next char into [char]
     */
    protected fun nextChar() {
        char = if (index < sourceString.length) sourceString[index++] else Constants.EOF
    }


    /**
     * Returns string that contains only non-whitespace chars
     */
    protected fun parseString(): String {
        val res = StringBuilder()
        while (char != Constants.EOF && !char.isWhitespace()) {
            res.append(char)
            nextChar()
        }
        return res.toString()
    }


    /**
     * Throws error if [char] is not equal to expected [expected]. Reads next char otherwise
     */
    protected fun expect(expected: Char) {
        if (char != expected) {
            throw error("Expected '$expected', found '$char'")
        }
        nextChar()
    }


    /**
     * Expects string
     *
     * @see [expect]
     */
    protected fun expect(value: String) {
        for (c in value.toCharArray()) {
            expect(c)
        }
    }


    /**
     *  Returns `true` if [char] is equal to [expected] and reads next char.
     *  Does not read next and returns `false` otherwise
     */
    protected fun test(expected: Char): Boolean {
        if (char == expected) {
            nextChar()
            return true
        }
        return false
    }

}