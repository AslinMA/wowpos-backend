package org.kmaihome.pos.controller;

import org.kmaihome.pos.entity.ReturnRecordEntity;
import org.kmaihome.pos.models.ReturnRecord;
import org.kmaihome.pos.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/return")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "false")
public class ReturnController {

    @Autowired
    private ReturnService returnService;

    // Check warranty for a sale
    @GetMapping("/warranty-check/{transactionId}")
    public ResponseEntity<?> checkWarranty(@PathVariable String transactionId) {
        try {
            Map<String, Object> warrantyStatus = returnService.checkWarranty(transactionId);
            return ResponseEntity.ok(warrantyStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Create return record
    @PostMapping
    public ResponseEntity<?> createReturn(@RequestBody ReturnRecord returnRecord) {
        try {
            ReturnRecordEntity created = returnService.createReturn(returnRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all returns
    @GetMapping
    public ResponseEntity<List<ReturnRecordEntity>> getAllReturns() {
        return ResponseEntity.ok(returnService.getAllReturns());
    }

    // Get return by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getReturnById(@PathVariable Integer id) {
        try {
            ReturnRecordEntity returnRecord = returnService.getReturnById(id);
            return ResponseEntity.ok(returnRecord);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get returns by claim type
    @GetMapping("/claim-type/{claimType}")
    public ResponseEntity<List<ReturnRecordEntity>> getReturnsByClaimType(@PathVariable String claimType) {
        return ResponseEntity.ok(returnService.getReturnsByClaimType(claimType));
    }

    // Get returns by warranty status
    @GetMapping("/warranty-status/{withinWarranty}")
    public ResponseEntity<List<ReturnRecordEntity>> getReturnsByWarrantyStatus(@PathVariable Boolean withinWarranty) {
        return ResponseEntity.ok(returnService.getReturnsByWarrantyStatus(withinWarranty));
    }

    // Get returns by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<ReturnRecordEntity>> getReturnsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(returnService.getReturnsByDateRange(startDate, endDate));
    }

    // Get statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLoss", returnService.getTotalLoss());
        stats.put("supplierClaimLoss", returnService.getTotalLossByClaimType("Supplier Claim"));
        stats.put("myLoss", returnService.getTotalLossByClaimType("My Loss"));
        stats.put("supplierClaimCount", returnService.getCountByClaimType("Supplier Claim"));
        stats.put("myLossCount", returnService.getCountByClaimType("My Loss"));
        stats.put("withinWarrantyCount", returnService.getCountByWarrantyStatus(true));
        stats.put("expiredWarrantyCount", returnService.getCountByWarrantyStatus(false));
        return ResponseEntity.ok(stats);
    }

    // Delete return
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReturn(@PathVariable Integer id) {
        try {
            returnService.deleteReturn(id);
            return ResponseEntity.ok(Map.of("message", "Return deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
