package org.kmaihome.pos.repository;

import org.kmaihome.pos.entity.ProductEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    List<ProductEntity> findByIsActiveTrue();

    @Modifying
    @Query("""
        update ProductEntity p
        set p.quantity = p.quantity - :qty
        where p.id = :id and p.quantity >= :qty
    """)
    int tryDecrement(@Param("id") Integer id, @Param("qty") int qty);

    // ✅ ADDED: Find product by brand and model
    List<ProductEntity> findByBrandAndModel(String brand, String model);
    @Modifying
    @Query("""
    update ProductEntity p
    set p.quantity = p.quantity + :qty
    where p.id = :id
""")
    int incrementStock(@Param("id") Integer id, @Param("qty") int qty);

}
