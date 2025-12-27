package org.kmaihome.pos.service;

import org.kmaihome.pos.entity.RepairEntity;
import org.kmaihome.pos.entity.RepairPartEntity;
import org.kmaihome.pos.models.Repair;
import org.kmaihome.pos.models.RepairPart;
import org.kmaihome.pos.repository.RepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepairServiceImpl implements RepairService {

    @Autowired
    private RepairRepository repairRepository;

    @Override
    @Transactional
    public RepairEntity createRepair(Repair repair) {
        double partsTotal = 0.0;

        // Create repair entity
        RepairEntity repairEntity = RepairEntity.builder()
                .customerName(repair.getCustomerName())
                .customerPhone(repair.getCustomerPhone())
                .deviceBrand(repair.getDeviceBrand())
                .deviceModel(repair.getDeviceModel())
                .issueDescription(repair.getIssueDescription())
                .laborCharge(repair.getLaborCharge() != null ? repair.getLaborCharge() : 0.0)
                .status(repair.getStatus() != null ? repair.getStatus() : "Pending")
                .parts(new ArrayList<>())
                .build();

        // Process parts
        if (repair.getParts() != null && !repair.getParts().isEmpty()) {
            for (RepairPart part : repair.getParts()) {
                // ✅ UPDATED: Calculate total with discount
                double subtotal = part.getQuantity() * part.getUnitPrice();
                double discount = (part.getDiscount() != null) ? part.getDiscount() : 0.0;
                double total = subtotal - discount;

                RepairPartEntity partEntity = RepairPartEntity.builder()
                        .repair(repairEntity)
                        .partName(part.getPartName())
                        .partCategory(part.getPartCategory())
                        .partBrand(part.getPartBrand())
                        .partModel(part.getPartModel())
                        .quantity(part.getQuantity())
                        .unitPrice(part.getUnitPrice())
                        .discount(discount)  // ✅ ADDED
                        .totalPrice(total)   // ✅ UPDATED
                        .build();

                repairEntity.getParts().add(partEntity);
                partsTotal += partEntity.getTotalPrice();
            }
        }

        // Calculate total cost
        repairEntity.setTotalCost(repairEntity.getLaborCharge() + partsTotal);

        return repairRepository.save(repairEntity);
    }

    @Override
    public List<RepairEntity> getAllRepairs() {
        return repairRepository.findAll();
    }

    @Override
    public RepairEntity getRepairById(Integer id) {
        return repairRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repair not found with ID: " + id));
    }

    @Override
    public List<RepairEntity> getRepairsByStatus(String status) {
        return repairRepository.findByStatus(status);
    }

    @Override
    public List<RepairEntity> getRepairsByCustomerPhone(String phone) {
        return repairRepository.findByCustomerPhone(phone);
    }

    @Override
    public List<RepairEntity> getRepairsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repairRepository.findByRepairDateBetween(startDate, endDate);
    }

    @Override
    @Transactional
    public RepairEntity updateRepairStatus(Integer repairId, String status) {
        RepairEntity repair = getRepairById(repairId);
        repair.setStatus(status);
        return repairRepository.save(repair);
    }

    @Override
    public Double getTotalRevenue() {
        Double total = repairRepository.getTotalRevenue();
        return total != null ? total : 0.0;
    }

    @Override
    public Double getTotalRevenueByStatus(String status) {
        Double total = repairRepository.getTotalRevenueByStatus(status);
        return total != null ? total : 0.0;
    }

    @Override
    public Long getCountByStatus(String status) {
        return repairRepository.countByStatus(status);
    }

    @Override
    @Transactional
    public void deleteRepair(Integer id) {
        repairRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RepairEntity updateRepair(RepairEntity repair) {
        repair.setUpdatedAt(LocalDateTime.now());
        return repairRepository.save(repair);
    }
}
