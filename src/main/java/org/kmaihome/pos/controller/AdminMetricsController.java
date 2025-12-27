package org.kmaihome.pos.controller;

import lombok.RequiredArgsConstructor;
import org.kmaihome.pos.models.DaySales;
import org.kmaihome.pos.service.AdminMetricsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/metrics")
@RequiredArgsConstructor
public class AdminMetricsController {

    private final AdminMetricsService metrics;

    @GetMapping("/weekly-sales")
    public List<DaySales> weeklySales() {
        return metrics.getLast7DaysSales();
    }

    @GetMapping("/weekly-total")
    public Map<String, Object> weeklyTotal() {
        return metrics.getWeeklyTotalSales();
    }
}
