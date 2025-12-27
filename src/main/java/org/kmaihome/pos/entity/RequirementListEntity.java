// src/main/java/org/kmaihome/pos/entity/RequirementListEntity.java
package org.kmaihome.pos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "requirement_list")
public class RequirementListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "require_date", nullable = false)
    private LocalDate requireDate;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequirementItemEntity> items = new ArrayList<>();

    // convenience method
    public void addItem(RequirementItemEntity item) {
        item.setRequirement(this);   // ✅ now compiles
        items.add(item);
    }
}
