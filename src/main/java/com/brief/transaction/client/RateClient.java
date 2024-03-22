package com.brief.transaction.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

import com.brief.transaction.dto.FiscalApiResponseDTO;

@Service
public class RateClient {

    private RestClient restClient;

    RateClient() {
        this.restClient = RestClient.create(
                "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange");
    }

    public FiscalApiResponseDTO getCountryCurrencies() {
        ResponseSpec response = this.restClient.get().uri("?fields=country_currency_desc&page[size]=1000&format=json")
                .retrieve();
        return response.body(FiscalApiResponseDTO.class);
    }

    public FiscalApiResponseDTO getRate(String countryCurrency, String monthsFromTransaction, String transactionDate) {
        ResponseSpec response = this.restClient
                .get()
                .uri("?filter=country_currency_desc:eq:" + countryCurrency + ",record_date:gt:" + monthsFromTransaction
                        + ",record_date:lte:" + transactionDate)
                .retrieve();
        return response.body(FiscalApiResponseDTO.class);
    }
}
