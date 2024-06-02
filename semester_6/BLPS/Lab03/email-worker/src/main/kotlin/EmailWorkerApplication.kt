package su.arlet

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EmailWorkerApplication : SpringApplication()

fun main(args: Array<String>) {
    runApplication<EmailWorkerApplication>(*args)
}