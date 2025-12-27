package org.kmaihome.pos.models;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    private Integer id;
    private LocalDate date;
    private String category;
    private String brand;
    private String model;
    private Integer quantity;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
    private String location;
    private Boolean isActive;
}
