package org.kmaihome.pos.repository;

import org.kmaihome.pos.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<ModelEntity, Long> {

    @Query("SELECT DISTINCT m.modelName FROM ModelEntity m WHERE m.brand = :brand ORDER BY m.modelName")
    List<String> findModelNamesByBrand(@Param("brand") String brand);

    @Query("SELECT DISTINCT m.brand FROM ModelEntity m ORDER BY m.brand")
    List<String> findDistinctBrands();

    @Query("SELECT DISTINCT m.category FROM ModelEntity m WHERE m.category IS NOT NULL ORDER BY m.category")
    List<String> findDistinctCategories();
}
