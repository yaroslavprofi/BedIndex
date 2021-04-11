package io

import java.nio.file.Path

/**
 * Reader for file that supposed to be a 'block' file
 *
 * @see BufferedBlockEntryWriter
 */
class BufferedBlockEntryReader(
    pathToBlockFile: Path,
    blockSize: Long,
    blockIndex: Long,
    bufferSize: Int = Constants.B
) : BaseBufferedReader(pathToBlockFile, bufferSize) {

    init {
        fileStart = blockSize * blockIndex
        index = fileStart
        fileEnd = fileStart + blockSize - 1 - Constants.LINE_SEPARATOR.length
    }
}