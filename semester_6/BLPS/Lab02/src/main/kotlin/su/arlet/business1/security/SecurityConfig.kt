package su.arlet.business1.security

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import su.arlet.business1.security.jaas.MyJAASAuthenticationProvider
import su.arlet.business1.security.jwt.AuthEntryPoint
import su.arlet.business1.security.jwt.AuthTokenFilter
import su.arlet.business1.security.services.AuthUsersDetailsService


@Configuration
@SecurityScheme(
    name = "Authorization",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig @Autowired constructor(
    private val userDetailsService: AuthUsersDetailsService,
    private val authEntryPoint: AuthEntryPoint,
) {
    @Value("\${api.path}")
    private var apiPath = ""

    @Bean
    fun authenticationJwtTokenFilter(): AuthTokenFilter {
        return AuthTokenFilter()
    }

    @Bean
    fun authenticationProvider(): MyJAASAuthenticationProvider {
        val authProvider = MyJAASAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        return authProvider
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.getAuthenticationManager()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun roleHierarchy(): RoleHierarchy {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy(
            "ROLE_ADMIN > ROLE_EDITOR\n" +
                    "ROLE_ADMIN > ROLE_SALES\n" +

                    "ROLE_EDITOR > ROLE_JOURNALIST\n" +
                    "ROLE_JOURNALIST > ROLE_DEFAULT\n" +

                    "ROLE_SALES > ROLE_ADVERTISER\n" +
                    "ROLE_ADVERTISER > ROLE_DEFAULT"
        )
        return roleHierarchy
    }

    @Bean
    fun customWebSecurityExpressionHandler(): DefaultWebSecurityExpressionHandler {
        val expressionHandler = DefaultWebSecurityExpressionHandler()
        expressionHandler.setRoleHierarchy(roleHierarchy())
        return expressionHandler
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors { obj: CorsConfigurer<HttpSecurity> -> obj.disable() }
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .exceptionHandling { exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(authEntryPoint)
            }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/swagger", "/swagger-ui/**", "/docs/**").permitAll()
                    .requestMatchers("${apiPath}/auth/**").permitAll()
                    .requestMatchers("${apiPath}/ad/posts/**").hasAnyRole("EDITOR", "SALES")
                    .requestMatchers("${apiPath}/ad/requests/**").hasRole("ADVERTISER")
                    .anyRequest().authenticated()
            }
        http.authenticationProvider(authenticationProvider())
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
