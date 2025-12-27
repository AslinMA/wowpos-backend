package org.kmaihome.pos.service;

import lombok.RequiredArgsConstructor;
import org.kmaihome.pos.entity.SaleEntity;
import org.kmaihome.pos.models.DaySales;
import org.kmaihome.pos.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMetricsServiceImpl implements AdminMetricsService {

    private final SaleRepository saleRepository;

    @Override
    public List<DaySales> getLast7DaysSales() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);

        Map<LocalDate, DaySales> byDay = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            LocalDate d = start.plusDays(i);
            byDay.put(d, DaySales.builder()
                    .date(d.toString())
                    .revenue(BigDecimal.ZERO)
                    .items(0)
                    .build());
        }

        List<Object[]> rows = saleRepository.sumDailyRevenueAndQty(start.atStartOfDay());
        for (Object[] r : rows) {
            LocalDate d = LocalDate.parse((String) r[0]);
            BigDecimal revenue = (BigDecimal) r[1];
            Long items = (Long) r[2];
            DaySales ds = byDay.get(d);
            if (ds != null) {
                ds.setRevenue(revenue);
                ds.setItems(items != null ? items.intValue() : 0);
            }
        }
        return new ArrayList<>(byDay.values());
    }

    @Override
    public Map<String, Object> getWeeklyTotalSales() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // Get total for current week - NOW returns List<Object[]>
        List<Object[]> currentWeekResults = saleRepository.sumTotalRevenueAndQty(
                startOfWeek.atStartOfDay(),
                endOfWeek.atTime(23, 59, 59)
        );

        // Extract values from the first row (if exists)
        double totalRevenue = 0.0;
        long totalItems = 0L;

        if (currentWeekResults != null && !currentWeekResults.isEmpty()) {
            Object[] row = currentWeekResults.get(0); // Get first row

            // Index 0 = revenue
            if (row[0] != null) {
                totalRevenue = ((Number) row[0]).doubleValue();
            }

            // Index 1 = items count
            if (row.length > 1 && row[1] != null) {
                totalItems = ((Number) row[1]).longValue();
            }
        }

        // Get previous week total for growth calculation
        LocalDate prevWeekStart = startOfWeek.minusDays(7);
        LocalDate prevWeekEnd = endOfWeek.minusDays(7);

        List<Object[]> prevWeekResults = saleRepository.sumTotalRevenueAndQty(
                prevWeekStart.atStartOfDay(),
                prevWeekEnd.atTime(23, 59, 59)
        );

        double prevWeekRevenue = 0.0;
        if (prevWeekResults != null && !prevWeekResults.isEmpty()) {
            Object[] row = prevWeekResults.get(0);
            if (row[0] != null) {
                prevWeekRevenue = ((Number) row[0]).doubleValue();
            }
        }

        // Calculate growth percentage
        double growthPercentage = 0.0;
        if (prevWeekRevenue > 0) {
            growthPercentage = ((totalRevenue - prevWeekRevenue) / prevWeekRevenue) * 100;
        } else if (totalRevenue > 0) {
            growthPercentage = 100.0;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);
        result.put("totalItems", totalItems);
        result.put("growthPercentage", Math.round(growthPercentage * 10.0) / 10.0);
        result.put("startDate", startOfWeek.toString());
        result.put("endDate", endOfWeek.toString());

        return result;
    }
}
