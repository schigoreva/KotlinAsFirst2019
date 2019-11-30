@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson5.task1

import kotlin.math.max
import kotlin.math.min

/**
 * Пример
 *
 * Для заданного списка покупок `shoppingList` посчитать его общую стоимость
 * на основе цен из `costs`. В случае неизвестной цены считать, что товар
 * игнорируется.
 */
fun shoppingListCost(
    shoppingList: List<String>,
    costs: Map<String, Double>
): Double {
    var totalCost = 0.0

    for (item in shoppingList) {
        val itemCost = costs[item]
        if (itemCost != null) {
            totalCost += itemCost
        }
    }

    return totalCost
}

/**
 * Пример
 *
 * Для набора "имя"-"номер телефона" `phoneBook` оставить только такие пары,
 * для которых телефон начинается с заданного кода страны `countryCode`
 */
fun filterByCountryCode(
    phoneBook: MutableMap<String, String>,
    countryCode: String
) {
    val namesToRemove = mutableListOf<String>()

    for ((name, phone) in phoneBook) {
        if (!phone.startsWith(countryCode)) {
            namesToRemove.add(name)
        }
    }

    for (name in namesToRemove) {
        phoneBook.remove(name)
    }
}

/**
 * Пример
 *
 * Для заданного текста `text` убрать заданные слова-паразиты `fillerWords`
 * и вернуть отфильтрованный текст
 */
fun removeFillerWords(
    text: List<String>,
    vararg fillerWords: String
): List<String> {
    val fillerWordSet = setOf(*fillerWords)

    val res = mutableListOf<String>()
    for (word in text) {
        if (word !in fillerWordSet) {
            res += word
        }
    }
    return res
}

/**
 * Пример
 *
 * Для заданного текста `text` построить множество встречающихся в нем слов
 */
fun buildWordSet(text: List<String>): MutableSet<String> {
    val res = mutableSetOf<String>()
    for (word in text) res.add(word)
    return res
}


/**
 * Простая
 *
 * По заданному ассоциативному массиву "студент"-"оценка за экзамен" построить
 * обратный массив "оценка за экзамен"-"список студентов с этой оценкой".
 *
 * Например:
 *   buildGrades(mapOf("Марат" to 3, "Семён" to 5, "Михаил" to 5))
 *     -> mapOf(5 to listOf("Семён", "Михаил"), 3 to listOf("Марат"))
 */
fun buildGrades(grades: Map<String, Int>): Map<Int, List<String>> {
    val ans = mutableMapOf<Int, MutableList<String>>()
    for ((first, second) in grades) {
        if (ans[second] == null) ans += Pair(second, mutableListOf(first))
        else ans[second]!! += first
    }
    return ans
}

/**
 * Простая
 *
 * Определить, входит ли ассоциативный массив a в ассоциативный массив b;
 * это выполняется, если все ключи из a содержатся в b с такими же значениями.
 *
 * Например:
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "z", "b" to "sweet")) -> true
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "zee", "b" to "sweet")) -> false
 */
fun containsIn(a: Map<String, String>, b: Map<String, String>): Boolean {
    for ((first, second) in a) {
        if (second != b[first]) return false
    }
    return true
}

/**
 * Простая
 *
 * Удалить из изменяемого ассоциативного массива все записи,
 * которые встречаются в заданном ассоциативном массиве.
 * Записи считать одинаковыми, если и ключи, и значения совпадают.
 *
 * ВАЖНО: необходимо изменить переданный в качестве аргумента
 *        изменяемый ассоциативный массив
 *
 * Например:
 *   subtractOf(a = mutableMapOf("a" to "z"), mapOf("a" to "z"))
 *     -> a changes to mutableMapOf() aka becomes empty
 */
fun subtractOf(a: MutableMap<String, String>, b: Map<String, String>): MutableMap<String, String> {
    for ((first, second) in b) {
        if (a[first] == second) a.remove(first)
    }
    return a
}

/**
 * Простая
 *
 * Для двух списков людей найти людей, встречающихся в обоих списках.
 * В выходном списке не должно быть повторяюихся элементов,
 * т. е. whoAreInBoth(listOf("Марат", "Семён, "Марат"), listOf("Марат", "Марат")) == listOf("Марат")
 */
fun whoAreInBoth(a: List<String>, b: List<String>): List<String> = a.intersect(b).toList()

/**
 * Средняя
 *
 * Объединить два ассоциативных массива `mapA` и `mapB` с парами
 * "имя"-"номер телефона" в итоговый ассоциативный массив, склеивая
 * значения для повторяющихся ключей через запятую.
 * В случае повторяющихся *ключей* значение из mapA должно быть
 * перед значением из mapB.
 *
 * Повторяющиеся *значения* следует добавлять только один раз.
 *
 * Например:
 *   mergePhoneBooks(
 *     mapOf("Emergency" to "112", "Police" to "02"),
 *     mapOf("Emergency" to "911", "Police" to "02")
 *   ) -> mapOf("Emergency" to "112, 911", "Police" to "02")
 */
fun mergePhoneBooks(mapA: Map<String, String>, mapB: Map<String, String>): Map<String, String> {
    val mapA1 = mapA.toMutableMap()
    for ((name, phone) in mapB) {
        when {
            mapA1[name] == null -> mapA1 += Pair(name, phone)
            mapA1[name] != phone -> mapA1[name] += (", $phone")
        }
    }
    return mapA1
}

/**
 * Средняя
 *
 * Для заданного списка пар "акция"-"стоимость" вернуть ассоциативный массив,
 * содержащий для каждой акции ее усредненную стоимость.
 *
 * Например:
 *   averageStockPrice(listOf("MSFT" to 100.0, "MSFT" to 200.0, "NFLX" to 40.0))
 *     -> mapOf("MSFT" to 150.0, "NFLX" to 40.0)
 */
fun averageStockPrice(stockPrices: List<Pair<String, Double>>): Map<String, Double> {
    val ans = mutableMapOf<String, Double>()
    val cnt = mutableMapOf<String, Int>()
    for ((item, cost) in stockPrices) {
        if (ans[item] == null) {
            ans += Pair(item, 0.0)
            cnt += Pair(item, 0)
        }
        ans[item] = ans[item]!! + cost
        cnt[item] = cnt[item]!! + 1
    }
    for ((item, cost) in cnt) ans[item] = ans[item]!! / cost
    return ans
}

/**
 * Средняя
 *
 * Входными данными является ассоциативный массив
 * "название товара"-"пара (тип товара, цена товара)"
 * и тип интересующего нас товара.
 * Необходимо вернуть название товара заданного типа с минимальной стоимостью
 * или null в случае, если товаров такого типа нет.
 *
 * Например:
 *   findCheapestStuff(
 *     mapOf("Мария" to ("печенье" to 20.0), "Орео" to ("печенье" to 100.0)),
 *     "печенье"
 *   ) -> "Мария"
 */
fun findCheapestStuff(stuff: Map<String, Pair<String, Double>>, kind: String): String? {
    var c = -1.0
    var ans: String? = null
    for ((name, sort) in stuff) {
        if (sort.first == kind && (c == -1.0 || c > sort.second)) {
            c = sort.second
            ans = name
        }
    }
    return ans
}

/**
 * Средняя
 *
 * Для заданного набора символов определить, можно ли составить из него
 * указанное слово (регистр символов игнорируется)
 *
 * Например:
 *   canBuildFrom(listOf('a', 'b', 'o'), "baobab") -> true
 */
fun canBuildFrom(chars: List<Char>, word: String): Boolean {
    val f = chars.map { it.toLowerCase() }.intersect(word.toLowerCase().toSet())
    return f == word.toLowerCase().toSet()
}

/**
 * Средняя
 *
 * Найти в заданном списке повторяющиеся элементы и вернуть
 * ассоциативный массив с информацией о числе повторений
 * для каждого повторяющегося элемента.
 * Если элемент встречается только один раз, включать его в результат
 * не следует.
 *
 * Например:
 *   extractRepeats(listOf("a", "b", "a")) -> mapOf("a" to 2)
 */
fun extractRepeats(list: List<String>): Map<String, Int> {
    val cnt = mutableMapOf<String, Int>()
    for (s in list) {
        if (cnt[s] == null) {
            cnt += Pair(s, 0)
        }
        cnt[s] = cnt[s]!! + 1
    }
    return cnt.filter { it.value != 1 }
}

/**
 * Средняя
 *
 * Для заданного списка слов определить, содержит ли он анаграммы
 * (два слова являются анаграммами, если одно можно составить из второго)
 *
 * Например:
 *   hasAnagrams(listOf("тор", "свет", "рот")) -> true
 */
fun hasAnagrams(words: List<String>): Boolean {
    val cnt = mutableListOf<Map<Char, Int>>()
    for (word in words) {
        val st = mutableMapOf<Char, Int>()
        for (c in word) {
            st[c] = st.getOrDefault(c, 0) + 1
        }
        cnt += st
    }
    for (i in 0 until cnt.size - 1) {
        for (j in i + 1 until cnt.size) {
            var fl = true
            var fl1 = true
            for ((key, value) in cnt[i]) {
                if (cnt[j].getOrDefault(key, 0) < value) {
                    fl = false
                    break
                }
            }
            for ((key, value) in cnt[j]) {
                if (cnt[i].getOrDefault(key, 0) < value) {
                    fl1 = false
                    break
                }
            }
            if (fl || fl1) return true
        }
    }
    return false
}

/**
 * Сложная
 *
 * Для заданного ассоциативного массива знакомых через одно рукопожатие `friends`
 * необходимо построить его максимальное расширение по рукопожатиям, то есть,
 * для каждого человека найти всех людей, с которыми он знаком через любое
 * количество рукопожатий.
 * Считать, что все имена людей являются уникальными, а также что рукопожатия
 * являются направленными, то есть, если Марат знает Свету, то это не означает,
 * что Света знает Марата.
 *
 * Например:
 *   propagateHandshakes(
 *     mapOf(
 *       "Marat" to setOf("Mikhail", "Sveta"),
 *       "Sveta" to setOf("Marat"),
 *       "Mikhail" to setOf("Sveta")
 *     )
 *   ) -> mapOf(
 *          "Marat" to setOf("Mikhail", "Sveta"),
 *          "Sveta" to setOf("Marat", "Mikhail"),
 *          "Mikhail" to setOf("Sveta", "Marat")
 *        )
 */

fun propagateHandshakes(friends: Map<String, Set<String>>): Map<String, Set<String>> {
    val ans = mutableMapOf<String, MutableSet<String>>()
    val tp = mutableSetOf<String>()
    for ((first, second) in friends) {
        tp += first
        tp += second
    }
    for (first in tp) {
        val prt = mutableSetOf(first)
        val lst = mutableListOf(first)
        while (lst.size > 0) {
            if (friends[lst[0]] == null) {
                lst -= lst[0]
                continue
            }
            val st = friends.getOrDefault(lst[0], setOf()).toMutableSet()
            st -= st.intersect(prt)
            prt += st
            lst += st
            lst -= lst[0]
        }
        prt -= first
        ans += Pair(first, prt)
    }
    return ans
}


/**
 * Сложная
 *
 * Для заданного списка неотрицательных чисел и числа определить,
 * есть ли в списке пара чисел таких, что их сумма равна заданному числу.
 * Если да, верните их индексы в виде Pair<Int, Int>;
 * если нет, верните пару Pair(-1, -1).
 *
 * Индексы в результате должны следовать в порядке (меньший, больший).
 *
 * Постарайтесь сделать ваше решение как можно более эффективным,
 * используя то, что вы узнали в данном уроке.
 *
 * Например:
 *   findSumOfTwo(listOf(1, 2, 3), 4) -> Pair(0, 2)
 *   findSumOfTwo(listOf(1, 2, 3), 6) -> Pair(-1, -1)
 */
fun findSumOfTwo(list: List<Int>, number: Int): Pair<Int, Int> {
    val mp = mutableMapOf<Int, MutableList<Int>>()
    for (i in 0 until list.size) {
        if (mp[list[i]] == null) mp += Pair(list[i], mutableListOf(i))
        else mp[list[i]]!! += i
    }
    for ((first, second) in mp) {
        if (first == number - first) {
            if (second.size > 1) return Pair(second[0], second[1])
        } else if (mp[number - first] != null) {
            val a = second[0]
            val b = mp[number - first]!![0]
            return Pair(min(a, b), max(a, b))
        }
    }
    return Pair(-1, -1)
}

/**
 * Очень сложная
 *
 * Входными данными является ассоциативный массив
 * "название сокровища"-"пара (вес сокровища, цена сокровища)"
 * и вместимость вашего рюкзака.
 * Необходимо вернуть множество сокровищ с максимальной суммарной стоимостью,
 * которые вы можете унести в рюкзаке.
 *
 * Перед решением этой задачи лучше прочитать статью Википедии "Динамическое программирование".
 *
 * Например:
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     850
 *   ) -> setOf("Кубок")
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     450
 *   ) -> emptySet()
 */
fun bagPacking(treasures: Map<String, Pair<Int, Int>>, capacity: Int): Set<String> {
    val dp = Array(treasures.size) { Array(capacity + 1) { 0 } }
    var cur = 0
    val names = mutableListOf<String>()
    for ((key, value) in treasures) {
        names += key
        if (cur == 0) {
            if (value.first <= capacity) {
                dp[cur][value.first] = value.second
            }
        } else {
            for (j in 0 until capacity + 1) {
                if (j >= value.first) {
                    dp[cur][j] = max(dp[cur - 1][j - value.first] + value.second, dp[cur - 1][j])
                } else {
                    dp[cur][j] = dp[cur - 1][j]
                }
            }
        }
        cur++
    }
    cur--
    val ans = mutableSetOf<String>()
    names.reverse()
    var w = 0
    for (i in 0 until capacity + 1) {
        if (dp[cur][i] > dp[cur][w]) {
            w = i
        }
    }
    for (i in names) {
        if (cur == 0) {
            if (w != 0) {
                ans.add(i)
            }
        } else {
            if (dp[cur][w] != dp[cur - 1][w]) {
                ans.add(i)
                w -= treasures.getOrDefault(i, Pair(0, 0)).second
            }
        }
        cur--
    }
    return ans
}
