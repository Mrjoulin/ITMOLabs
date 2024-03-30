package log

import interfaces.BasicFunction
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.math.ln

class LnTest {
    private val ln: BasicFunction = Ln()

    companion object {
        private const val NUM_DOTS = 100
        private const val PRECISION = 1e-6

        private val testSmallDots: DoubleArray = DoubleArray(NUM_DOTS).also {
            (1..NUM_DOTS).forEach { i -> it[i - 1] = 0.01 * i }
        }
        private val testLargeDots: DoubleArray = DoubleArray(NUM_DOTS).also {
            (1..NUM_DOTS).forEach { i -> it[i - 1] = 1000.0 * i }
        }
    }

    @Test
    fun testNegative() {
        assertThrows(
            IllegalArgumentException::class.java, { ln.calc(-1) }, "x must be positive"
        )
    }

    @Test
    fun testCalcZero() {
        assertThrows(
            IllegalArgumentException::class.java, { ln.calc(0) }, "x must be positive"
        )
    }

    @Test
    fun testOne() = assertEquals(0.0, ln.calc(1.0, PRECISION), PRECISION)


    @Test
    fun testBigPrecision() = assertEquals(ln(2.0), ln.calc(2.0, 1.0), 1.0)

    @Test
    fun testSmallPrecision() = assertEquals(ln(2.0), ln.calc(2.0, 1e-10), 1e-10)

    @TestFactory
    fun dynamicTestsSmallDots(): Iterable<DynamicTest> {
        return testSmallDots.map {
            DynamicTest.dynamicTest("Check small for %.2f".format(it)) {
                assertEquals(ln(it), ln.calc(it), PRECISION)
            }
        }
    }

    @TestFactory
    fun dynamicTestsLargeDots(): Iterable<DynamicTest> {
        return testLargeDots.map {
            DynamicTest.dynamicTest("Check large or %.2f".format(it)) {
                assertEquals(ln(it), ln.calc(it), PRECISION)
            }
        }
    }
}