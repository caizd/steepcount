package com.saofenbao.jifenshangcheng.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.saofenbao.jifenshangcheng.domain.validator.OrderValidator;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME) 
@Constraint(validatedBy = OrderValidator.class)
public @interface ValidOrder {
	String message() default "订单验证失败。";  
    Class<?>[] groups() default {};  
    Class<? extends Payload>[] payload() default {}; 
}
