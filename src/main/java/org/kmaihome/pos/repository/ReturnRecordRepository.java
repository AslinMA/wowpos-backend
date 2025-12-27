package org.kmaihome.pos.repository;

import org.kmaihome.pos.entity.ReturnRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReturnRecordRepository extends JpaRepository<ReturnRecordEntity, Integer> {

    Optional<ReturnRecordEntity> findBySaleTransactionId(String saleTransactionId);
    List<ReturnRecordEntity> findByClaimType(String claimType);
    List<ReturnRecordEntity> findByWithinWarranty(Boolean withinWarranty);
    List<ReturnRecordEntity> findByReturnDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(r.lossAmount) FROM ReturnRecordEntity r WHERE r.claimType = :claimType")
    Double getTotalLossByClaimType(String claimType);

    @Query("SELECT SUM(r.lossAmount) FROM ReturnRecordEntity r")
    Double getTotalLoss();

    Long countByClaimType(String claimType);
    Long countByWithinWarranty(Boolean withinWarranty);
}
