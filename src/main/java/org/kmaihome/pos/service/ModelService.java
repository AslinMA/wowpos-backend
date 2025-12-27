package org.kmaihome.pos.service;

import org.kmaihome.pos.entity.ModelEntity;
import java.util.List;

public interface ModelService {
    List<ModelEntity> getAllModels();
    ModelEntity getModelById(Long id);
    ModelEntity saveModel(ModelEntity model);
    ModelEntity updateModel(ModelEntity model);
    boolean deleteModel(Long id);
}
