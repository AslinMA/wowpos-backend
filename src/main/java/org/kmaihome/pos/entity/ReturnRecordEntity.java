package org.kmaihome.pos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "return_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_id")
    private Integer returnId;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "sale_transaction_id")
    private String saleTransactionId;

    @Column(name = "original_sale_date")
    private LocalDate originalSaleDate;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "returned_category")
    private String returnedCategory;

    @Column(name = "returned_brand")
    private String returnedBrand;

    @Column(name = "returned_model")
    private String returnedModel;

    @Column(name = "returned_quantity")
    private Integer returnedQuantity;

    @Column(name = "replacement_product_id")
    private Integer replacementProductId;

    @Column(name = "replacement_category")
    private String replacementCategory;

    @Column(name = "replacement_brand")
    private String replacementBrand;

    @Column(name = "replacement_model")
    private String replacementModel;

    @Column(name = "warranty_period")
    private String warrantyPeriod;

    @Column(name = "within_warranty")
    private Boolean withinWarranty;

    @Column(name = "claim_type")
    private String claimType;  // "Supplier Claim" or "My Loss"

    @Column(name = "loss_amount")
    private Double lossAmount;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        returnDate = LocalDateTime.now();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
