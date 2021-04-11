import java.nio.file.Path

fun main() {
//    val reader1 = BedReaderClass()
//    reader1.createIndex(
//        Path.of("C:\\Users\\yaros\\Desktop\\jb_stazhka_2021\\JBR_Genome_Browser\\bed_files\\input.bed"),
//        Path.of("C:\\Users\\yaros\\Desktop\\jb_stazhka_2021\\JBR_Genome_Browser\\bed_files\\output"),
//    )
//    for (entry in reader1.findWithIndex(
//        reader1.loadIndex(Path.of("C:\\Users\\yaros\\Desktop\\jb_stazhka_2021\\JBR_Genome_Browser\\bed_files\\output")),
//        Path.of("C:\\Users\\yaros\\Desktop\\jb_stazhka_2021\\JBR_Genome_Browser\\bed_files\\input.bed"),
//        "chadar7",
//        127471196,
//        127473530
//    )) {
//        println(entry)
//    }
//    val writer = io.BufferedBlockEntryWriter(
//        Path.of("C:\\Users\\yaros\\Desktop\\jb_stazhka_2021\\JBR_Genome_Browser\\bed_files\\B.txt"),
//        20
//    )
//    writer.addString("123456789")
//    writer.addString("921231289")
//    writer.addString("123456")
//    writer.addString("1")
//    writer.addString("1234")
//    writer.addString("aaaaaaaaaaaaa")
//    writer.addString("12345678901234")
//    writer.close()
//    val reader = io.BufferedBlockEntryReader(
//        Path.of("C:\\Users\\yaros\\Desktop\\jb_stazhka_2021\\JBR_Genome_Browser\\bed_files\\B.txt"),
//        20,
//        1,
//        10
//    )
//    while (reader.hasNext()) {
//        println("!${reader.next()}!")
//    }
//    reader.close()
//    reader.close()
//    BedEntrySorter(
//        Path.of("C:\\Users\\yaros\\Desktop\\jb_stazhka_2021\\JBR_Genome_Browser\\bed_files\\output\\chadar7.index")
//    ).sort()
//    val searcher =
//        BufferedBinarySearcher(Path.of("C:\\Users\\yaros\\Desktop\\jb_stazhka_2021\\JBR_Genome_Browser\\bed_files\\output\\chadar7.index"))
//    searcher.find(8)
    println(arrayListOf(1, 2, 3) == arrayListOf(1, 2, 3))
}