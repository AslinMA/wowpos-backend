package org.kmaihome.pos.controller;

import lombok.RequiredArgsConstructor;
import org.kmaihome.pos.models.NetProfitSummary;
import org.kmaihome.pos.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*", allowCredentials = "false")
@RequiredArgsConstructor

public class ReportsController {

    private final ReportService reportService;

    // Example:
    // /api/reports/net-profit?from=2025-12-19&to=2025-12-31
    // If params are missing, use last 7 days
    @GetMapping("/net-profit")
    public ResponseEntity<NetProfitSummary> getNetProfitSummary(
            @RequestParam(value = "from", required = false) String fromStr,
            @RequestParam(value = "to", required = false) String toStr
    ) {
        LocalDateTime from;
        LocalDateTime to;

        if (fromStr == null || toStr == null) {
            // default: last 7 days
            LocalDate today = LocalDate.now();
            LocalDate weekAgo = today.minusDays(6);
            from = weekAgo.atStartOfDay();
            to = today.atTime(23, 59, 59);
        } else {
            // Accept simple date: yyyy-MM-dd
            LocalDate fromDate = LocalDate.parse(fromStr.trim());
            LocalDate toDate   = LocalDate.parse(toStr.trim());
            from = fromDate.atStartOfDay();
            to   = toDate.atTime(23, 59, 59);
        }

        NetProfitSummary summary = reportService.getNetProfitSummary(from, to);
        return ResponseEntity.ok(summary);
    }
}
