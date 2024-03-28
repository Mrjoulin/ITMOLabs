package task2

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory


class BinomialQueueTest {
    private val insertNumbers = arrayOf(10, 50, 100, 1000, 10000, 100000, 1000000, 1 shl 31 - 1)

    @Test
    fun creationEmpty() {
        val binomialQueue: IBinomialQueue = BinomialQueue()

        assertEquals(0, binomialQueue.size)
        assertTrue(binomialQueue.isEmpty)
    }

    @Test
    fun simpleInsert() {
        val binomialQueue: IBinomialQueue = BinomialQueue()
        binomialQueue.insert(1)

        assertEquals(1, binomialQueue.size)

        binomialQueue.insert(2)

        assertEquals(2, binomialQueue.size)
        assertFalse(binomialQueue.isEmpty)
    }

    @Test
    fun insertSame() {
        val binomialQueue: IBinomialQueue = BinomialQueue()
        binomialQueue.insert(1)

        assertEquals(1, binomialQueue.size)

        binomialQueue.insert(1)

        assertEquals(2, binomialQueue.size)
        assertFalse(binomialQueue.isEmpty)
    }

    @Test
    fun insertNegative() {
        val binomialQueue: IBinomialQueue = BinomialQueue()
        binomialQueue.insert(-1)

        assertEquals(1, binomialQueue.size)
        assertFalse(binomialQueue.isEmpty)
    }

    @Test
    fun makeEmpty() {
        val binomialQueue: IBinomialQueue = BinomialQueue()

        binomialQueue.insert(1)
        assertFalse(binomialQueue.isEmpty)

        binomialQueue.makeEmpty()
        assertTrue(binomialQueue.isEmpty)
    }

    @Test
    fun findMinimum() {
        val binomialQueue: IBinomialQueue = BinomialQueue()
        binomialQueue.insert(4)
        binomialQueue.insert(2)
        binomialQueue.insert(3)

        assertEquals(2, binomialQueue.findMin())
    }

    @Test
    fun extractMin() {
        val binomialQueue: IBinomialQueue = BinomialQueue()
        binomialQueue.insert(4)
        binomialQueue.insert(3)
        binomialQueue.insert(1)
        binomialQueue.insert(2)

        assertEquals(1, binomialQueue.deleteMin())
        assertEquals(3, binomialQueue.size)
        assertEquals(2, binomialQueue.deleteMin())
        assertEquals(3, binomialQueue.deleteMin())
        assertEquals(4, binomialQueue.deleteMin())

        assertTrue(binomialQueue.isEmpty)
    }

    @Test
    fun extractMinEmpty() {
        val binomialQueue: IBinomialQueue = BinomialQueue()
        assertEquals(null, binomialQueue.deleteMin())

        binomialQueue.insert(4)

        assertEquals(4, binomialQueue.deleteMin())
        assertEquals(null, binomialQueue.deleteMin())

        assertTrue(binomialQueue.isEmpty)
    }

    @Test
    fun deleteMinimal() {
        val binomialQueue: IBinomialQueue = BinomialQueue()
        binomialQueue.insert(1)
        binomialQueue.insert(2)
        binomialQueue.insert(3)

        binomialQueue.deleteMin()

        assertEquals(2, binomialQueue.findMin())
        assertEquals(2, binomialQueue.size)
    }

    @Test
    fun deleteFromEmpty() {
        val binomialQueue: IBinomialQueue = BinomialQueue()

        binomialQueue.deleteMin()

        assertTrue(binomialQueue.isEmpty)
    }

    @Test
    fun deleteSeveral() {
        val binomialQueue: IBinomialQueue = BinomialQueue()
        binomialQueue.insert(5)
        binomialQueue.insert(3)
        binomialQueue.insert(1)
        binomialQueue.insert(4)
        binomialQueue.insert(2)

        binomialQueue.deleteMin()

        assertEquals(2, binomialQueue.findMin())
        assertEquals(4, binomialQueue.size)

        binomialQueue.deleteMin()

        assertEquals(3, binomialQueue.findMin())
        assertEquals(3, binomialQueue.size)
    }

    @TestFactory
    fun dynamicTestInsertALot(): Iterable<DynamicTest> {
        return insertNumbers.map {
            DynamicTest.dynamicTest("Check insert %d".format(it)) {
                insertALot(it)
            }
        }
    }

    private fun insertALot(numInsert: Int) {
        val binomialQueue: IBinomialQueue = BinomialQueue()

        for (i in 1..numInsert) binomialQueue.insert(i)

        assertEquals(1, binomialQueue.findMin())
        assertEquals(numInsert, binomialQueue.size)

        binomialQueue.makeEmpty()
    }

    @TestFactory
    fun dynamicTestInsertALotAndDeleteAll(): Iterable<DynamicTest> {
        return insertNumbers.map {
            DynamicTest.dynamicTest("Check insert %d and delete all".format(it)) {
                insertALotAndDeleteAll(it)
            }
        }
    }

    private fun insertALotAndDeleteAll(numInsert: Int) {
        val binomialQueue: IBinomialQueue = BinomialQueue()

        for (i in 1..numInsert) binomialQueue.insert(i)

        assertEquals(numInsert, binomialQueue.size)

        for (i in 1..numInsert) {
            assertEquals(i, binomialQueue.deleteMin())
            assertEquals(numInsert - i, binomialQueue.size)
        }

        assertTrue(binomialQueue.isEmpty)
        binomialQueue.makeEmpty()
    }


    @Test
    fun mergeQueues() {
        val queue1 = BinomialQueue()
        val queue2 = BinomialQueue()

        queue1.insert(2)
        queue1.insert(3)
        queue1.insert(3)

        assertEquals(3, queue1.size)

        queue2.insert(1)
        queue2.insert(5)

        assertEquals(2, queue2.size)

        queue1.merge(queue2)

        assertEquals(5, queue1.size)
        assertEquals(1, queue1.deleteMin())
        assertEquals(2, queue1.deleteMin())
        assertEquals(3, queue1.deleteMin())
        assertEquals(3, queue1.deleteMin())
        assertEquals(5, queue1.deleteMin())
    }

    @Test
    fun mergeWithEmptyQueue() {
        val queue1 = BinomialQueue()
        val queue2 = BinomialQueue()

        queue1.insert(2)
        queue1.insert(3)
        queue1.insert(3)

        assertEquals(3, queue1.size)
        assertEquals(0, queue2.size)

        queue1.merge(queue2)

        assertEquals(3, queue1.size)
        assertEquals(2, queue1.deleteMin())
        assertEquals(3, queue1.deleteMin())
    }
}