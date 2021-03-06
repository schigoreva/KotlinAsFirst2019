@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import java.lang.IllegalStateException

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */

fun isNull(list: List<Any?>): Boolean {
    val list2 = list.filterNotNull()
    return list.size != list2.size
}

private val monthStr = listOf(
    "января",
    "февраля",
    "марта",
    "апреля",
    "мая",
    "июня",
    "июля",
    "августа",
    "сентября",
    "октября",
    "ноября",
    "декабря"
)

fun dateStrToDigit(str: String): String {
    val date = str.split(" ")
    if (date.size != 3) {
        return ""
    }
    val day = date[0].toIntOrNull()
    val year = date[2].toIntOrNull()
    val month = monthStr.indexOf(date[1]) + 1
    if (month == 0) {
        return ""
    }
    return when {
        isNull(listOf(day, year)) -> ""
        day!! > daysInMonth(month, year!!) -> ""
        else -> String.format("%02d.%02d.%d", day, month, year)
    }
}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    val date = digital.split(".")
    if (date.size != 3) {
        return ""
    }
    val day = date[0].toIntOrNull()
    val year = date[2].toIntOrNull()
    return when {
        isNull(listOf(day, year, date[1].toIntOrNull())) -> ""
        day!! > daysInMonth(date[1].toInt(), year!!) -> ""
        else -> String.format("%d %s %d", day, monthStr[date[1].toInt() - 1], year)
    }
}

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String): String {
    val phoneFilter = phone.filter { it != ' ' }
    if (!(Regex("""[+\d\-()]+""")).matches(phoneFilter) ||
        (Regex("""\([^\d -]""")).containsMatchIn(phoneFilter) ||
        phone == "+"
    ) return ""
    return phoneFilter.filter { it !in setOf('(', ')', '-') }
}

/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (!jumps.matches(Regex("""(\d+[\- %]*)+"""))) {
        return -1
    }
    val jumps1 = jumps.split(" ").filter { it.toIntOrNull() != null }
    val jumps2 = jumps1.map { it.toInt() }
    return if (jumps2.isEmpty()) {
        -1
    } else {
        jumps2.max()!!
    }
}

/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    if (!jumps.matches(Regex("""(\d+ [%+-]+ ?)+"""))) {
        return -1
    }
    val jumps1 = jumps.split(" ")
    var ans = -1
    for (jump in 1 until jumps1.size step 2) {
        if (jumps1[jump].any { it == '+' }) {
            ans = maxOf(ans, jumps1[jump - 1].toInt())
        }
    }
    return ans
}

/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    try {
        var ans = 0
        var fl = false
        var plm = true
        var i = -1
        while (i + 1 < expression.length) {
            i++
            if (expression[i] == ' ') continue
            if (!fl && !(expression[i] == '+' || expression[i] == '-')) {
                var ind = i
                while (ind < expression.length &&
                    !(expression[ind] == '+' || expression[ind] == '-' || expression[ind] == ' ')
                ) {
                    ind++
                }
                val ar = expression.substring(i, ind).toInt()
                ans += if (plm) ar else -ar
                fl = !fl
                i = ind - 1
            } else if (fl && (expression[i] == '+' || expression[i] == '-')) {
                plm = expression[i] == '+'
                fl = !fl
            } else throw IllegalArgumentException()
        }

        if (fl) return ans
        else throw IllegalArgumentException()
    } catch (e: Exception) {
        throw IllegalArgumentException()
    }
}

/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    var ans = 0
    val lst = str.toLowerCase().split(' ')
    for (i in 0 until lst.size - 1) {
        if (lst[i] == lst[i + 1]) return ans
        ans += lst[i].length + 1
    }
    return -1
}

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше либо равны нуля.
 */
fun mostExpensive(description: String): String {
    try {
        var ans = ""
        var cnt = -1.0
        val lst = description.split(";").toMutableList()
        lst[0] = " " + lst[0]
        for (i in 0 until lst.size) {
            val ls = lst[i].split(" ")
            if (ls.size != 3) return ""
            if (cnt < ls[2].toDouble()) {
                cnt = ls[2].toDouble()
                ans = ls[1]
            }
        }
        return ans
    } catch (e: Exception) {
        return ""
    }
}

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    if (roman.isEmpty()) {
        return -1
    }
    if (!roman.matches(Regex("""M*(CM|DC{0,3}|CD|C{0,3})?(XC|LX{0,3}|XL|X{0,3})?(IX|VI{0,3}|IV|I{0,3})?"""))) {
        return -1
    }
    val dec = mapOf(
        'M' to 1000, 'D' to 500, 'C' to 100,
        'L' to 50, 'X' to 10, 'V' to 5, 'I' to 1
    )
    var res = 0
    for (i in 0 until roman.length - 1) {
        if (dec.getOrDefault(roman[i], 0) < dec.getOrDefault(roman[i + 1], 0)) {
            res -= dec.getOrDefault(roman[i], 0)
        } else {
            res += dec.getOrDefault(roman[i], 0)
        }
    }
    if (!dec.containsKey(roman[roman.length - 1])) {
        return -1;
    }
    res += dec.getOrDefault(roman[roman.length - 1], 0)
    return res
}

/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    val mem = mutableListOf<Int>()
    for (i in 0 until cells) {
        mem.add(0)
    }
    mem.fill(0)
    var bal = 0
    for (i in commands) {
        if (i != '+' && i != '-' && i != '>' && i != '<' && i != '[' && i != ']' && i != ' ') {
            throw java.lang.IllegalArgumentException()
        }
        if (i == '[') {
            bal++
        } else if (i == ']') {
            bal--
        }
        if (bal < 0) {
            throw java.lang.IllegalArgumentException()
        }
    }
    if (bal != 0) {
        throw java.lang.IllegalArgumentException()
    }
    var pos = cells / 2
    var cnt = 0
    var i = 0
    while (i < commands.length && cnt < limit) {
        cnt++
        if (commands[i] == '+') {
            mem[pos]++
        } else if (commands[i] == '-') {
            mem[pos]--
        } else if (commands[i] == '>') {
            pos++
        } else if (commands[i] == '<') {
            pos--
        } else if (commands[i] == '[') {
            if (mem[pos] == 0) {
                var balance = 1
                while (balance != 0) {
                    i++
                    if (commands[i] == '[') balance++
                    else if (commands[i] == ']') balance--
                }
            }
        } else if (commands[i] == ']') {
            if (mem[pos] != 0) {
                var balance = -1
                while (balance != 0) {
                    i--
                    if (commands[i] == '[') balance++
                    else if (commands[i] == ']') balance--
                }
            }
        }
        i++
        if (pos < 0 || pos >= cells) {
            throw IllegalStateException()
        }
    }
    return mem
}
