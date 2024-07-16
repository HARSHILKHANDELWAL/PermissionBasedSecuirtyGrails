package spring


import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import security.CustomFilter
import security.CustomPermissionEvaluator
import security.UserPasswordEncoderListener

beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    customFilter(CustomFilter)
    customPermissionEvaluator(CustomPermissionEvaluator)
    methodSecurityExpressionHandler(DefaultMethodSecurityExpressionHandler) {
        permissionEvaluator = ref('customPermissionEvaluator')
    }
}