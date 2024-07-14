package security

class Permission {
    String authority

    static mapping = {
        cache true
    }
//    static belongsTo = [role: Role]
    static constraints = {
        authority blank: false, unique: true
    }

}