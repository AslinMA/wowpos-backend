package org.kmaihome.pos.models;

import lombok.Data;

@Data
public class LowStockItem {
    private Integer id;
    private String category;
    private String brand;
    private String model;
    private Integer quantity;
    private String band; // CRITICAL, WARN, INFO, LOW
}
