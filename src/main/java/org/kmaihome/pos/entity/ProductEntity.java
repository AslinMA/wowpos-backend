package org.kmaihome.pos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // was String
    private LocalDate date;

    private String category;
    private String brand;
    private String model;

    // was String
    @Column(nullable = false)
    private Integer quantity;

    // was String
    @Column(name = "buy_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal buyPrice;

    // was String
    @Column(name = "sell_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal sellPrice;

    private String location;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;


}
