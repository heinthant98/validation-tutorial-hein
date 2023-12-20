package com.validationtask.task2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.validationtask.task2.request.ProductRequest;
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

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void product登録時にデータが正しい場合201となること() throws Exception {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 500000);
        ResultActions result = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(productRequest)));

        result.andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "message": "successfully created"
                        } 
                        """));
    }

    @Test
    public void product登録にproductNameとcategoryとpriceがnullの場合は400エラーとなること() throws Exception {
        ProductRequest productRequest = new ProductRequest(null, null, null);
        ResultActions result = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(productRequest)));

        result.andExpect(status().isBadRequest())
                .andExpect(content().json("""
                         {
                           "httpStatus": "BAD_REQUEST",
                           "message": "validation error",
                           "errors": [
                             {
                               "field": "productName",
                               "message": [
                                 "入力してください"
                               ]
                             },
                             {
                               "field": "category",
                               "message": [
                                 "入力してください"
                               ]
                             },
                             {
                               "field": "price",
                               "message": [
                                 "入力してください"
                               ]
                             }
                           ]
                         }                     
                        """));
    }

    @Test
    public void product登録にproductNameとcategoryが空文字でpriceが0の場合400エラーになること() throws Exception {
        ProductRequest productRequest = new ProductRequest("", "", 0);
        ResultActions result = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(productRequest)));

        result.andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                          "httpStatus": "BAD_REQUEST",
                          "message": "validation error",
                          "errors": [
                            {
                              "field": "productName",
                              "message": [
                                "入力してください",
                                "2文字以上20文字以下である必要があります"
                              ]
                            },
                            {
                              "field": "category",
                              "message": [
                                "入力してください",
                                "無効なカテゴリです"
                              ]
                            },
                            {
                              "field": "price",
                              "message": [
                                "0より大きい値である必要があります"
                              ]
                            }
                          ]
                        }                        
                        """));
    }

}
