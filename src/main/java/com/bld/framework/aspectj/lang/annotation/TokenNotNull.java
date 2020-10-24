package com.bld.framework.aspectj.lang.annotation;

import java.lang.annotation.*;

/**
 * @author SOFAS
 * @date 2020/5/7
 * @directions  检查是否有thingsboard 的TOKEN
*/
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenNotNull {
}
