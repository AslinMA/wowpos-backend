package org.kmaihome.pos.service;

import org.kmaihome.pos.entity.RequirementItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequirementListService {

    RequirementItemEntity save(RequirementItemEntity item);
    Page<RequirementItemEntity> list(Pageable pageable);
    void delete(Long id);
}
