package org.kmaihome.pos.service;

import org.kmaihome.pos.entity.ReturnRecordEntity;
import org.kmaihome.pos.entity.SaleEntity;
import org.kmaihome.pos.models.ReturnRecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReturnService {
    SaleEntity getSaleByTransactionId(String transactionId);
    Map<String, Object> checkWarranty(String transactionId);
    ReturnRecordEntity createReturn(ReturnRecord returnRecord);
    List<ReturnRecordEntity> getAllReturns();
    ReturnRecordEntity getReturnById(Integer id);
    List<ReturnRecordEntity> getReturnsByClaimType(String claimType);
    List<ReturnRecordEntity> getReturnsByWarrantyStatus(Boolean withinWarranty);
    List<ReturnRecordEntity> getReturnsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Double getTotalLoss();
    Double getTotalLossByClaimType(String claimType);
    Long getCountByClaimType(String claimType);
    Long getCountByWarrantyStatus(Boolean withinWarranty);
    void deleteReturn(Integer id);
}
