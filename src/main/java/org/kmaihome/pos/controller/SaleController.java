package org.kmaihome.pos.controller;

import org.kmaihome.pos.entity.SaleEntity;
import org.kmaihome.pos.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sale")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "false")
public class SaleController {

    @Autowired
    private SaleRepository saleRepository;

    // Search by phone number, name, or transaction ID
    @GetMapping("/search")
    public ResponseEntity<?> searchSales(@RequestParam String query) {
        try {
            System.out.println("🔍 Searching for: [" + query + "]");

            List<SaleEntity> results = new ArrayList<>();

            // Try to find by transaction ID first
            Optional<SaleEntity> byTransactionId = saleRepository.findByTransactionId(query);
            if (byTransactionId.isPresent()) {
                results.add(byTransactionId.get());
            }

            // Search by phone number
            List<SaleEntity> byPhone = saleRepository.findByPhoneNumber(query);
            results.addAll(byPhone);

            // Search by name (case-insensitive, partial match)
            List<SaleEntity> allSales = saleRepository.findAll();
            List<SaleEntity> byName = allSales.stream()
                    .filter(s -> s.getName() != null &&
                            s.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            results.addAll(byName);

            // Remove duplicates
            Set<Integer> seenIds = new HashSet<>();
            results = results.stream()
                    .filter(s -> seenIds.add(s.getSaleId()))
                    .collect(Collectors.toList());

            if (results.isEmpty()) {
                System.out.println("❌ No sales found for: " + query);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No sales found for: " + query));
            }

            System.out.println("✅ Found " + results.size() + " sale(s)");

            // Map to response format
            List<Map<String, Object>> response = results.stream()
                    .map(this::mapSaleToResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("💥 Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Original method - keep this too
    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getSaleByTransactionId(@PathVariable String transactionId) {
        try {
            System.out.println("🔍 Searching for transaction: [" + transactionId + "]");

            Optional<SaleEntity> saleOpt = saleRepository.findByTransactionId(transactionId);

            if (!saleOpt.isPresent()) {
                System.out.println("❌ Not found: [" + transactionId + "]");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Sale not found with Transaction ID: " + transactionId));
            }

            SaleEntity sale = saleOpt.get();
            System.out.println("✅ Found sale for: " + sale.getName());

            return ResponseEntity.ok(mapSaleToResponse(sale));

        } catch (Exception e) {
            System.err.println("💥 Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Helper method to map SaleEntity to response
    private Map<String, Object> mapSaleToResponse(SaleEntity sale) {
        Map<String, Object> response = new HashMap<>();
        response.put("transactionId", sale.getTransactionId());
        response.put("customerName", sale.getName());
        response.put("customerPhone", sale.getPhoneNumber());
        response.put("saleDate", sale.getDate().toString());
        response.put("category", sale.getCategory());
        response.put("brand", sale.getBrand());
        response.put("model", sale.getModel());
        response.put("quantity", sale.getQuantity());
        response.put("sellPrice", sale.getSellPrice());
        response.put("warrantyPeriod", sale.getWarrantyPeriod());
        return response;
    }
}
