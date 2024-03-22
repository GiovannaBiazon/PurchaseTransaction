package com.brief.transaction.controller;

import java.util.HashMap;

import javax.naming.NameNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.brief.transaction.dto.PurchaseDTO;
import com.brief.transaction.service.PurchaseService;
import com.brief.transaction.vo.PurchaseVO;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Tag(name = "Purchase", description = "Purchase Transaction APIs")
@RestController
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success", content = { @Content(schema = @Schema(implementation = PurchaseDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Not found", content = { @Content(schema = @Schema(implementation = HashMap.class)) })
    })
    @GetMapping("/purchase/{id}/country-currency/{country-currency}")
    public ResponseEntity<?> retrievePurchase(
        @Parameter(required = true, description = "The id of the purchase operation") @PathVariable("id") @NonNull Long id,
        @Parameter(required = true, description = "The specific country's currency to convertion") @PathVariable("country-currency") String countryCurrency) {
            try {
                PurchaseDTO purchaseDTO = this.purchaseService.retrieveTransaction(id, countryCurrency);
                return new ResponseEntity<PurchaseDTO>(purchaseDTO, HttpStatus.OK);
            } catch (EntityNotFoundException | NameNotFoundException e) {
                HashMap<String, String> response = new HashMap<>();
                response.put("error", e.getMessage());
                return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.NOT_FOUND);
            }
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success", content = { @Content(schema = @Schema(implementation = HashMap.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "500", description = "Save entity error", content = { @Content(schema = @Schema(implementation = HashMap.class)) })
    })
    @PostMapping("/purchase")
    public ResponseEntity<?> newPurchase(@RequestBody @NonNull PurchaseVO purchaseVo) {
        HashMap<String, String> response = new HashMap<>();
        try {
            PurchaseVO saveResponse = this.purchaseService.newPurchase(purchaseVo);
            response.put("id", saveResponse.getId().toString());
            return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
