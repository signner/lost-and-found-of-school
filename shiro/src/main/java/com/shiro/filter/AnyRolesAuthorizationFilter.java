package com.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class AnyRolesAuthorizationFilter extends RolesAuthorizationFilter {

    public AnyRolesAuthorizationFilter() {
    }

    @Override
    public boolean isAccessAllowed(ServletRequest request,
                                   ServletResponse response, Object mappedValue) throws IOException {

        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            // no roles specified, so nothing to check - allow access.
            return true;
        }

        for (String roleName : rolesArray) {
//            System.out.println("role------------>" + roleName);
            if (subject.hasRole(roleName)) {
                return true;
            }
        }

        return false;
    }
}