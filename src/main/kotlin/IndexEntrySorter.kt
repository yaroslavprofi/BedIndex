import io.BufferedBlockEntryReader
import io.BufferedBlockEntryWriter
import io.BaseBufferedReader
import parsers.IndexEntryParser
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption


/**
 * Performs merge sort of file that is supposed to contain only [IndexEntry.toString]
 */
class IndexEntrySorter(
    private var path: Path
) {

    /**
     *  Copies [temp] to [path] and deletes [temp]
     */
    private fun deleteTemp(temp: Path) {
        Files.copy(
            temp,
            path,
            StandardCopyOption.REPLACE_EXISTING
        )
        temp.toFile().delete()
    }


    /**
     *  Creates temp file for new sort iteration
     *  @see IndexEntrySorter.sort
     */
    private fun createTemp(): Path {
        return Files.createTempFile(
            path.parent,
            null,
            null
        )
    }


    /**
     * Merges two sorted 'blocks' in sorted one
     *
     * @see BufferedBlockEntryWriter
     */
    private fun merge(
        first: Long, second: Long,
        entryWriter: BufferedBlockEntryWriter,
        blockSize: Long
    ) {
        val firstBlockReader = BufferedBlockEntryReader(path, blockSize, first)
        val secondBlockReader = BufferedBlockEntryReader(path, blockSize, second)
        var entryFirst = firstBlockReader.nextEntry()
        var entrySecond = secondBlockReader.nextEntry()
        while (entryFirst != "" || entrySecond != "") {
            var toWrite: String
            if (entryFirst == "") {
                toWrite = entrySecond
                entrySecond = secondBlockReader.nextEntry()
            } else {
                if (entrySecond == "") {
                    toWrite = entryFirst
                    entryFirst = firstBlockReader.nextEntry()
                } else {
                    if (IndexEntry.compare(
                            IndexEntryParser(entryFirst).parse(),
                            IndexEntryParser(entrySecond).parse()
                        ) < 0
                    ) {
                        toWrite = entryFirst
                        entryFirst = firstBlockReader.nextEntry()
                    } else {
                        toWrite = entrySecond
                        entrySecond = secondBlockReader.nextEntry()
                    }
                }
            }
            entryWriter.addString(toWrite)
        }
        entryWriter.completeBlock()
        firstBlockReader.close()
        secondBlockReader.close()
    }


    /**
     * Transform [path] into 'block' file.
     * Each 'block' is is sorted with [List.sortedWith] with comparator [IndexEntry.compare]
     */
    private fun preSort() {
        val blockSize = 1L * Constants.B
        val reader = BaseBufferedReader(path)
        var temp = createTemp()
        var writer = BufferedBlockEntryWriter(temp, blockSize)
        while (reader.hasNext()) {
            writer.addString(reader.nextEntry())
        }
        reader.close()
        writer.close()
        deleteTemp(temp)

        temp = createTemp()
        writer = BufferedBlockEntryWriter(temp, blockSize)
        for (index in 0 until (path.toFile().length() / blockSize)) {
            val liner = BufferedBlockEntryReader(path, blockSize, index)
            val list = arrayListOf<IndexEntry>()
            while (liner.hasNext()) {
                list.add(IndexEntryParser(liner.nextEntry()).parse())
            }
            liner.close()
            for (line in list.sortedWith(IndexEntry)) {
                writer.addString(line.toString())
            }
            writer.completeBlock()
        }
        writer.close()
        deleteTemp(temp)
    }


    /**
     * Performs merge sort of file
     */
    fun sort() {
        preSort()
        var blockSize: Long = 1L * Constants.B
        while (path.toFile().length() / blockSize > 1) {
            val indicesNumber: Long = path.toFile().length() / blockSize
            val temp = createTemp()
            val writer = BufferedBlockEntryWriter(temp, blockSize * 2)
            for (index: Long in 0 until indicesNumber / 2) {
                merge(index * 2, index * 2 + 1, writer, blockSize)
            }
            if (indicesNumber % 2 != 0L) {
                val liner = BufferedBlockEntryReader(path, blockSize, indicesNumber - 1)
                while (liner.hasNext()) {
                    writer.addString(liner.nextEntry())
                }
                liner.close()
            }
            writer.close()
            deleteTemp(temp)
            blockSize *= 2
        }
    }
}