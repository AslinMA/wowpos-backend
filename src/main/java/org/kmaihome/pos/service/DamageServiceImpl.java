package org.kmaihome.pos.service;

import org.kmaihome.pos.entity.DamageEntity;
import org.kmaihome.pos.entity.ProductEntity;
import org.kmaihome.pos.models.Damage;
import org.kmaihome.pos.repository.DamageRepository;
import org.kmaihome.pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DamageServiceImpl implements DamageService {

    @Autowired
    private DamageRepository damageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public DamageEntity createDamage(Damage damage) {
        // Validate product exists
        Optional<ProductEntity> productOpt = productRepository.findById(damage.getProductId());
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + damage.getProductId());
        }

        ProductEntity product = productOpt.get();

        // Validate sufficient quantity
        if (product.getQuantity() < damage.getQuantity()) {
            throw new RuntimeException("Insufficient quantity. Available: " + product.getQuantity());
        }

        // Reduce stock
        product.setQuantity(product.getQuantity() - damage.getQuantity());
        productRepository.save(product);

        // Create damage entity
        DamageEntity damageEntity = DamageEntity.builder()
                .productId(damage.getProductId())
                .category(damage.getCategory())
                .brand(damage.getBrand())
                .model(damage.getModel())
                .quantity(damage.getQuantity())
                .damageType(damage.getDamageType())
                .damageReason(damage.getDamageReason())
                .buyPrice(damage.getBuyPrice())          // ← ADD THIS LINE!
                .lossAmount(damage.getLossAmount())
                .build();

        return damageRepository.save(damageEntity);
    }

    @Override
    public List<DamageEntity> getAllDamages() {
        return damageRepository.findAll();
    }

    @Override
    public DamageEntity getDamageById(Integer id) {
        return damageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Damage not found with ID: " + id));
    }

    @Override
    public List<DamageEntity> getDamagesByType(String damageType) {
        return damageRepository.findByDamageType(damageType);
    }

    @Override
    public List<DamageEntity> getDamagesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return damageRepository.findByDamageDateBetween(startDate, endDate);
    }

    @Override
    public Double getTotalLoss() {
        Double total = damageRepository.getTotalLoss();
        return total != null ? total : 0.0;
    }

    @Override
    public Double getTotalLossByType(String damageType) {
        Double total = damageRepository.getTotalLossByType(damageType);
        return total != null ? total : 0.0;
    }

    @Override
    public Long getCountByType(String damageType) {
        return damageRepository.countByDamageType(damageType);
    }

    @Override
    @Transactional
    public void deleteDamage(Integer id) {
        damageRepository.deleteById(id);
    }
}
