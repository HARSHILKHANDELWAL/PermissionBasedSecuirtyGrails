package security

import grails.converters.JSON;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

class CustomPermissionEvaluator implements PermissionEvaluator {




    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // Get roles

//        render([username: username, roles: authorities*.authority] as JSON)
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        System.out.println(targetDomainObject);
        // Print each authority using System.out.println
        for (GrantedAuthority authority : authorities) {

            def auth=authority.toString()
            System.out.println("Auth++++++++" +auth);
            def role=Role.findByAuthority(authority.toString())
           return (role.permissions.authority.contains(permission))
//            System.out.println("CustomPermissionEvaluator++++++++" +authority);
        }


        return true;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || targetType == null || !(permission instanceof String)) {
            return true;
        }

        System.out.println("CustomPermissionEvaluator_____");


        // Custom logic to check if the user has the given permission on the targetType and targetId
        return true;
    }


}
