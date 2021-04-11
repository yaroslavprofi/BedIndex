/**
 * Program constants
 * @property B size of buffer for `Writer` and `Readers`
 * @property EOF symbol of end of file
 * @property LINE_SEPARATOR line separator symbol in that system
 */
class Constants {
    companion object {
        const val B: Int = 200
        const val EOF: Char = '\u0000'

        @JvmField
        val LINE_SEPARATOR: String = System.getProperty("line.separator")
    }
}