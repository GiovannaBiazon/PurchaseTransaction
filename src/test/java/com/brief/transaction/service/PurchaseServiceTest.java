package com.brief.transaction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import javax.naming.NameNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.brief.transaction.client.RateClient;
import com.brief.transaction.dto.FiscalApiDataDTO;
import com.brief.transaction.dto.FiscalApiResponseDTO;
import com.brief.transaction.dto.PurchaseDTO;
import com.brief.transaction.repository.PurchaseRepository;
import com.brief.transaction.vo.PurchaseVO;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private RateClient rateClient;

    @InjectMocks
    private PurchaseService purchaseService;

    private PurchaseVO purchaseVO;
    private FiscalApiResponseDTO fiscalApiResponseDTO;

    @BeforeEach
    void setup() {
        purchaseVO = PurchaseVO.builder()
                .id(1L)
                .description("Teste")
                .transactionDate(LocalDate.now())
                .purchaseAmount(BigDecimal.TEN)
                .build();

        FiscalApiDataDTO fiscalApiDataDTO = FiscalApiDataDTO.builder()
                .country("Brazil")
                .currency("Real")
                .country_currency_desc("Brazil-Real")
                .effective_date(String.valueOf(LocalDate.now()))
                .exchange_rate("4.8")
                .record_date(String.valueOf(LocalDate.now()))
                .build();

        ArrayList<FiscalApiDataDTO> fiscalApiDatas = new ArrayList<FiscalApiDataDTO>();
        fiscalApiDatas.add(fiscalApiDataDTO);

        fiscalApiResponseDTO = FiscalApiResponseDTO.builder()
                .data(fiscalApiDatas)
                .build();
    }

    @SuppressWarnings("null")
    @Test
    void givenPurchaseObject_whenSavePurchase_thenReturnPurchaseObject() {
        assertNotNull(purchaseVO);

        when(purchaseRepository.save(purchaseVO)).thenReturn(purchaseVO);
        PurchaseVO savedPurchaseVO = purchaseRepository.save(purchaseVO);
        assertNotNull(savedPurchaseVO.getId());
    }

    @Test
    void testRetrieveTransaction() throws NameNotFoundException {

        FiscalApiDataDTO countryCurrenciesData = FiscalApiDataDTO.builder()
                .country_currency_desc("Brazil-Real")
                .build();
        ArrayList<FiscalApiDataDTO> list = new ArrayList<>();
        list.add(countryCurrenciesData);
        FiscalApiResponseDTO countryCurrencies = FiscalApiResponseDTO.builder()
                .data(list)
                .build();

        assertNotNull(purchaseVO);
        assertEquals(list.size(), 1);

        when(purchaseRepository.findById(1L)).thenReturn(Optional.of(purchaseVO));
        when(rateClient.getCountryCurrencies()).thenReturn(countryCurrencies);
        when(rateClient.getRate("Brazil-Real",
                purchaseVO.getTransactionDate().minusMonths(6).format(DateTimeFormatter.ISO_LOCAL_DATE),
                purchaseVO.getTransactionDate().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .thenReturn(fiscalApiResponseDTO);

        PurchaseDTO purchaseDTO = purchaseService.retrieveTransaction(1L, "Brazil-Real");

        assertEquals(
                purchaseDTO.getExchangeRate(),
                new BigDecimal(fiscalApiResponseDTO.getData().get(0).getExchange_rate()));
        assertEquals(
                purchaseDTO.getConvertedAmount(),
                purchaseVO.getPurchaseAmount()
                        .multiply(
                                new BigDecimal(fiscalApiResponseDTO.getData().get(0).getExchange_rate()).setScale(2)));
    }
}
