package org.kmaihome.pos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Table(name = "repair_part")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairPartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_id")
    @JsonIgnore
    private RepairEntity repair;

    @Column(name = "part_name")
    private String partName;

    @Column(name = "part_category")
    private String partCategory;

    @Column(name = "part_brand")
    private String partBrand;

    @Column(name = "part_model")
    private String partModel;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "discount")
    private Double discount;  // ✅ ADDED

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (quantity != null && unitPrice != null) {
            // ✅ UPDATED: Include discount in calculation
            double subtotal = quantity * unitPrice;
            double discountAmount = (discount != null) ? discount : 0.0;
            totalPrice = subtotal - discountAmount;
        }
    }
}
