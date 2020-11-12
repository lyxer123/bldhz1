package com.bld.framework.shiro.web.filter.cros;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author SOFAS
 * @date 2020/6/23
 * @directions  cros跨域支持
*/
@SpringBootApplication
@Configuration
public class CorsFilter implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //System.out.println("assd");
        //所有方法
        registry.addMapping("/**")
                //允许的域名
                .allowedOrigins("*")
                // 允许请求头
                .allowedHeaders("*")
                //允许方法
                .allowedMethods("GET", "POST", "DELETE", "PUT","OPTIONS")
                //表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
                .maxAge(3600)
                //.mediaType("json", MediaType.APPLICATION_JSON)
                .allowCredentials(true);
    }
}
