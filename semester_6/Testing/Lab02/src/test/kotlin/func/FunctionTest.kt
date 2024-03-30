package func

import interfaces.BasicFunction
import log.Ln
import log.Log
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.*
import trig.Sin
import kotlin.math.*

// Все моки без мокито

class FunctionTest {
    private val precision = 1e-6

    @Test
    fun testFunctionNegativeWithMock() {
        val mockLn = mock<BasicFunction> {
            on { calc(any()) } doReturn 1.0
        }
        val mockSin = mock<BasicFunction> {
            on { calc(-1) } doReturn -1.0
        }

        val func = Function(mockSin, mockLn, mockLn, mockLn, mockLn)

        assertEquals(-1.0, func.calc(-1), precision)

        verify(mockSin, only()).calc(-1)
        verify(mockLn, never()).calc(any())
    }

    @Test
    fun testZeroWithMock() {
        val mockLn = mock<BasicFunction> {
            on { calc(any()) } doReturn 1.0
        }
        val mockSin = mock<BasicFunction> {
            on { calc(0) } doReturn 0.0
        }

        val func = Function(mockSin, mockLn, mockLn, mockLn, mockLn)

        assertEquals(0.0, func.calc(0), precision)

        verify(mockSin, only()).calc(0)
        verify(mockLn, never()).calc(any())
    }

    @Test
    fun testOneWithMock() {
        val mockLn = mock<BasicFunction> {
            on { calc(1) } doReturn Double.NaN
        }
        val mockSin = mock<BasicFunction> {
            on { calc(any()) } doReturn 0.0
        }

        val func = Function(mockSin, mockLn, mockLn, mockLn, mockLn)

        assertEquals(Double.NaN, func.calc(1), precision)

        verify(mockLn, times(8)).calc(1)
        verify(mockSin, never()).calc(any())
    }

    @Test
    fun testPositiveWithMock() {
        val mockLn = mock<BasicFunction> {
            on { calc(2) } doReturn ln(2.0)
        }
        val mockLog2 = mock<BasicFunction> {
            on { calc(2) } doReturn log2(2.0)
        }
        val mockLog3  = mock<BasicFunction> {
            on { calc(2) } doReturn log(2.0, 3.0)
        }
        val mockLog10 = mock<BasicFunction> {
            on { calc(2) } doReturn log(2.0, 10.0)
        }
        val mockSin = mock<BasicFunction> {
            on { calc(any()) } doReturn 0.0
        }

        val func = Function(mockSin, mockLn, mockLog2, mockLog3, mockLog10)
        val expect = log(2.0, 3.0) * (log(2.0, 10.0) / log(2.0, 3.0)).pow(2) -
                (log2(2.0) + ln(2.0).pow(2) + log(2.0, 3.0).pow(2))

        assertEquals(expect, func.calc(2), precision)

        verify(mockLn, only()).calc(2)
        verify(mockLog2, only()).calc(2)
        verify(mockLog3, times(5)).calc(2)
        verify(mockLog10, only()).calc(2)
        verify(mockSin, never()).calc(any())
    }

    @ParameterizedTest
    @ValueSource(doubles = [-1000.0, -100.0, -5.0, -3.5, -PI, -1.0, -0.5])
    fun testFunctionNegative(param: Double) {
        val sin = mock<BasicFunction> {
            on { calc(-1000.0) } doReturn -0.8268795405
            on { calc(-5.0) } doReturn 0.9589242747
            on { calc(-2 * PI) } doReturn 0.9589242747
        }
        val mockLn = mock<BasicFunction> {
            on { calc(any()) } doReturn 0.0
        }

        val func = Function(sin, mockLn, mockLn, mockLn, mockLn)

        assertEquals(sin(param), func.calc(param), precision)

        verify(mockLn, never()).calc(any())
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.01, 0.1, 0.5, 1.0, 1.5, 2.0, 100.0, 1000.0])
    fun testFunctionPositive(x: Double) {
        val mockSin = mock<BasicFunction> {
            on { calc(any()) } doReturn 0.0
        }
        val ln = Ln()
        val log2 = Log(2, ln)
        val log3 = Log(3, ln)
        val log10 = Log(10, ln)

        val func = Function(mockSin, ln, log2, log3, log10)
        val expected = log(x, 3.0) * (log(x, 10.0) / log(x, 3.0)).pow(2) -
                            (log(x, 2.0) + ln(x).pow(2) + log(x, 3.0).pow(2))

        assertEquals(expected, func.calc(x), precision)

        verify(mockSin, never()).calc(any())
    }

    @Test
    fun testFunctionOne() {
        val mockSin = mock<BasicFunction> {
            on { calc(any()) } doReturn 0.0
        }
        val ln = Ln()
        val log2 = Log(2, ln)
        val log3 = Log(3, ln)
        val log10 = Log(10, ln)

        val func = Function(mockSin, ln, log2, log3, log10)

        assertEquals(Double.NaN, func.calc(1.0))

        verify(mockSin, never()).calc(any())
    }
}