package org.kmaihome.pos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class saleDetailsForDisaply {
    private Integer id;
    private Integer saleId;
    private String transactionId;
    private String name;
    private String phoneNumber;
    private String date;
    private String discountedPrice;
    private String sellPrice;
    private String buyPrice;        // ✅ NEW FIELD
    private String category;
    private String brand;
    private String model;
    private String quantity;

    // ✅ Constructor for JPQL query
    public saleDetailsForDisaply(
            Integer id,
            Integer saleId,
            String transactionId,
            String name,
            String phoneNumber,
            String date,
            String discountedPrice,
            String sellPrice,
            BigDecimal buyPrice,        // From ProductEntity
            String category,
            String brand,
            String model,
            String quantity
    ) {
        this.id = id;
        this.saleId = saleId;
        this.transactionId = transactionId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.discountedPrice = discountedPrice;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice != null ? buyPrice.toString() : "0";
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.quantity = quantity;
    }
}
