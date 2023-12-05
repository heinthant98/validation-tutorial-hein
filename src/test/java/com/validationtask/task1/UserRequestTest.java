package com.validationtask.task1;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


public class UserRequestTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void usernameとpasswordとconfirmPasswordがnullの時にバリデーションエラーとなること() {
        UserRequest userRequest = new UserRequest(null, null, null);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("username", "ユーザー名を入力してください"), tuple("password", "パスワードを入力してください"));
    }

    @Test
    public void usernameとpasswordとconfirmPasswordが空文字の時にバリデーションエラーとなること() {
        UserRequest userRequest = new UserRequest("", "", "");
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("username", "ユーザー名は3文字以上20文字以下である必要があります"), tuple("password", "パスワードは8文字以上30文字以下である必要があります"));
    }

    @Test
    public void usernameが3文字未満の時にバリテーションエラーとなること() {
        UserRequest userRequest = new UserRequest("us", "password", "password");
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("username", "ユーザー名は3文字以上20文字以下である必要があります"));
    }

    @Test
    public void usernameが20文字以降入力された時にバリテーションエラーとなること() {
        UserRequest userRequest = new UserRequest("useruseruseruseruseruser", "password", "password");
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("username", "ユーザー名は3文字以上20文字以下である必要があります"));
    }

    @Test
    public void passwordが8文字未満入力された時にバリテーションエラーとなること() {
        UserRequest userRequest = new UserRequest("user", "pass", "pass");
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("password", "パスワードは8文字以上30文字以下である必要があります"));
    }

    @Test
    public void passwordが30文字以降入力された時にバリテーションエラーとなること() {
        UserRequest userRequest = new UserRequest("user", "passwordpasswordpasswordpassword", "passwordpasswordpasswordpassword");
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("password", "パスワードは8文字以上30文字以下である必要があります"));
    }

    @Test
    public void passwordとconfirmPasswordが一致していない時にバリテーションエラーとなること() {
        UserRequest userRequest = new UserRequest("user", "password", "passwdro");
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("passwordMatching", "パスワードと確認用パスワードが一致していません"));
    }
}
