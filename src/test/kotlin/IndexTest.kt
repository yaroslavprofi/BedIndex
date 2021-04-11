import org.junit.Test
import java.nio.file.Path

class IndexTest {
    @Test
    fun testInput() {
        val tester = BaseTester()
        tester.test(
            Path.of("C:\\Users\\yaros\\Desktop\\jb_stazhka_2021\\JBR_Genome_Browser\\src\\test\\resources\\input.bed"),
            Path.of("C:\\Users\\yaros\\Desktop\\jb_stazhka_2021\\JBR_Genome_Browser\\src\\test\\resources\\out"),
            listOf(
                BedEntry("chr7", 127475864, 127132131, listOf())
            )
        )
    }
}