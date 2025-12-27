package org.kmaihome.pos.service;

import org.kmaihome.pos.models.DaySales;

import java.util.List;
import java.util.Map;

public interface AdminMetricsService {
    List<DaySales> getLast7DaysSales();
    Map<String, Object> getWeeklyTotalSales();
}
