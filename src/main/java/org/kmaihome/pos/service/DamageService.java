package org.kmaihome.pos.service;

import org.kmaihome.pos.entity.DamageEntity;
import org.kmaihome.pos.models.Damage;
import java.time.LocalDateTime;
import java.util.List;

public interface DamageService {
    DamageEntity createDamage(Damage damage);
    List<DamageEntity> getAllDamages();
    DamageEntity getDamageById(Integer id);
    List<DamageEntity> getDamagesByType(String damageType);
    List<DamageEntity> getDamagesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Double getTotalLoss();
    Double getTotalLossByType(String damageType);
    Long getCountByType(String damageType);
    void deleteDamage(Integer id);
}
