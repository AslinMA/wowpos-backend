package org.kmaihome.pos.service;

import lombok.RequiredArgsConstructor;
import org.kmaihome.pos.entity.ProductEntity;
import org.kmaihome.pos.entity.RepairEntity;
import org.kmaihome.pos.entity.RepairPartEntity;
import org.kmaihome.pos.models.NetProfitSummary;
import org.kmaihome.pos.models.saleDetailsForDisaply;
import org.kmaihome.pos.repository.DamageRepository;
import org.kmaihome.pos.repository.ProductRepository;
import org.kmaihome.pos.repository.RepairPartRepository;
import org.kmaihome.pos.repository.RepairRepository;
import org.kmaihome.pos.repository.ReturnRecordRepository;
import org.kmaihome.pos.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final SaleRepository saleRepository;
    private final DamageRepository damageRepository;
    private final ReturnRecordRepository returnRecordRepository;
    private final RepairRepository repairRepository;
    private final RepairPartRepository repairPartRepository;
    private final ProductRepository productRepository;

    @Override
    public NetProfitSummary getNetProfitSummary(LocalDateTime from, LocalDateTime to) {

        // ---------- 1. SALES SIDE (filtered by date) ----------
        List<saleDetailsForDisaply> sales =
                saleRepository.findSalesWithBuyPriceBetween(from, to);

        double salesNet = 0.0;
        double salesCOGS = 0.0;

        for (saleDetailsForDisaply s : sales) {
            int qty = parseIntSafe(s.getQuantity());

            BigDecimal sell = parseDecimalSafe(s.getSellPrice());
            BigDecimal disc = parseDecimalSafe(s.getDiscountedPrice());
            BigDecimal buy  = parseDecimalSafe(s.getBuyPrice());

            BigDecimal unitNet = disc.compareTo(BigDecimal.ZERO) > 0 ? disc : sell;

            BigDecimal net  = unitNet.multiply(BigDecimal.valueOf(qty));   // revenue
            BigDecimal cogs = buy.multiply(BigDecimal.valueOf(qty));       // cost

            salesNet  += net.doubleValue();
            salesCOGS += cogs.doubleValue();
        }

        double salesProfit = salesNet - salesCOGS;

        // ---------- 2. REPAIR SIDE (detailed) ----------
        List<RepairEntity> repairs = repairRepository.findByRepairDateBetween(from, to);

        double repairRevenue = 0.0;
        double repairPartsCost = 0.0;
        double repairPartsRevenue = 0.0;
        double repairLaborRevenue = 0.0;

        for (RepairEntity r : repairs) {
            if (r.getTotalCost() != null) {
                repairRevenue += r.getTotalCost();
            }

            if (r.getLaborCharge() != null) {
                repairLaborRevenue += r.getLaborCharge();
            }

            if (r.getParts() != null) {
                for (RepairPartEntity part : r.getParts()) {
                    if (part.getQuantity() == null || part.getQuantity() <= 0) continue;

                    if (part.getTotalPrice() != null) {
                        repairPartsRevenue += part.getTotalPrice();
                    }

                    List<ProductEntity> products =
                            productRepository.findByBrandAndModel(part.getPartBrand(), part.getPartModel());
                    if (!products.isEmpty()) {
                        ProductEntity p = products.get(0);
                        if (p.getBuyPrice() != null) {
                            repairPartsCost += p.getBuyPrice()
                                    .multiply(BigDecimal.valueOf(part.getQuantity()))
                                    .doubleValue();
                        }
                    }
                }
            }
        }

        double repairPartsProfit = repairPartsRevenue - repairPartsCost;
        double repairProfit = repairRevenue - repairPartsCost;

        // ---------- 3. LOSSES ----------
        double damageLoss = damageRepository.findByDamageDateBetween(from, to)
                .stream()
                .map(d -> d.getLossAmount() != null ? d.getLossAmount() : 0.0)
                .reduce(0.0, Double::sum);

        double returnLoss = returnRecordRepository.findByReturnDateBetween(from, to)
                .stream()
                .filter(r -> "My Loss".equalsIgnoreCase(r.getClaimType()))
                .map(r -> r.getLossAmount() != null ? r.getLossAmount() : 0.0)
                .reduce(0.0, Double::sum);

        double totalLoss = damageLoss + returnLoss;

        // ---------- 4. FINAL METRICS ----------
        double grossProfit   = salesProfit + repairProfit;
        double trueNetProfit = grossProfit - totalLoss;

        double revenueBase    = salesNet + repairRevenue;
        double lossPctOfSales = revenueBase > 0 ? (totalLoss / revenueBase) * 100.0 : 0.0;

        // ---------- 5. RETURN SUMMARY ----------
        return new NetProfitSummary(
                from.toLocalDate().toString(),
                to.toLocalDate().toString(),
                round(salesNet),
                round(salesCOGS),
                round(salesProfit),
                round(repairRevenue),
                round(repairPartsCost),
                round(repairPartsRevenue),
                round(repairLaborRevenue),
                round(repairPartsProfit),
                round(repairProfit),
                round(damageLoss),
                round(returnLoss),
                round(totalLoss),
                round(grossProfit),
                round(trueNetProfit),
                round(lossPctOfSales)
        );
    }

    // --------- helpers ---------

    private BigDecimal parseDecimalSafe(String s) {
        if (s == null || s.isBlank()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(s.replaceAll("[^0-9.\\-]", ""));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private int parseIntSafe(String s) {
        if (s == null || s.isBlank()) return 0;
        try {
            return Integer.parseInt(s.replaceAll("[^0-9\\-]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
