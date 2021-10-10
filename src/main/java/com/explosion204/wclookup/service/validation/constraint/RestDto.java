package com.explosion204.wclookup.service.validation.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

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
