import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import tivpomodule.*

class TextAnalyzerTest {

    @Nested
    @DisplayName("letterCounter - letter counting")
    inner class LetterCounterTest {

        @Test
        fun `boundary english letters z and Z are counted`() {
            assertEquals(2, letterCounter("zZ"))
            assertEquals(4, letterCounter("aAzZ"))
        }

        @Test
        fun `boundary russian letters ya and YA are counted`() {
            assertEquals(2, letterCounter("яЯ"))
            assertEquals(4, letterCounter("аАяЯ"))
        }

        @Test
        fun `empty string returns 0 letters`() {
            assertEquals(0, letterCounter(""))
        }

        @Test
        fun `only spaces and punctuation returns 0 letters`() {
            assertEquals(0, letterCounter("   .,!?123\n\t"))
        }

        @Test
        fun `english letters`() {
            assertEquals(3, letterCounter("abc"))
            assertEquals(3, letterCounter("ABC"))
            assertEquals(6, letterCounter("aBcDeF"))
        }

        @Test
        fun `characters outside supported letter ranges are ignored`() {
            assertEquals(0, letterCounter("1234567890 !?,."))
            assertEquals(0, letterCounter("éñø"))
        }

        @Test
        fun `russian letters`() {
            assertEquals(3, letterCounter("абв"))
            assertEquals(3, letterCounter("АБВ"))
        }

        @Test
        fun `letter ё is counted`() {
            assertEquals(2, letterCounter("ёЁ"))
        }

        @Test
        fun `mixed text`() {
            assertEquals(6, letterCounter("Кот Dog"))
        }

        @Test
        fun `digits and punctuation are not counted`() {
            assertEquals(10, letterCounter("Hello, World! 123"))
        }

        @Test
        fun `only letter yo is counted correctly`() {
            assertEquals(2, letterCounter("ёЁ"))
            assertEquals(1, letterCounter("ё"))
        }
    }

    @Nested
    @DisplayName("wordCounter - word counting")
    inner class WordCounterTest {

        @Test
        fun `words starting or ending with boundary letters are counted`() {
            assertEquals(2, wordCounter("zя zЯ"))
            assertEquals(2, wordCounter("Zz Яя"))
        }

        @Test
        fun `empty string returns 0 words`() {
            assertEquals(0, wordCounter(""))
        }

        @Test
        fun `only spaces returns 0 words`() {
            assertEquals(0, wordCounter("     "))
        }

        @Test
        fun `single word`() {
            assertEquals(1, wordCounter("Привет"))
        }

        @Test
        fun `multiple words separated by spaces`() {
            assertEquals(3, wordCounter("Кот спит дома"))
        }

        @Test
        fun `words with punctuation`() {
            assertEquals(4, wordCounter("Кот, пёс и собака!"))
        }

        @Test
        fun `multiple spaces between words`() {
            assertEquals(2, wordCounter("Hello    World"))
        }

        @Test
        fun `mixed russian and english`() {
            assertEquals(4, wordCounter("Кот спит. Dog runs."))
        }

        @Test
        fun `leading and trailing spaces do not create extra words`() {
            assertEquals(2, wordCounter("   кот спит   "))
        }

        @Test
        fun `newlines and tabs separate words`() {
            assertEquals(3, wordCounter("кот\nспит\tдома"))
        }

        @Test
        fun `punctuation only returns zero words`() {
            assertEquals(0, wordCounter("... !!! ,,,"))
        }
    }

    @Nested
    @DisplayName("symbolCounter - symbol counting")
    inner class SymbolCounterTest {

        @Test
        fun `empty string returns 0`() {
            assertEquals(0, symbolCounter(""))
        }

        @Test
        fun `counts all characters including spaces`() {
            assertEquals(5, symbolCounter("a b c"))
        }

        @Test
        fun `counts punctuation marks`() {
            assertEquals(5, symbolCounter("a, b!"))
        }
    }

    @Nested
    @DisplayName("InterpretTextToWords - splitting into words")
    inner class InterpretTextToWordsTest {

        @Test
        fun `single word without trailing punctuation is not lost`() {
            assertEquals(listOf("привет"), InterpretTextToWords("привет"))
            assertEquals(listOf("кот", "спит"), InterpretTextToWords("кот спит")) // Без точки в конце!
        }

        @Test
        fun `text with only letter yo`() {
            assertEquals(listOf("ё"), InterpretTextToWords("ё"))
        }

        @Test
        fun `empty string returns empty list`() {
            assertTrue(InterpretTextToWords("").isEmpty())
        }

        @Test
        fun `single word`() {
            assertEquals(listOf("hello"), InterpretTextToWords("hello"))
        }

        @Test
        fun `multiple words separated by spaces`() {
            assertEquals(listOf("кот", "спит"), InterpretTextToWords("кот спит"))
        }

        @Test
        fun `converts to lowercase`() {
            assertEquals(listOf("hello", "world"), InterpretTextToWords("Hello WORLD"))
        }

        @Test
        fun `punctuation is ignored`() {
            assertEquals(
                listOf("кот", "спит", "пёс", "лает"),
                InterpretTextToWords("Кот спит. Пёс лает!")
            )
        }

        @Test
        fun `leading and trailing delimiters are ignored`() {
            assertEquals(
                listOf("кот", "спит"),
                InterpretTextToWords("  кот,,, спит   ")
            )
        }

        @Test
        fun `uppercase russian yo is normalized`() {
            assertEquals(
                listOf("ёж", "ёлка"),
                InterpretTextToWords("ЁЖ ЁЛКА")
            )
        }

        @Test
        fun `punctuation only returns empty list`() {
            assertTrue(InterpretTextToWords("... !!! ,,,").isEmpty())
        }

        @Test
        fun `strictly verifies list is populated`() {
            val result = InterpretTextToWords("кот спит")
            assertEquals(2, result.size, "List size must be 2 if words.add() works")
            assertEquals("кот", result[0])
            assertEquals("спит", result[1])
        }

        @Test
        fun `words with boundary letters are split correctly`() {
            assertEquals(listOf("zя", "zя"), InterpretTextToWords("zя ZЯ"))
            assertEquals(listOf("a", "z"), InterpretTextToWords("a z"))
            assertEquals(listOf("а", "я"), InterpretTextToWords("а я"))
        }

        @Test
        fun `multiple delimiters`() {
            assertEquals(
                listOf("a", "b", "c"),
                InterpretTextToWords("a,,,b   c")
            )
        }

        @Test
        @DisplayName("⚠️ BUG: two consecutive words should be split correctly")
        fun `bug - two words should be separated`() {
            val result = InterpretTextToWords("кот спит")
            assertEquals(listOf("кот", "спит"), result)
        }

        @Test
        @DisplayName("⚠️ BUG: the last word is not lost")
        fun `bug - the last word is not lost`() {
            val result = InterpretTextToWords("кот спит")
            assertEquals(2, result.size, "There should be exactly 2 words")
        }
    }

    @Nested
    @DisplayName("wordFrequencyCounter - word frequency")
    inner class WordFrequencyCounterTest {

        @Test
        fun `empty list returns empty map`() {
            assertTrue(wordFrequencyCounter(emptyList()).isEmpty())
        }

        @Test
        fun `all words are unique`() {
            val result = wordFrequencyCounter(listOf("a", "b", "c"))
            assertEquals(mapOf("a" to 1, "b" to 1, "c" to 1), result)
        }

        @Test
        fun `repeating words`() {
            val result = wordFrequencyCounter(listOf("кот", "пёс", "кот"))
            assertEquals(2, result["кот"])
            assertEquals(1, result["пёс"])
        }

        @Test
        fun `all words are the same`() {
            val result = wordFrequencyCounter(listOf("a", "a", "a", "a"))
            assertEquals(1, result.size)
            assertEquals(4, result["a"])
        }

        @Test
        fun `interleaved repeating words force both if and else branches`() {
            val result = wordFrequencyCounter(listOf("a", "b", "a", "c", "a"))
            assertEquals(3, result.size)
            assertEquals(3, result["a"])
            assertEquals(1, result["b"])
            assertEquals(1, result["c"])
        }

        @Test
        fun `strictly verifies map is populated`() {
            val result = wordFrequencyCounter(listOf("a", "b", "a"))
            assertEquals(2, result.size, "Map size must be 2 if result[i] = ... works")
            assertEquals(2, result["a"])
            assertEquals(1, result["b"])
        }

        @Test
        fun `frequency is incremented independently for each word`() {
            val result = wordFrequencyCounter(
                listOf("a", "b", "a", "b", "a", "c")
            )

            assertEquals(
                mapOf("a" to 3, "b" to 2, "c" to 1),
                result
            )
        }

    }

    @Nested
    @DisplayName("symbolFrequencyCounter - symbol frequency")
    inner class SymbolFrequencyCounterTest {

        @Test
        fun `interleaved repeating characters force map updates`() {
            val result = symbolFrequencyCounter("a b a c a")
            assertEquals(4, result.size)
            assertEquals(3, result['a'])
            assertEquals(4, result[' '])
            assertEquals(1, result['b'])
            assertEquals(1, result['c'])
        }

        @Test
        fun `empty string returns empty map`() {
            assertTrue(symbolFrequencyCounter("").isEmpty())
        }

        @Test
        fun `all characters are unique`() {
            val result = symbolFrequencyCounter("abc")
            assertEquals(3, result.size)
            assertEquals(1, result['a'])
            assertEquals(1, result['b'])
            assertEquals(1, result['c'])
        }

        @Test
        fun `repeating characters`() {
            val result = symbolFrequencyCounter("aabb")
            assertEquals(2, result['a'])
            assertEquals(2, result['b'])
        }

        @Test
        fun `spaces are also counted`() {
            val result = symbolFrequencyCounter("a a")
            assertEquals(2, result['a'])
            assertEquals(1, result[' '])
        }

        @Test
        fun `case sensitivity matters`() {
            val result = symbolFrequencyCounter("aA")
            assertEquals(2, result.size)
            assertEquals(1, result['a'])
            assertEquals(1, result['A'])
        }

        @Test
        fun `strictly verifies symbol map is populated`() {
            val result = symbolFrequencyCounter("x y x")
            assertEquals(3, result.size, "Map size must be 3 if result[i] = ... works")
            assertEquals(2, result['x'])
            assertEquals(2, result[' '])
        }
    }
}