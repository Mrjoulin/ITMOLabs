import task1.Sin
import kotlin.math.round

fun main(args: Array<String>) {
    println("Sin calc")
    println(-12.3 % 5)

    val sin = Sin()
    for (i in 0..63) print("%.1f, ".format(0.1 * i))

    println("\nCalc with max precision")
    for (i in 0..63)
        println("In %.1f: %.6f".format(0.1 * i, sin.calc(0.1 * i)))

    println("\n\nCalc with min precision")
    for (i in 0..63)
        println("In %.1f: %.6f".format(0.1 * i, sin.calc(0.1 * i, 3)))

    println("\n\nCalc with big numbers")
    for (i in 0..63)
        println("In %d: %.6f".format(i, sin.calc(i)))

}
