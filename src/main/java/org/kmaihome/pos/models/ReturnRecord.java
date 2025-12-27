package org.kmaihome.pos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRecord {
    private Integer returnId;
    private String saleTransactionId;
    private LocalDate originalSaleDate;
    private String customerName;
    private String customerPhone;

    private String returnedCategory;
    private String returnedBrand;
    private String returnedModel;
    private Integer returnedQuantity;

    private Integer replacementProductId;
    private String replacementCategory;
    private String replacementBrand;
    private String replacementModel;

    private String warrantyPeriod;
    private Boolean withinWarranty;
    private String claimType;
    private Double lossAmount;
    private String notes;
}
