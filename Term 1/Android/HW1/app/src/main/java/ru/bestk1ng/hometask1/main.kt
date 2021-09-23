package ru.bestk1ng.hometask1

import kotlin.math.sqrt

fun main() {
    task1()
    task2()
}

// Задание 1.
// Напишите программу, которая запрашивает число N, далее последовательность из N чисел,
// и выводит сумму всех элементов.

fun task1() {
    println("Task 1")

    print("Enter N: ")
    val n = readLine()!!.toInt()

    print("Enter sequence: ")
    val array = readLine()!!.split(' ').map(String::toInt)
    if (n != array.count()) {
        println("Invalid sequence count")
        return
    }

    var sum = array.sum()
    println("Sum = $sum")
}

// Задание 2.
// Спроектируйте иерархию классов включающих квадрат, прямоугольник, круг, треугольник.
// Создайте список, включающий все фигуры.
// a. Получите сумму площадей всех фигур.
// b. Получите сумму периметров всех фигур.
// с. Найдите фигуру с самым большим периметром.
// d. Найдите фигуры с самой большой площадью.

abstract class Shape {
    abstract fun perimeter(): Double
    abstract fun square(): Double
}

open class Rect(
    private val a: Double,
    private val b: Double,
): Shape() {
    override fun perimeter(): Double {
        return 2 * (a + b)
    }

    override fun square(): Double {
        return a * b
    }
}

class Square(a: Double): Rect(a, a) {}

class Circle(
    private val r: Double,
): Shape() {
    override fun perimeter(): Double {
        return 2 * Math.PI * r
    }

    override fun square(): Double {
        return Math.PI * r * r
    }
}

class Triangle(
    private val a: Double,
    private val b: Double,
    private val c: Double,
): Shape() {
    override fun perimeter(): Double {
        return a + b + c
    }

    override fun square(): Double {
        return sqrt(perimeter() * (perimeter() - a) * (perimeter() - b) * (perimeter() - c))
    }
}

fun task2() {
    println("Task 2")

    val rect = Rect(4.0, 6.0)
    val square = Square(4.0)
    val circle = Circle(4.0)
    val triangle = Triangle(4.0, 6.0, 8.0)

    val shapes = arrayOf(rect, square, circle, triangle)

    val perimetersSum = shapes.sumOf { it.perimeter() }
    val squaresSum = shapes.sumOf { it.square() }
    val maxPerimeter = shapes.maxOf { it.perimeter() }
    val maxSquare = shapes.maxOf { it.square() }

    println("Perimeter's sum = $perimetersSum")
    println("Square's sum = $squaresSum")
    println("Max perimeter = $maxPerimeter")
    println("Max square = $maxSquare")
}
