import io.BaseBufferedBlockEntryReader
import io.BufferedBlockEntryWriter
import io.BaseBufferedReader
import parsers.IndexEntryParser
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class IndexEntrySorter(
    private var path: Path
) {
    private fun deleteTemp(temp: Path) {
        Files.copy(
            temp,
            path,
            StandardCopyOption.REPLACE_EXISTING
        )
        temp.toFile().delete()
    }

    private fun createTemp(): Path {
        return Files.createTempFile(
            path.parent,
            null,
            null
        )
    }

    private fun merge(first: Long, second: Long, entryWriter: BufferedBlockEntryWriter, blockSize: Long) {
        val firstBlockReader = BaseBufferedBlockEntryReader(path, blockSize, first)
        val secondBlockReader = BaseBufferedBlockEntryReader(path, blockSize, second)
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

    private fun preSort() {
        val blockSize = 1L * B
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
            val liner = BaseBufferedBlockEntryReader(path, blockSize, index)
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

    fun sort() {
        preSort()
        var blockSize: Long = 1L * B
        while (path.toFile().length() / blockSize > 1) {
            val indicesNumber: Long = path.toFile().length() / blockSize
            val temp = createTemp()
            val writer = BufferedBlockEntryWriter(temp, blockSize * 2)
            for (index: Long in 0 until indicesNumber / 2) {
                merge(index * 2, index * 2 + 1, writer, blockSize)
            }
            if (indicesNumber % 2 != 0L) {
                val liner = BaseBufferedBlockEntryReader(path, blockSize, indicesNumber - 1)
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