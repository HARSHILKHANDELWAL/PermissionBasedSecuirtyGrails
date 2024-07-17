package security


import grails.gorm.transactions.Transactional

@Transactional
class UserService {

     def createUser(String username, String password,String role) {
         def tenantId='UNO_T2'
         println(Role.findByAuthorityAndTenantId(role,tenantId))
         println(Role.findByAuthority(role))
        def userRole = Role.findByAuthorityAndTenantId(role,tenantId) ?: new Role(authority: role,tenantId: 'UNO_T2').save(flush: true)
         println(userRole)
        def user = User.findByUsername(username) ?: new User(username: username, password: password).save(flush: true)
        if (!UserRole.exists(user.id,userRole.id,"UNO_T2","Uno_BrandMaker")) {
            UserRole.create(user, userRole,"UNO_T2","Uno_BrandMaker" ,true)
        }

    }
}