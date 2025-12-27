package org.kmaihome.pos.service;

import java.util.List;

public interface AddItemsService {

    /**
     * Get all unique categories from products
     * @return List of category names
     */
    List<String> getAllCategories();

    /**
     * Get all unique brands from products
     * @return List of brand names
     */
    List<String> getAllBrands();

    /**
     * Get models filtered by brand
     * @param brand Brand name to filter by
     * @return List of model names for the specified brand
     */
    List<String> getModelsByBrand(String brand);

    /**
     * Get all unique locations from products
     * @return List of location names
     */
    List<String> getAllLocations();
}
