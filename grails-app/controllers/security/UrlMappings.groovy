package security

class UrlMappings {
    static mappings = {


        "/user/accessbyuser"(action:"accessByUser",controller: "User",method: "GET")
        "/user/accessbyadmin"(action:"accessByAdmin",controller: "User",method: "GET")
        "/user/verifyaccess"(action:"verifyAccess",controller: "User",method: "GET")
        "/user"(action:"createUser",controller: "User",method: "POST")
        "/creategroup"(action:"createRoleGroup",controller: "User",method: "POST")
        "/assignrole"(action:"assignRoleToGroup",controller: "User",method: "POST")
        "/assignpermission"(action:"assignPermission",controller: "User",method: "POST")
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')

    }
}