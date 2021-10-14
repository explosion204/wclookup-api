package com.explosion204.wclookup.service.validation.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = RestDtoValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestDto {
    Class<?>[] groups() default {};
    // I do not like this hardcode!!! =(
    String message() default "during creation entity must not have any null properties";
    Class<? extends Payload>[] payload() default {};
}
