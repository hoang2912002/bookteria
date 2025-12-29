package com.hamet.profile.configuration;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;

public class AuthenticationRequestInterceptor implements RequestInterceptor {
    // Tự động add header global bằng interceptor
    // Nhưng do thực tế thì sẽ có nhiều service: local service hoặc third party service 
    // cho nên ko nên khai báo bean @Component ở đây để tránh lỗi cần phải config cho từng service cụ thể
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes requestAttributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        var authHeader = requestAttributes.getRequest().getHeader("Authorization");
        if(StringUtils.hasText(authHeader))
            template.header("Authorization", authHeader);
    }
    
}
