package security


import grails.gorm.transactions.Transactional

@Transactional
class UserService {

     def createUser(String username, String password) {
        def userRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN',tenantId: 'UNO_T1').save(flush: true)
        def user = User.findByUsername(username) ?: new User(username: username, password: password).save(flush: true)
        if (!user.authorities.contains(userRole)) {
            UserRole.create(user, userRole, true)
        }

    }
}