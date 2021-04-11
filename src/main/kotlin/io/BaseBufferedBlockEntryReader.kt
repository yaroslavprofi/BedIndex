package io

import B
import LINE_SEPARATOR
import java.nio.file.Path

class BaseBufferedBlockEntryReader(
    blockPath: Path,
    blockSize: Long,
    blockIndex: Long,
    bufferSize: Int = B
) : BaseBufferedReader(blockPath, bufferSize) {

    init {
        fileStart = blockSize * blockIndex
        index = fileStart
        fileEnd = fileStart + blockSize - 1 - LINE_SEPARATOR.length
    }
}