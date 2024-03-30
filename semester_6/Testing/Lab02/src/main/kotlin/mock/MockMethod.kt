package mock

import kotlin.reflect.KParameter

class MockMethod<R>(private val parameters: List<KParameter>) {
    private val returnValueList = mutableListOf<Pair<List<Any?>, R>>()

    fun thenReturn(vararg arguments: Any?, returnValue: R) {
        returnValueList.add(arguments.toList() to returnValue)
    }

    fun invoke(arguments: Array<Any?>?): R? {
        val key = arguments?.toList()
        return returnValueList.firstOrNull { it.first == key }?.second
    }
}

