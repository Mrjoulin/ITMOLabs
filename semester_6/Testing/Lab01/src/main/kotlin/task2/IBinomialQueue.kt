package task2

interface IBinomialQueue {
    var size: Int
    val isEmpty: Boolean
    fun makeEmpty()

    fun capacity(): Int
    fun merge(rhs: BinomialQueue)
    fun insert(x: Int)
    fun findMin(): Int?
    fun deleteMin(): Int?
}