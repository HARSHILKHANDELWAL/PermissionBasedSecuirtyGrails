package security

import grails.converters.JSON
import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic

@ToString(cache=true, includeNames=true, includePackage=false)
class RoleGroupRole implements Serializable {

	private static final long serialVersionUID = 1

	RoleGroup roleGroup
	Role role
	String tenantId

	@Override
	boolean equals(other) {
		if (other instanceof RoleGroupRole) {
			other.roleId == role?.id && other.roleGroupId == roleGroup?.id && other.tenantId == tenantId
		}
	}

	@Override
	int hashCode() {
		int hashCode = HashCodeHelper.initHash()
		if (roleGroup) {
			hashCode = HashCodeHelper.updateHash(hashCode, roleGroup.id)
		}
		if (role) {
			hashCode = HashCodeHelper.updateHash(hashCode, role.id)
		}
		if (tenantId) {
			hashCode = HashCodeHelper.updateHash(hashCode, tenantId)
		}
		hashCode
	}

	static RoleGroupRole get(long roleGroupId, long roleId, String tenantId) {
		criteriaFor(roleGroupId, roleId, tenantId).get()
	}

	static boolean exists(long roleGroupId, long roleId, String tenantId) {
		criteriaFor(roleGroupId, roleId, tenantId).count() > 0
	}

	private static DetachedCriteria<RoleGroupRole> criteriaFor(long roleGroupId, long roleId, String tenantId) {
		RoleGroupRole.where {
			roleGroup == RoleGroup.load(roleGroupId) &&
					role == Role.load(roleId) &&
					tenantId == tenantId
		}
	}

	static RoleGroupRole create(RoleGroup roleGroup, Role role, String tenantId, boolean flush = false) {
		def instance = new RoleGroupRole(roleGroup: roleGroup, role: role, tenantId: tenantId)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(RoleGroup rg, Role r, String tenantId) {
		if (rg != null && r != null) {
			RoleGroupRole.where { roleGroup == rg && role == r && tenantId == tenantId }.deleteAll()
		}
	}

	static int removeAll(Role r, String tenantId) {
		r == null ? 0 : RoleGroupRole.where { role == r && tenantId == tenantId }.deleteAll() as int
	}

	static int removeAll(RoleGroup rg, String tenantId) {
		rg == null ? 0 : RoleGroupRole.where { roleGroup == rg && tenantId == tenantId }.deleteAll() as int
	}

	static constraints = {
		roleGroup nullable: false
		role nullable: false, validator: { Role r, RoleGroupRole rg ->
			if (rg.roleGroup?.id) {
				if (RoleGroupRole.exists(rg.roleGroup.id, r.id, rg.tenantId)) {
					return ['roleGroupRole.exists']
				}
			}
		}
		tenantId nullable: false
	}

	static mapping = {
		id composite: ['roleGroup', 'role', 'tenantId']
	}
}