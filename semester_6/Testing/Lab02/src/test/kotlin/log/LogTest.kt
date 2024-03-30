package log

import interfaces.BasicFunction
import interfaces.NewBasicFunc
import mock.MyMock
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.mockito.kotlin.*
import kotlin.math.ln
import kotlin.math.log

class LogTest {
    private val precision = 1e-6

    @Test
    fun testNegative() {
        val mockLn = mock<BasicFunction> {
            on { calc(3) } doReturn 3.0
            on { calc(-1) } doThrow IllegalArgumentException("x must be positive")
        }

        val log = Log(3, mockLn)

        verify(mockLn).calc(3)

        assertThrows(
            IllegalArgumentException::class.java, { log.calc(-1) }, "x must be positive"
        )

        verify(mockLn).calc(-1)
    }

    @Test
    fun testCalcZero() {
        val mockLn = mock<BasicFunction> {
            on { calc(3) } doReturn 3.0
            on { calc(0) } doThrow IllegalArgumentException("x must be positive")
        }

        val log = Log(3, mockLn)
        verify(mockLn).calc(3)

        assertThrows(
            IllegalArgumentException::class.java, { log.calc(0) }, "x must be positive"
        )
        verify(mockLn).calc(0)
    }

    @Test
    fun testOne() {
        val mockLn = mock<BasicFunction> {
            on { calc(3) } doReturn 3.0
            on { calc(eq(1.0), any()) } doReturn 0.0
        }

        val log = Log(3, mockLn)

        assertEquals(0.0, log.calc(1.0, precision), precision)
    }


    @Test
    fun testOneMyMock() {
        val mockLn = MyMock(NewBasicFunc::class.java)
        mockLn.whenCalled(NewBasicFunc::calc).thenReturn(3, returnValue = 3.0)
        mockLn.whenCalled(NewBasicFunc::calc).thenReturn(1.0, returnValue = 0.0)

        val mockInst = mockLn.build()

        val log = Log(3, mockInst)

        assertEquals(0.0, log.calc(1.0, precision), precision)
    }


    @Test
    fun testBigPrecision() {
        val mockLn = mock<BasicFunction> {
            on { calc(3) } doReturn 1.0986122
            on { calc(eq(2.0), any()) } doReturn 0.693147
        }

        val log = Log(3, mockLn)

        assertEquals(log(2.0, 3.0), log.calc(2.0, 0.5), 0.5)
    }

    @Test
    fun testSmallPrecision() {
        val mockLn = mock<BasicFunction> {
            on { calc(5) } doReturn 1.60943791243
            on { calc(eq(2.0), any()) } doReturn 0.69314718056
        }

        val log = Log(5, mockLn)

        assertEquals(log(2.0, 5.0), log.calc(2.0, 1e-12), precision)
    }

    @Test
    fun testLargeDot() {
        val mockLn = mock<BasicFunction> {
            on { calc(5) } doReturn 1.6094379
            on { calc(eq(1000.0), any()) } doReturn 6.907755
        }

        val log = Log(5, mockLn)

        assertEquals(log(1000.0, 5.0), log.calc(1000.0, 1e-12), precision)
    }

    @Test
    fun testOneBase() {
        val mockLn = mock<BasicFunction> {}

        assertThrows(
            IllegalArgumentException::class.java,
            { Log(1, mockLn) } ,
            "base should be positive and not equal to 1"
        )
    }
}