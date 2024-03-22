package com.brief.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.brief.transaction.vo.PurchaseVO;

public class PurchaseDTO {

    private Long id;
    private String description;
    private LocalDate transactionDate;
    private BigDecimal originalPurchaseAmount;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;

    public PurchaseDTO() {}

    public PurchaseDTO(PurchaseVO purchaseVo) {
        this.id = purchaseVo.getId();
        this.description = purchaseVo.getDescription();
        this.originalPurchaseAmount = purchaseVo.getPurchaseAmount().setScale(2);
        this.transactionDate = purchaseVo.getTransactionDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getOriginalPurchaseAmount() {
        return originalPurchaseAmount;
    }

    public void setOriginalPurchaseAmount(BigDecimal originalPurchaseAmount) {
        this.originalPurchaseAmount = originalPurchaseAmount.setScale(2);
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount.setScale(2);
    }
}
