package org.kmaihome.pos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairPart {
    private String partName;
    private String partCategory;
    private String partBrand;
    private String partModel;
    private Integer quantity;
    private Double unitPrice;
    private Double discount;  // ✅ ADDED
    private Double totalPrice;
}
