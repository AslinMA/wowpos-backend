package org.kmaihome.pos.service;

import org.kmaihome.pos.entity.ModelEntity;
import org.kmaihome.pos.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelRepository modelRepository;

    @Override
    public List<ModelEntity> getAllModels() {
        return modelRepository.findAll();
    }

    @Override
    public ModelEntity getModelById(Long id) {
        Optional<ModelEntity> model = modelRepository.findById(id);
        return model.orElse(null);
    }

    @Override
    public ModelEntity saveModel(ModelEntity model) {
        // Handle id = 0 or null (for new entities)
        if (model.getId() != null && model.getId() == 0) {
            model.setId(null);
        }

        // Only set createdAt for new entities
        if (model.getId() == null) {
            model.setCreatedAt(LocalDateTime.now());
        }

        return modelRepository.save(model);
    }

    @Override
    public ModelEntity updateModel(ModelEntity model) {
        if (model.getId() != null && modelRepository.existsById(model.getId())) {
            // Don't update createdAt for existing records
            ModelEntity existing = modelRepository.findById(model.getId()).orElse(null);
            if (existing != null) {
                model.setCreatedAt(existing.getCreatedAt()); // Preserve original createdAt
            }
            return modelRepository.save(model);
        }
        return null;
    }

    @Override
    public boolean deleteModel(Long id) {
        if (modelRepository.existsById(id)) {
            modelRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
