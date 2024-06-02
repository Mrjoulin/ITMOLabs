package su.arlet.business1.configs

import net.ser1.stomp.Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class StompConfig {

    @Value("\${mq_host}")
    private lateinit var host: String

    @Value("\${mq_port}")
    private var port: Int = 0

    @Value("\${mq_user}")
    private lateinit var user: String

    @Value("\${mq_password}")
    private lateinit var password: String

    @Bean
    fun stompClient(): Client {
        val client = Client(host, port, user, password)

        return client
    }
}