package io

import B
import EOF
import java.io.RandomAccessFile
import java.nio.file.Path

open class BaseBufferedReader(
    blockPath: Path,
    bufferSize: Int = B
) {
    private val buffer = ByteArray(bufferSize)

    protected var bufferOffsetStart: Long = 0
    protected var bufferOffsetEnd: Long = 0
    protected val file = RandomAccessFile(blockPath.toFile(), "r")
    protected var fileEnd: Long = file.length() - 1
    protected var fileStart: Long = 0
    protected var index: Long = 0

    private fun skipWhiteSpaces(): Long {
        while (char() != EOF && char().isWhitespace()) {
            index++
        }
        return index
    }

    protected fun read(newBufferOffset: Long) {
        bufferOffsetStart = newBufferOffset
        file.seek(bufferOffsetStart)
        bufferOffsetEnd = file.read(buffer) + bufferOffsetStart
    }

    protected fun char(): Char {
        if (index > fileEnd) {
            return EOF
        }
        if (index < bufferOffsetStart || index >= bufferOffsetEnd) {
            read(index)
        }
        return buffer[(index - bufferOffsetStart).toInt()].toChar()
    }

    fun nextEntry(): String {
        skipWhiteSpaces()
        val acc = StringBuilder()
        while (char() != EOF) {
            acc.append(char())
            if (char() == ')') {
                index++
                break
            }
            index++
        }
        return acc.toString()
    }

    fun hasNext(): Boolean {
        skipWhiteSpaces()
        return char() != EOF
    }

    fun setOffset(offset: Long) {
        index = offset
    }

    fun next(): String {
        skipWhiteSpaces()
        val acc = StringBuilder()
        while (char() != EOF && !char().isWhitespace()) {
            acc.append(char())
            index++
        }
        return acc.toString()
    }

    fun close() {
        file.close()
    }
}