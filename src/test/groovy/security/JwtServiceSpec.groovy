package security

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class JwtServiceSpec extends Specification implements ServiceUnitTest<JwtService> {

     void "test something"() {
        expect:
        service.doSomething()
     }
}
