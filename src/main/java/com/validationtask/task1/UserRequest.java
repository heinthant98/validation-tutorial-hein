package com.validationtask.task1;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "ユーザー名を入力してください")
    @Size(min = 3, max = 20, message = "ユーザー名は{min}文字以上{max}文字以下である必要があります")
    private String username;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, max = 30, message = "パスワードは{min}文字以上{max}文字以下である必要があります")
    private String password;

    private String confirmPassword;

    public UserRequest(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    @AssertTrue(message = "パスワードと確認用パスワードが一致していません")
    public boolean isPasswordMatching() {
        if (password == null || password.isEmpty() || password.equals(confirmPassword)) {
            return true;
        }
        return false;
    }

}
