package org.kmaihome.pos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kmaihome.pos.entity.SaleEntity;
import org.kmaihome.pos.models.Sale;
import org.kmaihome.pos.models.SaleItem;
import org.kmaihome.pos.models.saleDetailsForDisaply;
import org.kmaihome.pos.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {
    @Autowired
    SaleRepository repository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleRepository saleRepository;

    public void saveSale(Sale sale) {
        System.out.println("saling function is working in service class");
        for (SaleItem it : sale.getItems()) {
            productService.decrementStock(it.getProductId(), it.getQuantity());
        }

        for (SaleItem it : sale.getItems()) {
            SaleEntity row = SaleEntity.builder()
                    .transactionId(sale.getTransactionId())
                    .name(sale.getName())
                    .phoneNumber(sale.getPhoneNumber())
                    .date(sale.getDate() != null ? sale.getDate().atStartOfDay() : LocalDate.now().atStartOfDay())
                    // ✅ FIXED: Keep as BigDecimal, don't convert to Double
                    .discountedPrice(it.getDiscountedPrice() != null ? it.getDiscountedPrice() : BigDecimal.ZERO)
                    .sellPrice(it.getSellPrice() != null ? it.getSellPrice() : BigDecimal.ZERO)
                    .category(it.getCategory())
                    .brand(it.getBrand())
                    .model(it.getModel())
                    .quantity(it.getQuantity())
                    .productId(it.getProductId())
                    .warrantyPeriod(sale.getWarrantyPeriod())
                    .build();
            saleRepository.save(row);
        }
    }

    public List<saleDetailsForDisaply> saleRetrive() {
        List<saleDetailsForDisaply> allSaleList = saleRepository.findAllSalesWithBuyPrice();
        System.out.println("Retrieved sales with buy price: " + allSaleList);
        return allSaleList;
    }
}
