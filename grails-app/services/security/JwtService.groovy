package security

import grails.core.GrailsApplication
import grails.util.Holders
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import java.security.Key
class JwtService {
    @Autowired
    GrailsApplication grailsApplication
    def getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    Key key() {
        String secret=Holders.applicationContext.grailsApplication.config.grails.jwt.secret
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
    boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token)
            for (String key : claims.keySet()) {
                System.out.println(key + ": " + claims.get(key));
            }
            return true
        } catch (Exception e) {
            return false
        }
    }

    String getUsernameFromToken(String token) {

        return getClaimsFromToken(token)?.getSubject()
    }


    String getTenantIdFromToken(String token) {
        return getClaimsFromToken(token)?.get("tenantId", String)
    }

    String getUserIdFromToken(String token) {
        return getClaimsFromToken(token)?.get("userId", String)
    }
    String getRoleFromToken(String token) {
        return getClaimsFromToken(token)?.get("role", String)
    }


    def extractUserIdTenantId(def request) {
        def authHeader = request.getHeader("Authorization")
        if (authHeader?.startsWith("Bearer ")) {
            def token = authHeader.substring(7)

            if (validateToken(token)) {
                def username = getUsernameFromToken(token)
                def tenantId = getTenantIdFromToken(token)
                def userid = getUserIdFromToken(token)
                println tenantId

                return [tenantId, userid]
            }
        }

    }

}