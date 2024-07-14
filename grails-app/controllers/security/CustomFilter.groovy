package security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {
        JwtService jwtService = new JwtService();

        String authHeader = request.getHeader("Authorization")
        try {

            if (authHeader && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7) // Extract the token from the header
                Claims claims = jwtService.getClaimsFromToken(token)
                try {
                    String tenantId = jwtService.getTenantIdFromToken(token)
                    String userId = jwtService.getUserIdFromToken(token)
                    String role = jwtService.getRoleFromToken(token)
                    println "Invoked custom filter   "+role
                    def roles = []
                    roles.add(role)
                    if (userId && tenantId) {
                        request.setAttribute("user_id", userId)
                        request.setAttribute("tenant_id", tenantId)
//                        return true // Proceed with the action
                        List<SimpleGrantedAuthority> authorities = roles.collect { new SimpleGrantedAuthority(it) }
//                        UsernamePasswordAuthenticationToken authentication =
//                                new UsernamePasswordAuthenticationToken(
//                                        user,
//                                        null,
//                                        user.authorities);
//                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities)
//                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication)

                        response.writer << '{"sucess": "An good occurred"}'

                        filterChain.doFilter(request, response)

                    } else {
                        response.status = 400
                        response.writer << '{"error": "An error occurred"}'
//                        return false // Stop the execution of the action
                    }

                } catch (JwtException e) {
                    response.status = 401
                    response.writer << '{"error": "An error occurred"}'
//                    return false // Stop the execution of the action
                }
            } else {
                response.status = 401
                response.writer << '{"error": "An error occurred"}'
//                return false // Stop the execution of the action
            }
//            render "all good is there"
        }
        catch (Exception e) {
            print(e.message)
            response.writer << '{"error": "An error occurred"}'
        }

    }
}