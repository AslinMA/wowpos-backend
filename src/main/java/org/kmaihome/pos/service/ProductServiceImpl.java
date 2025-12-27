package org.kmaihome.pos.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.kmaihome.pos.entity.ProductEntity;
import org.kmaihome.pos.models.LowStockItem;
import org.kmaihome.pos.models.Product;
import org.kmaihome.pos.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    @Override
    public Product save(Product product) {
        // map models.Product -> entity.ProductEntity
        ProductEntity e = ProductEntity.builder()
                .id(product.getId() == null || product.getId() == 0 ? null : product.getId())

                .date(product.getDate())
                .category(product.getCategory())
                .brand(product.getBrand())
                .model(product.getModel())
                .quantity(product.getQuantity())
                .buyPrice(product.getBuyPrice())
                .sellPrice(product.getSellPrice())
                .location(product.getLocation())
                .isActive(Boolean.TRUE.equals(product.getIsActive()))
                .build();

        ProductEntity saved = repo.save(e);

        // map back entity -> model
        return Product.builder()
                .id(saved.getId())
                .date(saved.getDate())
                .category(saved.getCategory())
                .brand(saved.getBrand())
                .model(saved.getModel())
                .quantity(saved.getQuantity())
                .buyPrice(saved.getBuyPrice())
                .sellPrice(saved.getSellPrice())
                .location(saved.getLocation())
                .isActive(saved.isActive())
                .build();
    }

    @Override
    public List<Product> retrive() {
        return repo.findByIsActiveTrue().stream().map(pe ->
                Product.builder()
                        .id(pe.getId())
                        .date(pe.getDate())
                        .category(pe.getCategory())
                        .brand(pe.getBrand())
                        .model(pe.getModel())
                        .quantity(pe.getQuantity())
                        .buyPrice(pe.getBuyPrice())
                        .sellPrice(pe.getSellPrice())
                        .location(pe.getLocation())
                        .isActive(pe.isActive())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public void decrementStock(Integer productId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        int updated = repo.tryDecrement(productId, qty);
        if (updated == 0) {
            throw new IllegalStateException("Insufficient stock for productId: " + productId);
        }
    }

    // NOTE: If you already had your own implementation for low-stock, keep it.
    // This version assumes LowStockItem has setters: category, brand, model, quantity, band.
    @Override
    public List<LowStockItem> getLowStockItems(Integer maxThreshold) {
        return repo.findByIsActiveTrue().stream()
                .map(pe -> {
                    LowStockItem li = new LowStockItem();
                    li.setCategory(pe.getCategory());
                    li.setBrand(pe.getBrand());
                    li.setModel(pe.getModel());
                    li.setQuantity(pe.getQuantity());
                    li.setBand(classifyBand(pe.getQuantity()));
                    return li;
                })
                .filter(li -> maxThreshold == null || li.getQuantity() <= maxThreshold)
                .collect(Collectors.toList());
    }

    private String classifyBand(int qty) {
        if (qty < 3) return "CRITICAL";
        if (qty <= 5) return "WARN";
        if (qty <= 10) return "INFO";
        if (qty <= 15) return "LOW";
        return "OK";
    }
    @Override
    public void restock(Integer productId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        int updated = repo.incrementStock(productId, qty);
        if (updated == 0) {
            throw new IllegalStateException("Product not found: " + productId);
        }
    }

}
