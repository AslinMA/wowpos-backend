package org.kmaihome.pos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Damage {
    private Integer damageId;
    private Integer productId;
    private String category;
    private String brand;
    private String model;
    private Integer quantity;
    private String damageType;
    private String damageReason;
    private Double lossAmount;
}
