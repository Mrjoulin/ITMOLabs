package task2


class BinomialQueue : IBinomialQueue {
    private val MAX_TREES = 31
    private val theTrees: Array<BinomialNode?> = arrayOfNulls(MAX_TREES)
    override var size = 0

    /**
     * Test if the priority queue is logically empty.
     * @return true if empty, false otherwise.
     */
    override val isEmpty: Boolean
        get() = size == 0

    /**
     * Make the priority queue logically empty.
     */
    override fun makeEmpty() {
        size = 0
        for (i in theTrees.indices) theTrees[i] = null
    }

    init { makeEmpty() }

    /**
     * Return the capacity.
     */
    override fun capacity(): Int {
        return (1 shl theTrees.size) - 1
    }

    /**
     * Merge rhs into the priority queue.
     * rhs becomes empty. rhs must be different from this.
     * @param rhs the other binomial queue.
     * @exception Exception if result exceeds capacity.
     */
    override fun merge(rhs: BinomialQueue) {
        if (this === rhs) return
        if (size + rhs.size > capacity()) throw Exception("Not enough capacity")
        size += rhs.size
        var carry: BinomialNode? = null
        var i = 0
        var j = 1
        while (j <= size) {
            val t1 = theTrees[i]
            val t2 = rhs.theTrees[i]
            var whichCase = if (t1 == null) 0 else 1
            whichCase += if (t2 == null) 0 else 2
            whichCase += if (carry == null) 0 else 4
            when (whichCase) {
                0, 1 -> {}
                2 -> {
                    theTrees[i] = t2
                    rhs.theTrees[i] = null
                }
                3 -> {
                    carry = combineTrees(t1, t2)
                    run {
                        rhs.theTrees[i] = null
                        theTrees[i] = rhs.theTrees[i]
                    }
                }
                4 -> {
                    theTrees[i] = carry
                    carry = null
                }
                5 -> {
                    carry = combineTrees(t1, carry)
                    theTrees[i] = null
                }
                6 -> {
                    carry = combineTrees(t2, carry)
                    rhs.theTrees[i] = null
                }
                7 -> {
                    theTrees[i] = carry
                    carry = combineTrees(t1, t2)
                    rhs.theTrees[i] = null
                }
            }
            i++
            j *= 2
        }
        for (k in rhs.theTrees.indices) rhs.theTrees[k] = null
        rhs.size = 0
    }

    /**
     * Insert into the priority queue, maintaining heap order.
     * This implementation is not optimized for O(1) performance.
     * @param x the item to insert.
     * @exception Overflow if capacity exceeded.
     */
    override fun insert(x: Int) {
        val oneItem = BinomialQueue()
        oneItem.size = 1
        oneItem.theTrees[0] = BinomialNode(x)
        merge(oneItem)
    }

    /**
     * Find the smallest item in the priority queue.
     * @return the smallest item, or null, if empty.
     */
    override fun findMin(): Int? {
        return if (isEmpty) null else theTrees[findMinIndex()]!!.element
    }

    /**
     * Find index of tree containing the smallest item in the priority queue.
     * The priority queue must not be empty.
     * @return the index of tree containing the smallest item.
     */
    private fun findMinIndex(): Int {
        var minIndex: Int
        var i = 0
        while (theTrees[i] == null) i++

        minIndex = i
        while (i < theTrees.size) {
            if (theTrees[i] != null && theTrees[i]!!.element < theTrees[minIndex]!!.element)
                minIndex = i
            i++
        }
        return minIndex
    }

    /**
     * Remove the smallest item from the priority queue.
     * @return the smallest item, or null, if empty.
     */
    override fun deleteMin(): Int? {
        if (isEmpty) return null
        val minIndex = findMinIndex()
        val minItem = theTrees[minIndex]!!.element
        var deletedTree = theTrees[minIndex]!!.leftChild
        val deletedQueue = BinomialQueue()
        deletedQueue.size = (1 shl minIndex) - 1

        for (j in minIndex - 1 downTo 0) {
            deletedQueue.theTrees[j] = deletedTree
            deletedTree = deletedTree?.nextSibling
            deletedQueue.theTrees[j]?.nextSibling = null
        }

        theTrees[minIndex] = null
        size -= deletedQueue.size + 1

        try {
            merge(deletedQueue)
        } catch (_: Exception) { }

        return minItem
    }

    /**
     * Return the result of merging equal-sized t1 and t2.
     */
    private fun combineTrees(t1: BinomialNode?, t2: BinomialNode?): BinomialNode? {
        if (t1!!.element > t2!!.element) return combineTrees(t2, t1)
        t2.nextSibling = t1.leftChild
        t1.leftChild = t2
        return t1
    }
}