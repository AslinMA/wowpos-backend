package org.kmaihome.pos.service;

import org.kmaihome.pos.entity.RepairEntity;
import org.kmaihome.pos.models.Repair;
import java.time.LocalDateTime;
import java.util.List;

public interface RepairService {
    RepairEntity createRepair(Repair repair);
    List<RepairEntity> getAllRepairs();
    RepairEntity getRepairById(Integer id);
    List<RepairEntity> getRepairsByStatus(String status);
    List<RepairEntity> getRepairsByCustomerPhone(String phone);
    List<RepairEntity> getRepairsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    RepairEntity updateRepairStatus(Integer repairId, String status);
    Double getTotalRevenue();
    Double getTotalRevenueByStatus(String status);
    Long getCountByStatus(String status);
    void deleteRepair(Integer id);
    RepairEntity updateRepair(RepairEntity repair);

}
