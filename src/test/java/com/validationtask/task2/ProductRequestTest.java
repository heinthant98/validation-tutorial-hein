package com.validationtask.task2;

import com.validationtask.task2.dto.ProductRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


public class ProductRequestTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "iPhone15, Electronics, 200000",
            "iPhone15, electronics, 200000",
            "iPhone15, ELECTRONICS, 200000",
            "PC, Electronics, 500000",
            "Macbook Pro, Electronics, 1000000"
    })
    public void 有効なproductNameとcategoryとprice場合はバリデーションエラーとならないこと(String productName, String category, Integer price) {
        ProductRequest product = new ProductRequest(productName, category, price);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(0);
    }

    @Test
    public void productNameとcategoryとpriceがnullの場合バリデーションエラーとなること() {
        ProductRequest product = new ProductRequest(null, null, null);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(3);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"), tuple("category", "入力してください"), tuple("price", "入力してください"));
    }

    @Test
    public void productNameが空文字の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("", "Electronics", 100000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"), tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが半角スペースの場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest(" ", "Electronics", 100000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"), tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが2文字未満の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("p", "Electronics", 100000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが20文字の場合はバリデーションエラーとならないこと() {
        ProductRequest product = new ProductRequest("p".repeat(20), "Electronics", 100000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(0);
    }

    @Test
    public void productNameが20文字を超えるとバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("p".repeat(21), "Electronics", 100000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void categoryが空文字の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPhone15", "", 300000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "入力してください"), tuple("category", "無効なカテゴリです"));
    }

    @Test
    public void categoryが半角スペースの場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPhone15", " ", 300000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "入力してください"), tuple("category", "無効なカテゴリです"));
    }

    @Test
    public void categoryに存在しない文字列の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPhone15", "Food", 300000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(Tuple.tuple("category", "無効なカテゴリです"));
    }

    @Test
    public void priceが0の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPhone15", "Electronics", 0);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("price", "0より大きい値である必要があります"));
    }

    @Test
    public void priceが1000001の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPhone15", "Electronics", 1000001);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("price", "1000000以下である必要があります"));
    }

}
