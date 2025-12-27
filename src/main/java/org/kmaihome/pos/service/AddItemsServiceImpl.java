package org.kmaihome.pos.service;

import org.kmaihome.pos.repository.AddItemsRepository;
import org.kmaihome.pos.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AddItemsServiceImpl implements AddItemsService {

    @Autowired
    private AddItemsRepository addItemsRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Override
    public List<String> getAllCategories() {
        // Get categories from models table
        List<String> categories = modelRepository.findDistinctCategories();

        // Define default categories
        List<String> defaultCategories = Arrays.asList("Display", "Temperd", "Handfree", "Mic", "Charger");

        if (categories != null && !categories.isEmpty()) {
            Set<String> combined = new LinkedHashSet<>(defaultCategories);
            combined.addAll(categories);
            return new ArrayList<>(combined);
        }

        return defaultCategories;
    }

    @Override
    public List<String> getAllBrands() {
        // Get brands from models table
        List<String> brands = modelRepository.findDistinctBrands();

        if (brands != null && !brands.isEmpty()) {
            return brands;
        }

        // Fallback
        return Arrays.asList("Apple", "Samsung", "Redmi", "Nokia");
    }

    @Override
    public List<String> getModelsByBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Get models from models table
        List<String> models = modelRepository.findModelNamesByBrand(brand);

        if (models != null && !models.isEmpty()) {
            return models;
        }

        // Fallback (kept for backward compatibility)
        if ("Apple".equalsIgnoreCase(brand)) {
            return Arrays.asList("iPhone 6", "iPhone 7", "iPhone 8", "iPhone X", "iPhone 11", "iPhone 12");
        }

        return new ArrayList<>();
    }

    @Override
    public List<String> getAllLocations() {
        List<String> locations = addItemsRepository.findDistinctLocations();

        List<String> allLocations = Arrays.asList("Rack A", "Rack B", "Rack C", "Rack D");

        if (locations != null && !locations.isEmpty()) {
            Set<String> combined = new LinkedHashSet<>(allLocations);
            combined.addAll(locations);
            return new ArrayList<>(combined);
        }

        return allLocations;
    }
}
