package com.validationtask.task2;

import com.validationtask.task2.request.ProductRequest;
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
            "5, Electronics, 200000",
            "10, electronics, 200000",
            "15, ELECTRONICS, 200000",
            "2, Electronics, 500000",
            "20, Electronics, 100000",
            "18, Electronics, 1000000",
            "18, Electronics, 1"
    })
    public void productNameとcategoryとpriceとsellerが有効な場合はバリデーションエラーとならないこと(int count, String category, Integer price) {
        ProductRequest product = new ProductRequest("p".repeat(count), category, price, "s".repeat(count));
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(0);
    }

    @Test
    public void productNameとcategoryとpriceとsellerがnullの場合バリデーションエラーとなること() {
        ProductRequest product = new ProductRequest(null, null, null, null);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(4);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"), tuple("category", "入力してください"),
                        tuple("price", "入力してください"), tuple("seller", "無効な販売者です"));
    }

    @Test
    public void productNameが空文字の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("", "Electronics", 100000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"), tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが半角スペースの場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest(" ", "Electronics", 100000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"), tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが2文字未満の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("p", "Electronics", 100000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが20文字を超えるとバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("p".repeat(21), "Electronics", 100000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void categoryが空文字の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPhone15", "", 300000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "入力してください"), tuple("category", "無効なカテゴリです"));
    }

    @Test
    public void categoryが半角スペースの場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPhone15", " ", 300000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "入力してください"), tuple("category", "無効なカテゴリです"));
    }

    @Test
    public void categoryに存在しない文字列の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPhone15", "Food", 300000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(Tuple.tuple("category", "無効なカテゴリです"));
    }

    @Test
    public void priceが0の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPhone15", "Electronics", 0, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("price", "0より大きい値である必要があります"));
    }

    @Test
    public void priceが1000001の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPhone15", "Electronics", 1000001, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("price", "1000000以下である必要があります"));
    }

    @Test
    public void sellerが空文字の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPad", "Electronics", 600000, "");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("seller", "無効な販売者です"));
    }

    @Test
    public void sellerが半角スペースの場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPad", "Electronics", 600000, " ");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("seller", "無効な販売者です"));
    }

    @Test
    public void sellerが2文字未満の場合はバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPad", "Electronics", 500000, "s");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("seller", "無効な販売者です"));
    }

    @ParameterizedTest
    @CsvSource({
            "10, Electronics, 400000, \uD842\uDFB7",
            "15, Electronics, 100000, \uD83E\uDEE0"
    })
    public void sellerに1文字のサロゲートペアの漢字と絵文字の場合はバリデーションエラーとならないこと(int count, String category, Integer price, String surrogatePairWord) {
        ProductRequest product = new ProductRequest("p".repeat(count), category, price, surrogatePairWord);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(0);
    }

    @Test
    public void sellerの文字数が20文字を超えるとバリデーションエラーとなること() {
        ProductRequest product = new ProductRequest("iPad", "Electronics", 500000, "s".repeat(21));
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("seller", "無効な販売者です"));
    }

    @ParameterizedTest
    @CsvSource({
            "19, electronics, 300000, \uD842\uDFB7",
            "19, electronics, 100000, \uD83E\uDEE0",
    })
    public void sellerに19文字のサロゲートペアの漢字と絵文字の場合はバリデーションエラーとなること(int count, String category, Integer price, String surrogatePairWord) {
        ProductRequest product = new ProductRequest("p".repeat(count), category, price, "s".repeat(count).concat(surrogatePairWord));
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactly(tuple("seller", "無効な販売者です"));
    }

}
