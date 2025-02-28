package com.notaaiassignmentrdbac.application.config.security

import com.notaaiassignmentrdbac.domain.account.dto.AccountJwtPayload
import com.notaaiassignmentrdbac.domain.common.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider

) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7)
            if (jwtTokenProvider.validateToken(token)) {
                val tokenResult: AccountJwtPayload = jwtTokenProvider.parseAuthorizationToken(token)

                val userDetails: UserDetails =
                    CustomUserDetails(CustomUserDto(
                        accountId= tokenResult.accountId,
                        role = tokenResult.role,
                        tenantKey = tokenResult.tenantKey
                    ))

                if (userDetails != null) {
                    val usernamePasswordAuthenticationToken =
                        UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}
