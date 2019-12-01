@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import lesson5.task1.canBuildFrom
import kotlin.math.*

/**
 * Точка на плоскости
 */

fun ang(angle: Double): Double {
    var a = angle
    if (angle < 0) {
        a += 2 * PI
    }
    return a % PI
}

data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double = max(center.distance(other.center) - radius - other.radius, 0.0)

    /**
     * Тривиальная
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = center.distance(p) <= radius
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Средняя
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    if (points.size < 2) throw IllegalArgumentException()
    var cn = -1.0
    var ans = Segment(Point(0.0, 0.0), Point(0.0, 0.0))
    for (i in 0 until points.size - 1) {
        for (j in i + 1 until points.size) {
            if (points[i].distance(points[j]) > cn) {
                cn = points[i].distance(points[j])
                ans = Segment(points[i], points[j])
            }
        }
    }
    return ans
}

/**
 * Простая
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle {
    val a = diameter.begin
    val b = diameter.end
    return Circle(Point(a.x + (b.x - a.x) / 2, a.y + (b.y - a.y) / 2), a.distance(b) / 2)
}

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        return if (angle * 2 == PI) {
            val x1 = (b * cos(other.angle) - other.b * cos(angle)) / sin(other.angle - angle)
            val y1 = (x1 * sin(other.angle) + other.b) / cos(other.angle)
            Point(x1, y1)
        } else {
            val x1 = (other.b * cos(angle) - b * cos(other.angle)) / sin(angle - other.angle)
            val y1 = (x1 * sin(angle) + b) / cos(angle)
            Point(x1, y1)
        }
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line {
    val angle = ang(atan2((s.end.y - s.begin.y), (s.end.x - s.begin.x)))
    val b = s.end.y * cos(angle) - s.end.x * sin(angle)
    return Line(b, angle)
}

/**
 * Средняя
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = lineBySegment(Segment(a, b))

/**
 * Сложная
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val x = (a.x + b.x) / 2
    val y = (a.y + b.y) / 2
    return lineByPoints(Point(x, y), Point(x + a.y - b.y, y + b.x - a.x))
}

/**
 * Средняя
 *
 * Задан список из n окружностей на плоскости. Найти пару наименее удалённых из них.
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> = TODO()

/**
 * Сложная
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val xA = a.x
    val xB = b.x
    val xC = c.x
    val yA = a.y
    val yB = b.y
    val yC = c.y
    if ((xA * (yB - yC) + xB * (yC - yA) + xC * (yA - yB)) == 0.0) {
        return Circle(a, 0.0)
    }
    val l = (xA * (yB - yC) + xB * (yC - yA) + xC * (yA - yB)) * 2
    val myCenter = Point(
        ((yA * (xB.pow(2) + yB.pow(2) - xC.pow(2) - yC.pow(2)) +
                yB * (xC.pow(2) + yC.pow(2) - xA.pow(2) - yA.pow(2)) +
                yC * (xA.pow(2) + yA.pow(2) - xB.pow(2) - yB.pow(2))) / -l),
        ((xA * (xB.pow(2) + yB.pow(2) - xC.pow(2) - yC.pow(2)) +
                xB * (xC.pow(2) + yC.pow(2) - xA.pow(2) - yA.pow(2)) +
                xC * (xA.pow(2) + yA.pow(2) - xB.pow(2) - yB.pow(2))) / l)
    )
    val myRadius = (a.distance(myCenter) + b.distance(myCenter) + c.distance(myCenter)) / 3
    return Circle(myCenter, myRadius)
}

/**
 * Очень сложная
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg points: Point): Circle {
    if (points.isEmpty()) {
        throw java.lang.IllegalArgumentException()
    }
    var center = points[0]
    var rad = 0.0
    for (i in points) {
        for (j in points) {
            val dist = i.distance(j)
            if (dist > rad) {
                rad = dist / 2
                center = Point((i.x + j.x) / 2, (i.y + j.y) / 2)
            }
        }
    }
    var flag = true
    for (i in points) {
        if (rad + 1e-9 < i.distance(center)) {
            flag = false
            break
        }
    }
    if (flag) {
        return Circle(center, rad)
    }
    var first = true
    for (i in points) {
        for (j in points) {
            if (i == j) continue
            for (z in points) {
                if (i == z || j == z) continue
                val c = circleByThreePoints(i, j, z)
                var fl = true
                for (t in points) {
                    if (c.radius + 1e-18 < t.distance(c.center)) {
                        fl = false
                        break
                    }
                }
                if (fl) {
                    if (c.radius < rad || first) {
                        rad = c.radius
                        center = c.center
                        first = false
                    }
                }
            }
        }
    }
    return Circle(center, rad)
}

