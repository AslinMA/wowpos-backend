package org.kmaihome.pos.service;

import org.kmaihome.pos.models.NetProfitSummary;

import java.time.LocalDateTime;

public interface ReportService {
    NetProfitSummary getNetProfitSummary(LocalDateTime from, LocalDateTime to);
}
