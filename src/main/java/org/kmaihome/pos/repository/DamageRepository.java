package org.kmaihome.pos.repository;

import org.kmaihome.pos.entity.DamageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DamageRepository extends JpaRepository<DamageEntity, Integer> {

    List<DamageEntity> findByDamageType(String damageType);
    List<DamageEntity> findByDamageDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(d.lossAmount) FROM DamageEntity d WHERE d.damageType = :damageType")
    Double getTotalLossByType(String damageType);

    @Query("SELECT SUM(d.lossAmount) FROM DamageEntity d")
    Double getTotalLoss();

    Long countByDamageType(String damageType);
}
