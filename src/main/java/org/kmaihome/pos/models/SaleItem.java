package org.kmaihome.pos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleItem {
    private Integer productId;
    private String category;
    private String brand;
    private String model;
    private Integer quantity;
    private BigDecimal sellPrice;        // Unit price (original)
    private BigDecimal discountedPrice;  // Final price per unit after discount
}
