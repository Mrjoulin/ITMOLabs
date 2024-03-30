package mock

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import kotlin.reflect.jvm.kotlinFunction

class MockHandler(private val mock: MyMock<*>) : InvocationHandler {
    override fun invoke(proxy: Any, method: Method, args: Array<Any?>?): Any? {
        val kFunction = method.kotlinFunction ?: error("Failed to find corresponding Kotlin function")
        return mock(proxy, kFunction, args)
    }
}
