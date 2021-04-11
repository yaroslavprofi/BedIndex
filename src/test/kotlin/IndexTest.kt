import org.junit.Test
import java.nio.file.Path


/**
 * Tests for [BedReader]
 */
class IndexTest {
    private val resources: Path = Path.of("src/test/resources")

    @Test
    fun testInput() {
        val tester = BaseTester()
        tester.test(
            resources.resolve("input.bed"),
            resources.resolve("out"),
            listOf(
                BedEntry("chr7", 127475864, 127132131, listOf()),
                BedEntry("chadar7", 127471196, 127473530, listOf()),
                BedEntry("asdfchr7", 12747, 999999999, listOf()),
            )
        )
    }
}