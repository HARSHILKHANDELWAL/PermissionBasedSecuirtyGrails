package security

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import org.postgresql.jdbc.PreferQueryMode
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
        def demoUser = request.JSON.user
        def password = request.JSON.password
        def role = request.JSON.role
        try {
            def user = userService.createUser(demoUser, password, role)
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

    def createRoleGroup() {
        def group = request.JSON.group
        def userRole = RoleGroup.findByName(group) ?: new RoleGroup(name: group).save(flush: true)
        render userRole as JSON
    }

    def assignRoleToGroup() {
        def group = request.JSON.group
        def newRole = request.JSON.role
        def roleGroup = RoleGroup.findByName(group) ?: new RoleGroup(name: group).save(flush: true)
        def role = Role.findByAuthorityAndTenantId(newRole, 'UNO_T2') ?: new Role(authority: newRole, tenantId: 'UNO_T2').save(flush: true)
        if (!RoleGroupRole.exists(roleGroup.id, role.id, "UNO_T2")) {
            def assignRole = RoleGroupRole.create(roleGroup, role, "UNO_T2", true)

            render assignRole
        } else
            render "Already Exist "
    }

    def assignPermission() {
        def group = request.JSON.group
        def newPermission = request.JSON.permission
        def roleGroup = RoleGroup.findByName(group) ?: new RoleGroup(name: group).save(flush: true)
        def permission = Permission.findByNameAndTenantId(newPermission,'UNO_T2') ?: new Permission(name: newPermission, tenantId: 'UNO_T2').save(flush: true)
        if (!RoleGroupPermission.exists(roleGroup.id, permission.id)) {
            def assignPermission = RoleGroupPermission.create(roleGroup, permission, true)
            render assignPermission as JSON

        } else
            render "Already exists"


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