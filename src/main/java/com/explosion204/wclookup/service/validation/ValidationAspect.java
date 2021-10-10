package com.explosion204.wclookup.service.validation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Set;

@Aspect
@Component
public class ValidationAspect {
    private final Validator validator;

    public ValidationAspect(Validator validator) {
        this.validator = validator;
    }

    @Before("@annotation(DtoValidation)")
    public void validationAdvice(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> {
            Set<ConstraintViolation<Object>> errors = validator.validate(arg);

            if (!errors.isEmpty()) {
                throw new ConstraintViolationException(errors);
            }
        });
    }
}
