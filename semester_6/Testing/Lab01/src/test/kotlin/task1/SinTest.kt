package task1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import kotlin.math.sin


class SinTest {
    private val sin: Sin = Sin()

    companion object {
        private const val NUM_DOTS = 64
        private const val PI = Math.PI
        private const val PRECISION = 1e-4

        private val testSmallDots: DoubleArray = DoubleArray(NUM_DOTS).also {
            (0..<NUM_DOTS).forEach { i -> it[i] = 0.1 * i }
        }
        private val testLargeDots: DoubleArray = DoubleArray(NUM_DOTS).also {
            (0..<NUM_DOTS).forEach { i -> it[i] = 1000000.0 * i }
        }
        private val testNegativeSmallDots: DoubleArray = DoubleArray(NUM_DOTS).also {
            (0..<NUM_DOTS).forEach { i -> it[i] = (-0.1) * i }
        }
        private val testNegativeLargeDots: DoubleArray = DoubleArray(NUM_DOTS).also {
            (0..<NUM_DOTS).forEach { i -> it[i] = (-1000000.0) * i }
        }
    }

    @Test
    fun testCalcZero() = assertEquals(0.0, sin.calc(0), PRECISION)

    @Test
    fun testPI() = assertEquals(0.0, sin.calc(PI), PRECISION)


    @Test
    fun testHalfPI() = assertEquals(1.0, sin.calc(PI / 2), PRECISION)

    @Test
    fun testMinusHalfPI() = assertEquals(-1.0, sin.calc(-PI / 2), PRECISION)

    @Test
    fun testDoublePI() = assertEquals(0.0, sin.calc(2 * PI), PRECISION)

    @TestFactory
    fun dynamicTestsSmallDots(): Iterable<DynamicTest> {
        return testSmallDots.map {
            DynamicTest.dynamicTest("Check small for %.2f".format(it)) {
                assertEquals(sin(it), sin.calc(it), PRECISION)
            }
        }
    }

    @TestFactory
    fun dynamicTestsLargeDots(): Iterable<DynamicTest> {
        return testLargeDots.map {
            DynamicTest.dynamicTest("Check large or %.2f".format(it)) {
                assertEquals(sin(it), sin.calc(it), PRECISION)
            }
        }
    }

    @TestFactory
    fun dynamicTestsNegativeSmallDots(): Iterable<DynamicTest> {
        return testNegativeSmallDots.map {
            DynamicTest.dynamicTest("Check negative small for %.2f".format(it)) {
                assertEquals(sin(it), sin.calc(it), PRECISION)
            }
        }
    }

    @TestFactory
    fun dynamicTestsNegativeLargeDots(): Iterable<DynamicTest> {
        return testNegativeLargeDots.map {
            DynamicTest.dynamicTest("Check negative large for %.2f".format(it)) {
                assertEquals(sin(it), sin.calc(it), PRECISION)
            }
        }
    }
}