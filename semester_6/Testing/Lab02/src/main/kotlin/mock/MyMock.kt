package mock


import java.lang.reflect.Proxy
import kotlin.reflect.KFunction


class MyMock<T>(private val targetClass: Class<T>) {
    private val methodMap = mutableMapOf<String, MutableList<MockMethod<*>>>()

    fun <R> whenCalled(method: KFunction<R>): MockMethod<R> {
        val methodName = method.name
        val mockMethod = MockMethod<R>(method.parameters)
        methodMap.getOrPut(methodName) { mutableListOf() }.add(mockMethod)
        return mockMethod
    }

    operator fun <R> invoke(proxy: Any, method: KFunction<R>, args: Array<Any?>?): Any? {
        val methodName = method.name
        val mockMethods = methodMap[methodName]
        val returnValue = mockMethods?.asSequence()
            ?.mapNotNull { it.invoke(args) }
            ?.firstOrNull()
        return returnValue ?: createDefaultReturnValue<R>(method)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R> createDefaultReturnValue(method: KFunction<R>): R? {
        return when (method.returnType.classifier) {
            Int::class -> 0
            Double::class -> 0.0
            String::class -> ""
            else -> null
        } as R?
    }


    @Suppress("UNCHECKED_CAST")
    fun build(): T {
        val mockHandler = MockHandler(this)
        return Proxy.newProxyInstance(
            targetClass.classLoader,
            arrayOf(targetClass),
            mockHandler
        ) as T
    }
}



