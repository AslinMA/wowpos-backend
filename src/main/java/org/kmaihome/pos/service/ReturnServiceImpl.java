package org.kmaihome.pos.service;

import org.kmaihome.pos.entity.ReturnRecordEntity;
import org.kmaihome.pos.entity.SaleEntity;
import org.kmaihome.pos.entity.ProductEntity;
import org.kmaihome.pos.models.ReturnRecord;
import org.kmaihome.pos.repository.ReturnRecordRepository;
import org.kmaihome.pos.repository.SaleRepository;
import org.kmaihome.pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReturnServiceImpl implements ReturnService {

    @Autowired
    private ReturnRecordRepository returnRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public SaleEntity getSaleByTransactionId(String transactionId) {
        Optional<SaleEntity> saleOpt = saleRepository.findByTransactionId(transactionId);
        if (!saleOpt.isPresent()) {
            throw new RuntimeException("Sale not found with Transaction ID: " + transactionId);
        }
        return saleOpt.get();
    }

    @Override
    public Map<String, Object> checkWarranty(String transactionId) {
        SaleEntity sale = getSaleByTransactionId(transactionId);

        LocalDate saleDate = sale.getDate().toLocalDate();
        LocalDate today = LocalDate.now();
        long daysSinceSale = ChronoUnit.DAYS.between(saleDate, today);

        int warrantyDays = getWarrantyDays(sale.getWarrantyPeriod());
        long daysRemaining = warrantyDays - daysSinceSale;
        boolean valid = daysRemaining > 0;

        String message;
        if (valid) {
            message = "✅ Valid - " + daysRemaining + " days remaining";
        } else if (warrantyDays == 0) {
            message = "❌ No Warranty";
        } else {
            message = "❌ Expired " + Math.abs(daysRemaining) + " days ago";
        }

        Map<String, Object> result = new HashMap<>();
        result.put("valid", valid);
        result.put("daysRemaining", daysRemaining);
        result.put("message", message);
        result.put("sale", sale);

        return result;
    }

    private int getWarrantyDays(String warrantyPeriod) {
        if (warrantyPeriod == null || warrantyPeriod.equals("No Warranty")) {
            return 0;
        }
        switch (warrantyPeriod) {
            case "1 Month":
                return 30;
            case "3 Months":
                return 90;
            case "1 Year":
                return 365;
            default:
                return 0;
        }
    }

    @Override
    @Transactional
    public ReturnRecordEntity createReturn(ReturnRecord returnRecord) {
        // Validate replacement product exists
        Optional<ProductEntity> replacementOpt = productRepository.findById(returnRecord.getReplacementProductId());
        if (!replacementOpt.isPresent()) {
            throw new RuntimeException("Replacement product not found with ID: " + returnRecord.getReplacementProductId());
        }

        ProductEntity replacement = replacementOpt.get();

        // Validate sufficient stock
        if (replacement.getQuantity() < returnRecord.getReturnedQuantity()) {
            throw new RuntimeException("Insufficient replacement stock. Available: " + replacement.getQuantity());
        }

        // Reduce replacement product stock
        replacement.setQuantity(replacement.getQuantity() - returnRecord.getReturnedQuantity());
        productRepository.save(replacement);

        // Calculate loss amount
        double lossAmount = 0.0;
        if (returnRecord.getClaimType().equals("My Loss")) {
            lossAmount = replacement.getBuyPrice().multiply(java.math.BigDecimal.valueOf(returnRecord.getReturnedQuantity())).doubleValue();


        }

        // Create return entity
        ReturnRecordEntity returnEntity = ReturnRecordEntity.builder()
                .saleTransactionId(returnRecord.getSaleTransactionId())
                .originalSaleDate(returnRecord.getOriginalSaleDate())
                .customerName(returnRecord.getCustomerName())
                .customerPhone(returnRecord.getCustomerPhone())
                .returnedCategory(returnRecord.getReturnedCategory())
                .returnedBrand(returnRecord.getReturnedBrand())
                .returnedModel(returnRecord.getReturnedModel())
                .returnedQuantity(returnRecord.getReturnedQuantity())
                .replacementProductId(returnRecord.getReplacementProductId())
                .replacementCategory(returnRecord.getReplacementCategory())
                .replacementBrand(returnRecord.getReplacementBrand())
                .replacementModel(returnRecord.getReplacementModel())
                .warrantyPeriod(returnRecord.getWarrantyPeriod())
                .withinWarranty(returnRecord.getWithinWarranty())
                .claimType(returnRecord.getClaimType())
                .lossAmount(lossAmount)
                .notes(returnRecord.getNotes())
                .build();

        return returnRepository.save(returnEntity);
    }

    @Override
    public List<ReturnRecordEntity> getAllReturns() {
        return returnRepository.findAll();
    }

    @Override
    public ReturnRecordEntity getReturnById(Integer id) {
        return returnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Return not found with ID: " + id));
    }

    @Override
    public List<ReturnRecordEntity> getReturnsByClaimType(String claimType) {
        return returnRepository.findByClaimType(claimType);
    }

    @Override
    public List<ReturnRecordEntity> getReturnsByWarrantyStatus(Boolean withinWarranty) {
        return returnRepository.findByWithinWarranty(withinWarranty);
    }

    @Override
    public List<ReturnRecordEntity> getReturnsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return returnRepository.findByReturnDateBetween(startDate, endDate);
    }

    @Override
    public Double getTotalLoss() {
        Double total = returnRepository.getTotalLoss();
        return total != null ? total : 0.0;
    }

    @Override
    public Double getTotalLossByClaimType(String claimType) {
        Double total = returnRepository.getTotalLossByClaimType(claimType);
        return total != null ? total : 0.0;
    }

    @Override
    public Long getCountByClaimType(String claimType) {
        return returnRepository.countByClaimType(claimType);
    }

    @Override
    public Long getCountByWarrantyStatus(Boolean withinWarranty) {
        return returnRepository.countByWithinWarranty(withinWarranty);
    }

    @Override
    @Transactional
    public void deleteReturn(Integer id) {
        returnRepository.deleteById(id);
    }
}
