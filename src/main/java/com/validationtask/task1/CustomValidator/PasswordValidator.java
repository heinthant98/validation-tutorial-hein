package com.validationtask.task1.CustomValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@NotNull
@NotBlank
public @interface PasswordValidator {
    String message() default "パスワードを入力してください";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
