package org.kmaihome.pos.repository;

import org.kmaihome.pos.entity.RequirementItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequirementListRepository extends JpaRepository<RequirementItemEntity, Long> {
    Page<RequirementItemEntity> findAll(Pageable pageable);
}
