package org.kmaihome.pos.repository;

import org.kmaihome.pos.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddItemsRepository extends JpaRepository<ProductEntity, Long> {

    /**
     * Find all distinct categories from products
     */
    @Query("SELECT DISTINCT p.category FROM ProductEntity p WHERE p.category IS NOT NULL ORDER BY p.category")
    List<String> findDistinctCategories();

    /**
     * Find all distinct brands from products
     */
    @Query("SELECT DISTINCT p.brand FROM ProductEntity p WHERE p.brand IS NOT NULL ORDER BY p.brand")
    List<String> findDistinctBrands();

    /**
     * Find models filtered by brand
     */
    @Query("SELECT DISTINCT p.model FROM ProductEntity p WHERE p.brand = :brand AND p.model IS NOT NULL ORDER BY p.model")
    List<String> findModelsByBrand(@Param("brand") String brand);

    /**
     * Find all distinct locations from products
     */
    @Query("SELECT DISTINCT p.location FROM ProductEntity p WHERE p.location IS NOT NULL ORDER BY p.location")
    List<String> findDistinctLocations();
}
