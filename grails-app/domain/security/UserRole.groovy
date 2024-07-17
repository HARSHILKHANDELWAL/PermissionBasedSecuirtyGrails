package security

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic

@ToString(cache=true, includeNames=true, includePackage=false)
class UserRole implements Serializable {

	private static final long serialVersionUID = 1

	User user
	Role role
	String tenantId
	String serviceId

	@Override
	boolean equals(other) {
		if (other instanceof UserRole) {
			other.userId == user?.id && other.roleId == role?.id && other.tenantId == tenantId && other.serviceId == serviceId
		}
	}

	@Override
	int hashCode() {
		int hashCode = HashCodeHelper.initHash()
		if (user) {
			hashCode = HashCodeHelper.updateHash(hashCode, user.id)
		}
		if (role) {
			hashCode = HashCodeHelper.updateHash(hashCode, role.id)
		}
		if (tenantId) {
			hashCode = HashCodeHelper.updateHash(hashCode, tenantId)
		}
		if (serviceId) {
			hashCode = HashCodeHelper.updateHash(hashCode, serviceId)
		}
		hashCode
	}

	static UserRole get(long userId, long roleId, String tenantId, String serviceId) {
		criteriaFor(userId, roleId, tenantId, serviceId).get()
	}

	static boolean exists(long userId, long roleId, String tenantId, String serviceId) {
		criteriaFor(userId, roleId, tenantId, serviceId).count() > 0
	}

	private static DetachedCriteria<UserRole> criteriaFor(long userId, long roleId, String tenantId, String serviceId) {
		UserRole.where {
			user == User.load(userId) &&
					role == Role.load(roleId) &&
					tenantId == tenantId &&
					serviceId == serviceId
		}
	}

	static UserRole create(User user, Role role, String tenantId, String serviceId, boolean flush = false) {
		def instance = new UserRole(user: user, role: role, tenantId: tenantId, serviceId: serviceId)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(User u, Role r, String tenantId, String serviceId) {
		if (u != null && r != null) {
			UserRole.where { user == u && role == r && tenantId == tenantId && serviceId == serviceId }.deleteAll()
		}
	}

	static int removeAll(User u, String tenantId, String serviceId) {
		u == null ? 0 : UserRole.where { user == u && tenantId == tenantId && serviceId == serviceId }.deleteAll() as int
	}

	static int removeAll(Role r, String tenantId, String serviceId) {
		r == null ? 0 : UserRole.where { role == r && tenantId == tenantId && serviceId == serviceId }.deleteAll() as int
	}

	static constraints = {
		user nullable: false
		role nullable: false, validator: { Role r, UserRole ur ->
			if (ur.user?.id) {
				if (UserRole.exists(ur.user.id, r.id, ur.tenantId, ur.serviceId)) {
					return ['userRole.exists']
				}
			}
		}
		tenantId nullable: false
		serviceId nullable: false
	}

	static mapping = {
		id composite: ['user', 'role', 'tenantId', 'serviceId']
	}
}