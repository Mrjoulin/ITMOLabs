package su.arlet.services

import EmailInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


@Service
class EmailService(
    val mailSender: JavaMailSender,
) {
    @Value("\${email_from}")
    private lateinit var from: String

    @Value("\${spring.mail.username}")
    private lateinit var username: String

    fun sendEmail(emailInfo: EmailInfo) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, "utf-8")
        helper.setFrom("$from<$username>")
        helper.setTo(emailInfo.to)
        helper.setSubject(emailInfo.letter.subject)
        helper.setText(emailInfo.letter.html, true)

        mailSender.send(message)
        println("Letter ${emailInfo.letter.name} was sent to ${emailInfo.to}")
    }
}