package com.validationtask.task1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void ユーザ登録時にデータが正当な場合は201となること() throws Exception {
        UserRequest user = new UserRequest("user", "password", "password");
        ResultActions actualResult = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(user)));
        actualResult.andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "message": "successfully created"
                        }
                        """));
    }

    @Test
    public void ユーザー登録時にusernameとpasswordとconfirmPasswordがnullの場合は400エラーとなること() throws Exception {
        UserRequest user = new UserRequest(null, null, null);
        ResultActions actualResult = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(user)));
        actualResult.andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                          "httpStatus": "BAD_REQUEST",
                          "message": "validation error",
                          "errors": [
                            {
                              "field": "username",
                              "message": [
                                "ユーザー名を入力してください"
                              ]
                            },
                            {
                              "field": "password",
                              "message": [
                                "パスワードを入力してください"
                              ]
                            }
                          ]
                        }
                        """));
    }

    @Test
    public void ユーザー登録時にusernameとpasswordとconfirmPasswordが空文字の場合は400エラーとなること() throws Exception {
        UserRequest user = new UserRequest("", "", "");
        ResultActions actualResult = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(user)));
        actualResult.andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                          "httpStatus": "BAD_REQUEST",
                          "message": "validation error",
                          "errors": [
                            {
                              "field": "username",
                              "message": [
                                "ユーザー名は3文字以上20文字以下である必要があります",
                                "ユーザー名を入力してください"
                              ]
                            },
                            {
                              "field": "password",
                              "message": [
                                "パスワードは8文字以上30文字以下である必要があります",
                                "パスワードを入力してください"
                              ]
                            }
                          ]
                        }
                        """));
    }
}
