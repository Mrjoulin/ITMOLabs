package su.arlet.business1.security.jaas

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.support.WebApplicationContextUtils
import java.security.Principal
import javax.security.auth.Subject
import javax.security.auth.callback.Callback
import javax.security.auth.callback.CallbackHandler
import javax.security.auth.callback.NameCallback
import javax.security.auth.callback.PasswordCallback
import javax.security.auth.login.LoginException
import javax.security.auth.spi.LoginModule


class Login: LoginModule {
    private var usersDetailsService: UserDetailsService? = null
    private val encoder: PasswordEncoder = BCryptPasswordEncoder()
    private lateinit var subject: Subject
    private var username: String? = null
    private var password: String? = null

    class UserPrincipal(private val name: String): Principal {
        override fun getName(): String = name
    }

    override fun initialize(
        subject: Subject,
        callbackHandler: CallbackHandler,
        sharedState: MutableMap<String, *>?,
        options: MutableMap<String, *>?
    ) {
        this.subject = subject

        try {
            val nameCallback = NameCallback("login")
            val passwordCallback = PasswordCallback("password", false)

            callbackHandler.handle(
                arrayOf<Callback>(
                    nameCallback,
                    passwordCallback
                )
            )

            username = nameCallback.name
            password = String(passwordCallback.password)

            val servletContext = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)?.request?.servletContext
            val context = servletContext?.let { WebApplicationContextUtils.getWebApplicationContext(servletContext) }
            usersDetailsService = context?.getBean(UserDetailsService::class.java)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @Throws(LoginException::class)
    override fun login(): Boolean {
        if (usersDetailsService == null) throw LoginException("No context")
        if (username == null) throw LoginException("Invalid username or password")

        val userDetails = usersDetailsService!!.loadUserByUsername(username!!)

        if (!encoder.matches(password, userDetails.password))
            throw LoginException("Invalid username or password")

        subject.principals.add(UserPrincipal(username!!))
        return true
    }

    override fun commit(): Boolean {
        return true
    }

    override fun abort(): Boolean {
        return false
    }

    override fun logout(): Boolean {
        return false
    }
}