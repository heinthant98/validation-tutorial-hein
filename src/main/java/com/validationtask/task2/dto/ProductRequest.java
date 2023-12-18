package com.validationtask.task2.dto;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

public class ProductRequest {

    @NotBlank(message = "{E0001}")
    @Size(min = 2, max = 20, message = "{E0003}")
    private String productName;

    @NotBlank(message = "{E0001}")
    @ValidCategory
    private String category;

    @NotNull(message = "{E0002}")
    @Positive(message = "{E0004}")
    @Max(value = 1000000, message = "{E0005}")
    private Integer price;

    public ProductRequest(String productName, String category, Integer price) {
        this.productName = productName;
        this.category = category;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public Integer getPrice() {
        return price;
    }

    public enum Category {
        ELECTRONICS,
        CLOTHING,
        BOOKS;

        public static Category checkCategory(String value) {
            return Optional.of(Category.valueOf(value.toUpperCase())).orElseThrow(() -> new IllegalArgumentException());
        }
    }

    @Documented
    @Constraint(validatedBy = CategoryValidator.class)
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidCategory {
        String message() default "{ValidCategory}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    public static class CategoryValidator implements ConstraintValidator<ValidCategory, String> {
        @Override
        public void initialize(ValidCategory constraintAnnotation) {
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.isBlank()) {
                return true;
            }

            try {
                Category.checkCategory(value);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }

}
