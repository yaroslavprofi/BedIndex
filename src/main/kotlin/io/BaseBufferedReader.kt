package io

import java.io.RandomAccessFile
import java.nio.file.Path
import IndexEntry
import BedEntry

/**
 * Class for getting string or string representation of [BedEntry] or [IndexEntry]
 */
open class BaseBufferedReader(
    pathToFile: Path,
    bufferSize: Int = Constants.B
) {
    private val buffer = ByteArray(bufferSize)

    protected var bufferOffsetStart: Long = 0
    protected var bufferOffsetEnd: Long = 0
    protected val file = RandomAccessFile(pathToFile.toFile(), "r")
    protected var fileEnd: Long = file.length() - 1
    protected var fileStart: Long = 0
    protected var index: Long = 0


    /**
     * Skip whitespaces until [char] is whitespace or the end of file reached
     */
    private fun skipWhiteSpaces(): Long {
        while (char() != Constants.EOF && char().isWhitespace()) {
            index++
        }
        return index
    }


    /**
     * Read bytes from file into [buffer] and updates [bufferOffsetStart] and [bufferOffsetEnd]
     */
    protected fun read(newBufferOffset: Long) {
        bufferOffsetStart = newBufferOffset
        file.seek(bufferOffsetStart)
        bufferOffsetEnd = file.read(buffer) + bufferOffsetStart
    }


    /**
     * Returns char at position [index] in [file] or EOF if the end of file reached
     */
    protected fun char(): Char {
        if (index > fileEnd) {
            return Constants.EOF
        }
        if (index < bufferOffsetStart || index >= bufferOffsetEnd) {
            read(index)
        }
        return buffer[(index - bufferOffsetStart).toInt()].toChar()
    }


    /**
     * Returns nearest to [index] string representation of [BedEntry] or [IndexEntry]
     */
    fun nextEntry(): String {
        skipWhiteSpaces()
        val acc = StringBuilder()
        while (char() != Constants.EOF) {
            acc.append(char())
            if (char() == ')') {
                index++
                break
            }
            index++
        }
        return acc.toString()
    }


    /**
     * Returns `true` if non-whitespace symbol could be read, `false` otherwise
     */
    fun hasNext(): Boolean {
        skipWhiteSpaces()
        return char() != Constants.EOF
    }


    /**
     * Sets [index]
     */
    fun setOffset(offset: Long) {
        index = offset
    }


    /**
     * Returns nearest to [index] string
     */
    fun next(): String {
        skipWhiteSpaces()
        val acc = StringBuilder()
        while (char() != Constants.EOF && !char().isWhitespace()) {
            acc.append(char())
            index++
        }
        return acc.toString()
    }


    /**
     * Closes [file]
     */
    fun close() {
        file.close()
    }
}