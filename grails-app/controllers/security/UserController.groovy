package security

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

@Transactional

class UserController {


    @Autowired
    SpringSecurityService springSecurityService

    UserService userService =
            new UserService()

    def createUser() {
        try {
            def user = userService.createUser("user", "user")
            if (user == null)
                render 'Already created successfully'
            else
              render'User created successfully'

        }
        catch (Exception e) {
            println(e.message)
        }
    }

//    @Secured(['PERMISSION_USER_ACCESS'])
//    @PreAuthorize("hasPermission(authentication, 'PERMISSION_USER_ACCESS')")
    @PreAuthorize("hasPermission('Role', 'PERMISSION_USER_ACCESS')")
    def accessByUser() {
        render 'Access by  user successfully'
    }
    def show(Long id) {
        // Your logic here
    }

    def accessByAdmin() {

        render 'Access by  ADMIN successfully'
    }

    def verifyAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
        String username = authentication.getName() // Get the username
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities() // Get roles

        render([username: username, roles: authorities*.authority] as JSON)

    }
}