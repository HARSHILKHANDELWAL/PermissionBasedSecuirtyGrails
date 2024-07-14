package security

import grails.converters.JSON
import security.Role
import security.User
import grails.gorm.transactions.Transactional
import security.UserRole
@Transactional
class UserService {

     def createUser(String username, String password) {
//        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(flush: true)
//        def user = User.findByUsername(username) ?: new User(username: username, password: password).save(flush: true)
//        if (!user.authorities.contains(userRole)) {
//            UserRole.create(user, userRole, true)
//        }

         def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(flush: true)
         def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(flush: true)

         def userPermission = Permission.findByAuthority('PERMISSION_USER_ACCESS') ?: new Permission(authority: 'PERMISSION_USER_ACCESS').save(flush: true)
         def adminPermission = Permission.findByAuthority('PERMISSION_ADMIN_ACCESS') ?: new Permission(authority: 'PERMISSION_ADMIN_ACCESS').save(flush: true)
//         println(adminRole.permissions.id as JSON)

         if (!adminRole.permissions) {
             adminRole.permissions = []
         }

         if (!adminRole.permissions.contains(adminPermission)) {
             adminRole.addToPermissions(adminPermission)
             adminRole.save(flush: true)
         }

         // Check and initialize the permissions collection if null
         if (!userRole.permissions) {
             userRole.permissions = []
         }

         if (!userRole.permissions.contains(userPermission)) {
             userRole.addToPermissions(userPermission)
             userRole.save(flush: true)
         }

         def adminUser = User.findByUsername('admin') ?: new User(username: 'admin', password: 'admin').save(flush: true)
         def normalUser = User.findByUsername('user') ?: new User(username: 'user', password: 'user').save(flush: true)

         if (!UserRole.exists(adminUser.id, adminRole.id)) {
             UserRole.create(adminUser, adminRole, true)
         }

         if (!UserRole.exists(normalUser.id, userRole.id)) {
             UserRole.create(normalUser, userRole, true)
         }
    }
}