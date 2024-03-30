package log

import interfaces.BasicFunction
import interfaces.NewBasicFunc

class Log(
    private val base: Int,
    private val baseFun: NewBasicFunc = Ln()
) : BasicFunction {
    private val baseLn =  baseFun.calc(base)

    init {
        if (base <= 0 || base == 1)
            throw IllegalArgumentException("base should be positive and not equal to 1")
    }

    override fun calc(x: Number, precision: Double): Double =
        // baseFun.calc(x, precision) / baseLn
        baseFun.calc(x) / baseLn

    override fun calc(x: Number): Double =
        baseFun.calc(x) / baseLn

    override fun toString(): String = "log$base"
}