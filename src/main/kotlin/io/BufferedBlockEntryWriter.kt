package io

import java.nio.file.Path


/**
 * Write file in 'blocks', i.e. content of file is divided in lines (blocks) with the length of [blockSize]
 * and trailed with whitespaces and line separator to fit exactly [blockSize]
 */
class BufferedBlockEntryWriter(
    path: Path,
    private val blockSize: Long,
    private val bufferSize: Int = Constants.B
) {
    private val writer = path.toFile().bufferedWriter(Charsets.UTF_8)
    private var buffer = arrayListOf<String>()
    private var bufferLength: Int = 0
    private var length: Long = 0


    /**
     * Writes [buffer] to a file if it is full
     */
    private fun flush() {
        val builder = StringBuilder()
        for (string in buffer) {
            builder.append("$string ")
        }
        writer.write(builder.toString())
        bufferLength = 0
    }


    /**
     * Writes trailing whitespaces and line separator
     */
    private fun end() {
        val builder = StringBuilder()
        for (count in length until blockSize - Constants.LINE_SEPARATOR.length) {
            builder.append(' ')
        }
        writer.write(builder.append(Constants.LINE_SEPARATOR).toString())
    }


    /**
     * Write 'block' in file using [writer]
     */
    fun completeBlock() {
        flush()
        end()
        buffer.clear()
        length = 0
    }


    /**
     * Adds [value] in buffer
     */
    fun addString(value: String) {
        if (value.length + bufferLength >= bufferSize) {
            flush()
        }
        if (value.length + length + 1 + Constants.LINE_SEPARATOR.length > blockSize) {
            completeBlock()
        }
        buffer.add(value)
        length += value.length + 1
    }


    /**
     * Write buffer if it is not empty and then closes [writer]
     */
    fun close() {
        if (length > 0L) {
            completeBlock()
        }
        writer.close()
    }
}