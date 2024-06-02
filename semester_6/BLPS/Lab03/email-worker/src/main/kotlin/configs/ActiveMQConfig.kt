package su.arlet.configs

import jakarta.jms.ConnectionFactory
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ActiveMQConfig {

    @Value("\${mq_host}")
    private lateinit var host: String

    @Value("\${mq_port}")
    private lateinit var port: String

    @Value("\${mq_user}")
    private lateinit var user: String

    @Value("\${mq_password}")
    private lateinit var password: String

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val url = "tcp://${host}:${port}?protocols=CORE"
        println("Connected to $url")
        return ActiveMQConnectionFactory(url, user, password)
    }
}