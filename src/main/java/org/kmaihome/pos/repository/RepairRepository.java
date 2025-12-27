package org.kmaihome.pos.repository;

import org.kmaihome.pos.entity.RepairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<RepairEntity, Integer> {

    List<RepairEntity> findByStatus(String status);

    List<RepairEntity> findByCustomerPhone(String phone);  // ✅ ADD THIS

    List<RepairEntity> findByRepairDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(r.totalCost) FROM RepairEntity r")
    Double getTotalRevenue();

    @Query("SELECT SUM(r.totalCost) FROM RepairEntity r WHERE r.status = :status")
    Double getTotalRevenueByStatus(String status);

    Long countByStatus(String status);
}
