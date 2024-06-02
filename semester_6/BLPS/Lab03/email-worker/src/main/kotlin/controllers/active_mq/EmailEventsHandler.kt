package su.arlet.controllers.active_mq

import EmailInfo
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import su.arlet.services.EmailService

@Component
class EmailEventsHandler(
    val emailService: EmailService,
) {

    @JmsListener(destination = "email-queue")
    fun receiveEvent(emailInfo: EmailInfo) {
        emailService.sendEmail(emailInfo)
    }
}