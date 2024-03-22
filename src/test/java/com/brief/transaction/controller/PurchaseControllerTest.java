package com.brief.transaction.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.brief.transaction.service.PurchaseService;
import com.brief.transaction.vo.PurchaseVO;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PurchaseController.class)
public class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PurchaseService purchaseService;

    private PurchaseVO purchaseVO;

    @BeforeEach
    void setup() {
        purchaseVO = new PurchaseVO(1L, "Teste", LocalDate.now(), BigDecimal.TEN);
    }

    @SuppressWarnings("null")
    @Test
    void whenValidInput_registerPurchaseTransaction() throws Exception {

        assertNotNull(purchaseVO);

        when(purchaseService.newPurchase(any(PurchaseVO.class))).thenReturn(purchaseVO);

        mockMvc.perform(MockMvcRequestBuilders.post("/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseVO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }
}
