package com.brief.transaction.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PurchaseVO {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 50, min = 1, message = "Provide a purchase description")
    @Column(length = 50, nullable = false)
    private String description;

    @NotBlank
    @Column(name = "transaction-date")
    private LocalDate transactionDate;

    @NotBlank
    @Column(name = "purchase-amount", scale = 2)
    private BigDecimal purchaseAmount;
}
