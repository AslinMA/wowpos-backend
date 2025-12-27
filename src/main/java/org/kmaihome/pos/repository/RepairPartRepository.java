package org.kmaihome.pos.repository;

import org.kmaihome.pos.entity.RepairPartEntity;  // ✅ Use ENTITY, not Model!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairPartRepository extends JpaRepository<RepairPartEntity, Integer> {

    // Find all parts for a specific repair
    List<RepairPartEntity> findByRepairRepairId(Integer repairId);

    // Find parts by name
    List<RepairPartEntity> findByPartName(String partName);

    // Find parts by category
    List<RepairPartEntity> findByPartCategory(String category);
}
