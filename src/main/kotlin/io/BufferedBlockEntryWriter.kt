package io

import B
import LINE_SEPARATOR
import java.nio.file.Path

class BufferedBlockEntryWriter(
    path: Path,
    private val blockSize: Long,
    private val bufferSize: Int = B
) {
    private val writer = path.toFile().bufferedWriter(Charsets.UTF_8)
    private var buffer = arrayListOf<String>()
    private var bufferLength: Int = 0
    private var length: Long = 0

    private fun flush() {
        val builder = StringBuilder()
        for (string in buffer) {
            builder.append("$string ")
        }
        writer.write(builder.toString())
        bufferLength = 0
    }

    private fun end() {
        for (count in length until blockSize - LINE_SEPARATOR.length) {
            writer.write(" ")
        }
        writer.newLine()
    }

    fun completeBlock() {
        flush()
        end()
        buffer.clear()
        length = 0
    }

    fun addString(value: String) {
        if (value.length + bufferLength >= bufferSize) {
            flush()
        }
        if (value.length + length + 1 + LINE_SEPARATOR.length > blockSize) {
            completeBlock()
        }
        buffer.add(value)
        length += value.length + 1
    }

    fun close() {
        if (length > 0L) {
            completeBlock()
        }
        writer.close()
    }
}