package su.arlet.business1.security.jaas

import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import javax.security.auth.callback.CallbackHandler
import javax.security.auth.login.AppConfigurationEntry
import javax.security.auth.login.Configuration
import javax.security.auth.login.LoginContext

class MyJAASAuthenticationProvider : AbstractJaasAuthenticationProvider() {
    private var configuration: Configuration? = null
    private val loginContextName = "SPRINGSECURITY"

    private fun createConfiguration() {
        val mappedConfigurations = mutableMapOf<String, Array<out AppConfigurationEntry>>()

        mappedConfigurations[loginContextName] = arrayOf(
            AppConfigurationEntry(
                "su.arlet.business1.security.jaas.Login",
                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                emptyMap<String, Any>()
            )
        )

        configuration = InMemoryConfiguration(mappedConfigurations)
    }

    override fun createLoginContext(handler: CallbackHandler?): LoginContext {
        if (configuration == null) createConfiguration()

        return LoginContext(loginContextName, null, handler, configuration)
    }

    fun setUserDetailsService(userDetailsService: UserDetailsService) {
        setAuthorityGranters(arrayOf(RoleGranter(userDetailsService)))
    }
}