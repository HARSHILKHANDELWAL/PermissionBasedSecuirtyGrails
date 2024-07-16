package security

import grails.gorm.DetachedCriteria
import groovy.transform.ToString
import org.codehaus.groovy.util.HashCodeHelper

@ToString(cache=true, includeNames=true, includePackage=false)
class RoleGroupPermission implements Serializable {

    private static final long serialVersionUID = 1

    RoleGroup roleGroup
    Permission permission

    @Override
    boolean equals(other) {
        if (other instanceof RoleGroupPermission) {
            other.permissionId == permission?.id && other.roleGroupId == roleGroup?.id
        }
    }

    @Override
    int hashCode() {
        int hashCode = HashCodeHelper.initHash()
        if (roleGroup) {
            hashCode = HashCodeHelper.updateHash(hashCode, roleGroup.id)
        }
        if (permission) {
            hashCode = HashCodeHelper.updateHash(hashCode, permission.id)
        }
        hashCode
    }

    static RoleGroupPermission get(long roleGroupId, long permissionId) {
        criteriaFor(roleGroupId, permissionId).get()
    }

    static boolean exists(long roleGroupId, long permissionId) {
        criteriaFor(roleGroupId, permissionId).count()
    }

    private static DetachedCriteria<RoleGroupPermission> criteriaFor(long roleGroupId, long permissionId) {
        RoleGroupPermission.where {
            roleGroup == RoleGroup.load(roleGroupId) &&
                    permission == Permission.load(permissionId)
        }
    }

    static RoleGroupPermission create(RoleGroup roleGroup, Permission permission, boolean flush = false) {
        def instance = new RoleGroupPermission(roleGroup: roleGroup, permission: permission)
//        instance.tenantId = permission.tenantId // Assign tenantId from permission
        instance.save(flush: flush)
        instance
    }

    static boolean remove(RoleGroup rg, Permission p) {
        if (rg != null && p != null) {
            RoleGroupPermission.where { roleGroup == rg && permission == p }.deleteAll()
        }
    }

    static int removeAll(Permission p) {
        p == null ? 0 : RoleGroupPermission.where { permission == p }.deleteAll() as int
    }

    static int removeAll(RoleGroup rg) {
        rg == null ? 0 : RoleGroupPermission.where { roleGroup == rg }.deleteAll() as int
    }

    static constraints = {
        roleGroup nullable: false
        permission nullable: false, validator: { Permission p, RoleGroupPermission rg ->
            if (rg.roleGroup?.id) {
                if (RoleGroupPermission.exists(rg.roleGroup.id, p.id)) {
                    return ['roleGroup.exists']
                }
            }
        }
    }

    static mapping = {
        id composite: ['roleGroup', 'permission']
    }
}