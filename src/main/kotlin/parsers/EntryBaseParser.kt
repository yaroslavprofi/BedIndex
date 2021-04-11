package parsers

import EOF

open class EntryBaseParser(
    protected val source: String
) {
    protected var index = 1
    protected var ch: Char = source[0]

    protected fun parseString(): String {
        val res = StringBuilder()
        while (ch.isLetterOrDigit()) {
            res.append(ch)
            nextChar()
        }
        return res.toString()
    }

    protected fun parseCommonArg(arg: String) {
        expect(arg)
        expect('=')
    }

    protected fun nextChar() {
        ch = if (index < source.length) source[index++] else EOF
    }

    protected fun expect(c: Char) {
        if (ch != c) {
            throw error("Expected '$c', found '${ch}'")
        }
        nextChar()
    }

    protected fun expect(value: String) {
        for (c in value.toCharArray()) {
            expect(c)
        }
    }

    protected fun test(expected: Char): Boolean {
        if (ch == expected) {
            nextChar()
            return true
        }
        return false
    }

    protected fun parseArg(arg: String): String {
        parseCommonArg(arg)
        return parseString()
    }
}