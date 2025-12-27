package org.kmaihome.pos.service;

import org.kmaihome.pos.entity.RequirementItemEntity;
import org.kmaihome.pos.repository.RequirementListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequirementListServiceImpl implements RequirementListService {

    private final RequirementListRepository repo;

    public RequirementListServiceImpl(RequirementListRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public RequirementItemEntity save(RequirementItemEntity item) {
        return repo.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RequirementItemEntity> list(Pageable pageable) {
        return repo.findAll(pageable);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
