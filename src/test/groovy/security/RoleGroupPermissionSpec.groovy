package security

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class RoleGroupPermissionSpec extends Specification implements DomainUnitTest<RoleGroupPermission> {

     void "test domain constraints"() {
        when:
        RoleGroupPermission domain = new RoleGroupPermission()
        //TODO: Set domain props here

        then:
        domain.validate()
     }
}
