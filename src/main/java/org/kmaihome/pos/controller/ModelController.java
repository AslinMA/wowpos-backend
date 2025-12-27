package org.kmaihome.pos.controller;

import org.kmaihome.pos.entity.ModelEntity;
import org.kmaihome.pos.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/models")

public class ModelController {

    @Autowired
    private ModelService modelService;

    // Get all models
    @GetMapping
    public ResponseEntity<List<ModelEntity>> getAllModels() {
        List<ModelEntity> models = modelService.getAllModels();
        return ResponseEntity.ok(models);
    }

    // Get model by ID
    @GetMapping("/{id}")
    public ResponseEntity<ModelEntity> getModelById(@PathVariable Long id) {
        ModelEntity model = modelService.getModelById(id);
        if (model != null) {
            return ResponseEntity.ok(model);
        }
        return ResponseEntity.notFound().build();
    }

    // Add new model
    @PostMapping
    public ResponseEntity<ModelEntity> addModel(@RequestBody ModelEntity model) {
        ModelEntity savedModel = modelService.saveModel(model);
        return ResponseEntity.ok(savedModel);
    }

    // Update model
    @PutMapping("/{id}")
    public ResponseEntity<ModelEntity> updateModel(@PathVariable Long id, @RequestBody ModelEntity model) {
        model.setId(id);
        ModelEntity updatedModel = modelService.updateModel(model);
        if (updatedModel != null) {
            return ResponseEntity.ok(updatedModel);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete model
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        boolean deleted = modelService.deleteModel(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
