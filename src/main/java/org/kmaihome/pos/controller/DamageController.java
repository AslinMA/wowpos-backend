package org.kmaihome.pos.controller;

import org.kmaihome.pos.entity.DamageEntity;
import org.kmaihome.pos.models.Damage;
import org.kmaihome.pos.service.DamageService;
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
@RequestMapping("/api/damage")
@CrossOrigin(origins = "*", allowCredentials = "false")

public class DamageController {

    @Autowired
    private DamageService damageService;

    // Create damage record
    @PostMapping
    public ResponseEntity<?> createDamage(@RequestBody Damage damage) {
        try {
            DamageEntity created = damageService.createDamage(damage);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all damages
    @GetMapping
    public ResponseEntity<List<DamageEntity>> getAllDamages() {
        return ResponseEntity.ok(damageService.getAllDamages());
    }

    // Get damage by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getDamageById(@PathVariable Integer id) {
        try {
            DamageEntity damage = damageService.getDamageById(id);
            return ResponseEntity.ok(damage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get damages by type
    @GetMapping("/type/{damageType}")
    public ResponseEntity<List<DamageEntity>> getDamagesByType(@PathVariable String damageType) {
        return ResponseEntity.ok(damageService.getDamagesByType(damageType));
    }

    // Get damages by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<DamageEntity>> getDamagesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(damageService.getDamagesByDateRange(startDate, endDate));
    }

    // Get statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLoss", damageService.getTotalLoss());
        stats.put("supplierIssueLoss", damageService.getTotalLossByType("Supplier Issue"));
        stats.put("myFaultLoss", damageService.getTotalLossByType("My Fault"));
        stats.put("supplierIssueCount", damageService.getCountByType("Supplier Issue"));
        stats.put("myFaultCount", damageService.getCountByType("My Fault"));
        return ResponseEntity.ok(stats);
    }

    // Delete damage
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDamage(@PathVariable Integer id) {
        try {
            damageService.deleteDamage(id);
            return ResponseEntity.ok(Map.of("message", "Damage deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
