package com.bld.framework.aspectj;

import com.bld.common.exception.BldException;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.project.system.user.domain.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author SOFAS
 * @date 2020/5/7
 * @directions  检查是否有thingsboard 的TOKEN
 */
@Aspect
@Component
public class TokenNotNullAspect {
    @Pointcut("@annotation(com.bld.framework.aspectj.lang.annotation.TokenNotNull)")
   public void tokenNotNullPointCut(){ }

   @Before("tokenNotNullPointCut()")
    public void doBefore() throws Throwable {
       User sysUser = ShiroUtils.getSysUser();
       if (sysUser == null){
           throw new BldException("请重新登录");
       }
   }
}
