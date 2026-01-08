package org.kmaihome.pos.controller;

import org.kmaihome.pos.entity.ProductEntity;
import org.kmaihome.pos.entity.RepairEntity;
import org.kmaihome.pos.entity.RepairPartEntity;
import org.kmaihome.pos.models.Repair;
import org.kmaihome.pos.models.RepairPart;
import org.kmaihome.pos.repository.ProductRepository;
import org.kmaihome.pos.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/repair")
@CrossOrigin(origins = "*", allowCredentials = "false")
public class RepairController {

    @Autowired
    private RepairService repairService;

    @Autowired
    private ProductRepository productRepository;  // ✅ ADDED

    // Search by phone, name, or ID
    @GetMapping("/search")
    public ResponseEntity<?> searchRepairs(@RequestParam String query) {
        try {
            System.out.println("🔍 Searching repairs for: [" + query + "]");

            List<RepairEntity> results = new ArrayList<>();

            // Try to find by ID first (if query is numeric)
            try {
                Integer id = Integer.parseInt(query);
                RepairEntity byId = repairService.getRepairById(id);
                if (byId != null) {
                    results.add(byId);
                }
            } catch (NumberFormatException e) {
                // Not a number, skip ID search
            } catch (RuntimeException e) {
                // ID not found, continue
            }

            // Search by phone number
            List<RepairEntity> byPhone = repairService.getRepairsByCustomerPhone(query);
            results.addAll(byPhone);

            // Search by name (case-insensitive, partial match)
            List<RepairEntity> allRepairs = repairService.getAllRepairs();
            List<RepairEntity> byName = allRepairs.stream()
                    .filter(r -> r.getCustomerName() != null &&
                            r.getCustomerName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            results.addAll(byName);

            // Remove duplicates
            Set<Integer> seenIds = new HashSet<>();
            results = results.stream()
                    .filter(r -> seenIds.add(r.getRepairId()))
                    .collect(Collectors.toList());

            if (results.isEmpty()) {
                System.out.println("❌ No repairs found for: " + query);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No repairs found for: " + query));
            }

            System.out.println("✅ Found " + results.size() + " repair(s)");
            return ResponseEntity.ok(results);

        } catch (Exception e) {
            System.err.println("💥 Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Create repair
    @PostMapping
    public ResponseEntity<?> createRepair(@RequestBody Repair repair) {
        try {
            RepairEntity created = repairService.createRepair(repair);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update repair (add parts, update labor charge, update issue description)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRepair(@PathVariable Integer id, @RequestBody Repair repairUpdate) {
        try {
            System.out.println("🔧 Updating repair ID: " + id);

            RepairEntity existingRepair = repairService.getRepairById(id);

            // Update labor charge if provided
            if (repairUpdate.getLaborCharge() != null) {
                existingRepair.setLaborCharge(repairUpdate.getLaborCharge());
                System.out.println("   Updated labor charge: " + repairUpdate.getLaborCharge());
            }

            // Update issue description if provided (append new issues)
            if (repairUpdate.getIssueDescription() != null && !repairUpdate.getIssueDescription().isEmpty()) {
                String currentIssue = existingRepair.getIssueDescription();
                String newIssue = currentIssue + "\n\n[UPDATE]: " + repairUpdate.getIssueDescription();
                existingRepair.setIssueDescription(newIssue);
                System.out.println("   Added new issue description");
            }

            // Add new parts if provided
            if (repairUpdate.getParts() != null && !repairUpdate.getParts().isEmpty()) {
                for (RepairPart newPart : repairUpdate.getParts()) {
                    // ✅ UPDATED: Calculate total with discount
                    double subtotal = newPart.getQuantity() * newPart.getUnitPrice();
                    double discount = (newPart.getDiscount() != null) ? newPart.getDiscount() : 0.0;
                    double total = subtotal - discount;

                    RepairPartEntity partEntity = RepairPartEntity.builder()
                            .repair(existingRepair)
                            .partName(newPart.getPartName())
                            .partCategory(newPart.getPartCategory())
                            .partBrand(newPart.getPartBrand())
                            .partModel(newPart.getPartModel())
                            .quantity(newPart.getQuantity())
                            .unitPrice(newPart.getUnitPrice())
                            .discount(discount)  // ✅ ADDED
                            .totalPrice(total)   // ✅ UPDATED
                            .build();

                    existingRepair.getParts().add(partEntity);
                    System.out.println("   Added part: " + newPart.getPartName());
                }
            }

            // Recalculate total cost
            double partsTotal = existingRepair.getParts().stream()
                    .mapToDouble(RepairPartEntity::getTotalPrice)
                    .sum();
            existingRepair.setTotalCost(existingRepair.getLaborCharge() + partsTotal);

            System.out.println("   New total cost: " + existingRepair.getTotalCost());

            // Save updated repair
            RepairEntity updated = repairService.updateRepair(existingRepair);

            System.out.println("✅ Repair updated successfully");
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            System.err.println("💥 Error updating repair: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all repairs
    @GetMapping
    public ResponseEntity<List<RepairEntity>> getAllRepairs() {
        return ResponseEntity.ok(repairService.getAllRepairs());
    }

    // Get repair by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRepairById(@PathVariable Integer id) {
        try {
            RepairEntity repair = repairService.getRepairById(id);
            return ResponseEntity.ok(repair);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get repairs by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RepairEntity>> getRepairsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(repairService.getRepairsByStatus(status));
    }

    // Get repairs by customer phone
    @GetMapping("/customer/{phone}")
    public ResponseEntity<List<RepairEntity>> getRepairsByCustomerPhone(@PathVariable String phone) {
        return ResponseEntity.ok(repairService.getRepairsByCustomerPhone(phone));
    }

    // Get repairs by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<RepairEntity>> getRepairsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(repairService.getRepairsByDateRange(startDate, endDate));
    }

    // ✅ UPDATED: Update repair status with inventory management
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateRepairStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        try {
            String status = body.get("status");

            // ✅ NEW: When status is "Completed", reduce inventory
            if ("Completed".equals(status)) {
                RepairEntity repair = repairService.getRepairById(id);

                // Check if already completed to prevent double inventory reduction
                if ("Completed".equals(repair.getStatus())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Repair is already completed"));
                }

                // Reduce inventory for each part
                for (RepairPartEntity part : repair.getParts()) {
                    // Find product by brand and model
                    List<ProductEntity> products = productRepository.findByBrandAndModel(
                            part.getPartBrand(),
                            part.getPartModel()
                    );

                    if (!products.isEmpty()) {
                        ProductEntity product = products.get(0);
                        int newQuantity = product.getQuantity() - part.getQuantity();

                        if (newQuantity < 0) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body(Map.of("error", "Not enough stock for: " + part.getPartName() +
                                            ". Available: " + product.getQuantity() +
                                            ", Required: " + part.getQuantity()));
                        }

                        product.setQuantity(newQuantity);
                        productRepository.save(product);

                        System.out.println("✅ Reduced inventory: " + part.getPartName() +
                                " (Qty: " + part.getQuantity() +
                                ", New stock: " + newQuantity + ")");
                    } else {
                        System.out.println("⚠️ Product not found in inventory: " +
                                part.getPartBrand() + " " + part.getPartModel());
                    }
                }
            }

            RepairEntity updated = repairService.updateRepairStatus(id, status);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            System.err.println("💥 Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRevenue", repairService.getTotalRevenue());
        stats.put("completedRevenue", repairService.getTotalRevenueByStatus("Completed"));
        stats.put("pendingCount", repairService.getCountByStatus("Pending"));
        stats.put("inProgressCount", repairService.getCountByStatus("In Progress"));
        stats.put("completedCount", repairService.getCountByStatus("Completed"));
        return ResponseEntity.ok(stats);
    }

    // Delete repair
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRepair(@PathVariable Integer id) {
        try {
            repairService.deleteRepair(id);
            return ResponseEntity.ok(Map.of("message", "Repair deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
