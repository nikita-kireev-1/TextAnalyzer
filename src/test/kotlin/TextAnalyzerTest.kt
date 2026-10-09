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
    }

    @Nested
    @DisplayName("wordCounter - word counting")
    inner class WordCounterTest {

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
    }

    @Nested
    @DisplayName("symbolFrequencyCounter - symbol frequency")
    inner class SymbolFrequencyCounterTest {

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
    }
}