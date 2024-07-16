package security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class Role implements Serializable {

	private static final long serialVersionUID = 1

	String authority
    String tenantId
	static constraints = {
		authority nullable: false, blank: false
		tenantId nullable:false
	}

	static mapping = {
		cache true

	}
}