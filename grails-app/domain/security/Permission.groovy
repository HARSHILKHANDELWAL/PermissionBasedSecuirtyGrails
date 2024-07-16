package security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@EqualsAndHashCode(includes='name')
@ToString(includes='name', includeNames=true, includePackage=false)
class Permission implements Serializable {

    private static final long serialVersionUID = 1

    String name
    String tenantId // Add tenantId field

    static constraints = {
        name nullable: false, blank: false
        tenantId nullable: false
    }

    static mapping = {
        cache true

    }
}