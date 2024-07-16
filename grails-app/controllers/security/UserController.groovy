package security

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.PermissionEvaluator
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
            def user = userService.createUser("admin", "admin")
            println(user as JSON)
            if (user == null)
                render 'Already created successfully'
            else
                render 'User created successfully'

        }
        catch (Exception e) {
            println(e.message)
        }
    }

    def assignRoleToGroup() {

        def roleGroup = RoleGroup.findByName('ADMIN_GROUP') ?: new RoleGroup(name: 'ADMIN_GROUP').save(flush: true)
        def role = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN', tenantId: 'UNO_T1').save(flush: true)
        def assignRole = RoleGroupRole.create(roleGroup, role, true)
        render assignRole
    }
    def assignPermission() {

        def roleGroup = RoleGroup.findByName('ADMIN_GROUP') ?: new RoleGroup(name: 'ADMIN_GROUP').save(flush: true)
        def permission = Permission.findByName('PERMISSION_WRITE') ?: new Permission(name: 'PERMISSION_WRITE', tenantId: 'UNO_T1').save(flush: true)
        def assignPermission = RoleGroupPermission.create(roleGroup, permission, true)
        render assignPermission as JSON
    }


    def createRoleGroup() {
        def userRole = RoleGroup.findByName('ADMIN_GROUP') ?: new RoleGroup(name: 'ADMIN_GROUP').save(flush: true)
        render userRole as JSON
    }


//    @PreAuthorize("hasPermission(authentication, 'PERMISSION_USER_ACCESS')")
    @PreAuthorize("hasPermission('Role', 'PERMISSION_USER_ACCES')")
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