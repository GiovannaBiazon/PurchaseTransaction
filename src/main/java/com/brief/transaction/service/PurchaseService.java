package com.brief.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

import javax.naming.NameNotFoundException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.brief.transaction.client.RateClient;
import com.brief.transaction.dto.FiscalApiDataDTO;
import com.brief.transaction.dto.FiscalApiResponseDTO;
import com.brief.transaction.dto.PurchaseDTO;
import com.brief.transaction.repository.PurchaseRepository;
import com.brief.transaction.vo.PurchaseVO;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final RateClient rateClient;

    public PurchaseVO newPurchase(@NonNull PurchaseVO purchaseVo) {
        return this.purchaseRepository.save(purchaseVo);
    }

    public PurchaseDTO retrieveTransaction(@NonNull Long id, String countryCurrency) throws NameNotFoundException {
        if (checkCountryCurrency(countryCurrency)) {
            PurchaseVO purchaseVO = this.purchaseRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Purchase transaction for id " + id + " not found"));
            PurchaseDTO purchaseDTO = new PurchaseDTO(purchaseVO);
            FiscalApiResponseDTO fiscalRate = rateClient.getRate(countryCurrency,
                    purchaseDTO.getTransactionDate().minusMonths(6).format(DateTimeFormatter.ISO_LOCAL_DATE),
                    purchaseDTO.getTransactionDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            FiscalApiDataDTO convertionRate = fiscalRate.getData().stream()
                    .sorted(Comparator.comparingLong(
                            x -> Math.abs(
                                    ChronoUnit.DAYS.between(
                                            LocalDate.parse(x.getRecord_date()),
                                            purchaseDTO.getTransactionDate()))))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Purchase cannot be converted to target currency"));
            purchaseDTO.setExchangeRate(new BigDecimal(convertionRate.getExchange_rate()));
            purchaseDTO.setConvertedAmount(purchaseDTO.getExchangeRate().multiply(purchaseDTO.getOriginalPurchaseAmount()));
            return purchaseDTO;
        } else {
            throw new NameNotFoundException( countryCurrency + "not available at fiscal data");
        }
    }

    private boolean checkCountryCurrency(String countryCurrency) {
        FiscalApiResponseDTO apiResponseCountryCurrencies = rateClient.getCountryCurrencies();
        return apiResponseCountryCurrencies.getData().stream().anyMatch(cc -> cc.getCountry_currency_desc() == countryCurrency);
    }
}
