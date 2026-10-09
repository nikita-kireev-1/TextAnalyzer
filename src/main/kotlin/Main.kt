import model.TextHolder
import tivpomodule.*

fun main() {
    var textHolder: TextHolder? = null

    while (true) {
        println("\n=== Анализатор текста ===")
        val textPreview = if (textHolder == null) "[не введен]" else "\"${textHolder.text.take(40)}...\""
        println("Текущий текст: $textPreview")

        println("1. Ввести новый текст")
        println("2. Подсчитать количество букв")
        println("3. Подсчитать количество слов")
        println("4. Подсчитать общее количество символов")
        println("5. Вывести список всех слов")
        println("6. Показать частоту слов")
        println("7. Показать частоту символов")
        println("0. Выход")
        print("\nВыберите пункт меню: ")

        val choice = readlnOrNull()?.trim()

        if (choice == "0") {
            println("Завершение работы программы. До свидания!")
            break
        }

        if (choice != "1" && textHolder == null) {
            println("⚠️ Ошибка: Сначала введите текст (пункт 1)!")
            continue
        }

        when (choice) {
            "1" -> {
                println("Введите текст (нажмите Enter, когда закончите):")
                val newText = readlnOrNull() ?: ""
                val words = InterpretTextToWords(newText)
                textHolder = TextHolder(
                    text = newText,
                    wordCounter = wordCounter(newText),
                    symbolCounter = symbolCounter(newText),
                    letterCounter = letterCounter(newText),
                    wordFrequency = wordFrequencyCounter(words),
                    symbolFrequency = symbolFrequencyCounter(newText),
                    wordInterpretation = words
                )
                println("✅ Текст успешно установлен и проанализирован!")
            }
            "2" -> {
                println("Количество букв: ${textHolder!!.letterCounter}")
            }
            "3" -> {
                println("Количество слов: ${textHolder!!.wordCounter}")
            }
            "4" -> {
                println("Общее количество символов: ${textHolder!!.symbolCounter}")
            }
            "5" -> {
                val words = textHolder!!.wordInterpretation
                println("Список слов (всего ${words.size} шт.):")
                println(words.joinToString(", ") { "'$it'" })
            }
            "6" -> {
                val freq = textHolder!!.wordFrequency
                println("Частота слов:")
                if (freq.isEmpty()) {
                    println("Слов в тексте не найдено.")
                } else {
                    val sortedFreq = freq.entries.sortedByDescending { it.value }
                    for ((word, count) in sortedFreq) {
                        println("  '$word': $count")
                    }
                }
            }
            "7" -> {
                val freq = textHolder!!.symbolFrequency
                println("Частота символов:")
                val sortedFreq = freq.entries.sortedByDescending { it.value }
                for ((char, count) in sortedFreq) {
                    val displayChar = when (char) {
                        ' ' -> "пробел"
                        '\n' -> "перенос строки"
                        '\t' -> "табуляция"
                        else -> char.toString()
                    }
                    println("  '$displayChar': $count")
                }
            }
            else -> {
                println("❌ Неверный ввод. Пожалуйста, выберите пункт от 0 до 7.")
            }
        }
    }
}