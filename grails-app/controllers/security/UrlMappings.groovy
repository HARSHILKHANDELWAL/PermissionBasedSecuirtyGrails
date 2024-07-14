package security

class UrlMappings {
    static mappings = {

        "/user"(action:"createUser",controller: "User",method: "GET")
        "/user/accessbyuser"(action:"accessByUser",controller: "User",method: "GET")
        "/user/accessbyadmin"(action:"accessByAdmin",controller: "User",method: "GET")
        "/user/verifyaccess"(action:"verifyAccess",controller: "User",method: "GET")


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
