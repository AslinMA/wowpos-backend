package org.kmaihome.pos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "damage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DamageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "damage_id")
    private Integer damageId;

    @Column(name = "damage_date")
    private LocalDateTime damageDate;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "category")
    private String category;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "damage_type")
    private String damageType;  // "Supplier Issue" or "My Fault"

    @Column(name = "damage_reason", columnDefinition = "TEXT")
    private String damageReason;

    @Column(name = "loss_amount")
    private Double lossAmount;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        damageDate = LocalDateTime.now();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
