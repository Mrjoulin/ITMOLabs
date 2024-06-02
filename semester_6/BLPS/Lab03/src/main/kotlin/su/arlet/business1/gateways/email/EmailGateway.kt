package su.arlet.business1.gateways.email

import EmailInfo
import com.fasterxml.jackson.databind.ObjectMapper
import letters.Letter
import net.ser1.stomp.Client
import org.springframework.stereotype.Service

@Service
class EmailGateway(
    private val stompClient: Client,
) {
    private val queueName = "email-queue"
    fun sendEmail(to: String, letter: Letter) {
        val encodedValue = ObjectMapper().writeValueAsString(EmailInfo(to, letter))
        stompClient.send(queueName, encodedValue, hashMapOf(Pair("_type", EmailInfo::class.java.name)))
    }
}