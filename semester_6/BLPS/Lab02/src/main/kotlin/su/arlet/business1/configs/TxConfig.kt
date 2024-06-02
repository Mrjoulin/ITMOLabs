package su.arlet.business1.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.TransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.jta.JtaTransactionManager

@EnableTransactionManagement
@Configuration
class TxConfig {
    @Bean(name = ["transactionManager"])
    fun platformTransactionManager(): TransactionManager {
        return JtaTransactionManager()
    }
}