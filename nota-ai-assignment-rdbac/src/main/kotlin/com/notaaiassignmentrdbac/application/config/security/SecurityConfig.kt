package com.notaaiassignmentrdbac.application.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val securityAccessDeniedHandler: SecurityAccessDeniedHandler,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) {

    // 비밀번호 암호화
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        //CSRF, CORS
        http.csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }
        http.cors(Customizer.withDefaults())


        http.sessionManagement { sessionManagement: SessionManagementConfigurer<HttpSecurity?> ->
            sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            )
        }

        //FormLogin, BasicHttp 비활성화
        http.formLogin { form: FormLoginConfigurer<HttpSecurity> -> form.disable() }
        http.httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }

        http.addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )


        // 권한 규칙 작성
        http.authorizeHttpRequests { httpRequest ->
            httpRequest.requestMatchers("/account/password/*").authenticated()
            httpRequest.requestMatchers("/account/*").permitAll()
            httpRequest.anyRequest().authenticated()
        }

        http.exceptionHandling { exceptions ->
            exceptions.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(securityAccessDeniedHandler)
        }
        return http.build()
    }

}