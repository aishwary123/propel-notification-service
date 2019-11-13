package com.notification.propel.configs.tenancy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.notification.propel.rest.clients.PropelAPIRestClient;
import com.notification.propel.utils.JwtTokenUtil;
import com.notification.propel.utils.JwtTokenUtil.JWTTokenPayload;

@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private PropelAPIRestClient propelAPIRestClient;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler)
        throws Exception {
        String authToken = request.getHeader(this.tokenHeader);
        if (StringUtils.isEmpty(authToken)) {
            return true;
        }
        authToken = authToken.split(" ")[1];
        JWTTokenPayload tokenPayload = JwtTokenUtil.parseJwtToken(authToken);
        final String shardId = propelAPIRestClient.getShardIdFromOrgId(
                    tokenPayload.getCurrentOrgId());
        TenantContext.setCurrentTenant(shardId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView)
        throws Exception {
        TenantContext.clear();
    }
}