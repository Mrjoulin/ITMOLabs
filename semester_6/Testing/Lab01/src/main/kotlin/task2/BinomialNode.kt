package task2

data class BinomialNode(
    var element: Int,
    var leftChild: BinomialNode? = null,
    var nextSibling: BinomialNode? = null
)