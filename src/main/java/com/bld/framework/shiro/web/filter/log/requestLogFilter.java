package com.bld.framework.shiro.web.filter.log;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author SOFAS
 * @date 2020/6/17
 * @directions  数据请求接口日志
*/
@Slf4j
@WebFilter(urlPatterns = "/*",filterName = "channelFilter")
public class requestLogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String method = r.getMethod();
        String uri = r.getRequestURI();
//        cors请求通过
        if (RequestMethod.OPTIONS.name().equals(method) || r.getRequestURI().contains("logout.json")){
            res.setHeader("Access-control-Allow-Origin", r.getHeader("Origin"));
            res.setHeader("Access-Control-Allow-Methods", r.getMethod());
            res.setHeader("Access-Control-Allow-Credentials", "true");
            res.setHeader("Access-Control-Allow-Headers", r.getHeader("Access-Control-Request-Headers"));
            //防止乱码，适用于传输JSON数据
            res.setHeader("Content-Type","application/json;charset=UTF-8");
            res.setStatus(HttpStatus.OK.value());
        }else {
            r.setCharacterEncoding("UTF-8");
            Map<String, String[]> pm = r.getParameterMap();
            if (pm == null || pm.size() == 0){
                RequestWrapper r1 = new RequestWrapper((HttpServletRequest) request);
                log.info("*****************请求url：{}，请求类型：{}，请求参数：{}", uri, method, r1.getBody());
                chain.doFilter(r1, response);
                return;
            }
            log.info("*****************请求url：{}，请求类型：{}，请求参数：{}", uri, method, JSONObject.toJSONString(pm));
            chain.doFilter(request, response);
        }
    }
}
