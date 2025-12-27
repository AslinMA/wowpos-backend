package org.kmaihome.pos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sale")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Integer saleId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "category")
    private String category;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sell_price")
    private BigDecimal sellPrice;

    @Column(name = "discounted_price")
    private BigDecimal discountedPrice;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "warranty_period")
    private String warrantyPeriod;

    // ❌ REMOVE THESE IF YOU DON'T WANT TO ADD COLUMNS TO DATABASE
    // @Column(name = "created_at", updatable = false)
    // private LocalDateTime createdAt;
    //
    // @Column(name = "updated_at")
    // private LocalDateTime updatedAt;
    //
    // @PrePersist
    // protected void onCreate() {
    //     createdAt = LocalDateTime.now();
    //     updatedAt = LocalDateTime.now();
    // }
    //
    // @PreUpdate
    // protected void onUpdate() {
    //     updatedAt = LocalDateTime.now();
    // }
}
