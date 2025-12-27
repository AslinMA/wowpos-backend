package org.kmaihome.pos.repository;

import org.kmaihome.pos.entity.SaleEntity;
import org.kmaihome.pos.models.saleDetailsForDisaply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Integer> {

    Optional<SaleEntity> findByTransactionId(String transactionId);

    @Query("""
        SELECT new org.kmaihome.pos.models.saleDetailsForDisaply(
            s.productId,
            s.saleId,
            s.transactionId,
            s.name,
            s.phoneNumber,
            CAST(s.date AS string),
            CAST(s.discountedPrice AS string),
            CAST(s.sellPrice AS string),
            p.buyPrice,
            s.category,
            s.brand,
            s.model,
            CAST(s.quantity AS string)
        )
        FROM SaleEntity s
        LEFT JOIN ProductEntity p ON s.productId = p.id
        ORDER BY s.date DESC
    """)
    List<saleDetailsForDisaply> findAllSalesWithBuyPrice();

    // ✅ NEW: same DTO but filtered by date range for Net Profit
    @Query("""
        SELECT new org.kmaihome.pos.models.saleDetailsForDisaply(
            s.productId,
            s.saleId,
            s.transactionId,
            s.name,
            s.phoneNumber,
            CAST(s.date AS string),
            CAST(s.discountedPrice AS string),
            CAST(s.sellPrice AS string),
            p.buyPrice,
            s.category,
            s.brand,
            s.model,
            CAST(s.quantity AS string)
        )
        FROM SaleEntity s
        LEFT JOIN ProductEntity p ON s.productId = p.id
        WHERE s.date BETWEEN :from AND :to
        ORDER BY s.date DESC
    """)
    List<saleDetailsForDisaply> findSalesWithBuyPriceBetween(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query(value = """
        SELECT DATE(date) AS d,
               SUM(sell_price * quantity) AS revenue,
               SUM(quantity) AS items
        FROM sale
        WHERE date >= :from
        GROUP BY DATE(date)
        ORDER BY d
    """, nativeQuery = true)
    List<Object[]> sumDailyRevenueAndQty(@Param("from") LocalDateTime from);

    @Query(value = """
        SELECT SUM(sell_price * quantity) AS revenue,
               SUM(quantity) AS items
        FROM sale
        WHERE date BETWEEN :from AND :to
    """, nativeQuery = true)
    List<Object[]> sumTotalRevenueAndQty(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    List<SaleEntity> findByPhoneNumber(String phoneNumber);

    List<SaleEntity> findAllByTransactionId(String transactionId);

    List<SaleEntity> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT SUM(sell_price * quantity) FROM sale", nativeQuery = true)
    Double getTotalRevenue();

    @Query(value = """
        SELECT SUM(sell_price * quantity) 
        FROM sale 
        WHERE date BETWEEN :from AND :to
    """, nativeQuery = true)
    Double getTotalRevenueByDateRange(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query(value = "SELECT COUNT(*) FROM sale", nativeQuery = true)
    Long getTotalSalesCount();

    @Query(value = """
        SELECT category, brand, model, 
               SUM(quantity) AS total_sold,
               SUM(sell_price * quantity) AS total_revenue
        FROM sale
        GROUP BY category, brand, model
        ORDER BY total_sold DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> getTopSellingProducts(@Param("limit") int limit);

    @Query(value = """
        SELECT category, 
               SUM(quantity) AS total_sold,
               SUM(sell_price * quantity) AS total_revenue
        FROM sale
        GROUP BY category
        ORDER BY total_revenue DESC
    """, nativeQuery = true)
    List<Object[]> getSalesByCategory();

    @Query(value = """
        SELECT DATE_FORMAT(date, '%Y-%m') AS month,
               SUM(sell_price * quantity) AS revenue,
               SUM(quantity) AS items_sold,
               COUNT(DISTINCT transaction_id) AS transactions
        FROM sale
        WHERE date >= DATE_SUB(NOW(), INTERVAL :months MONTH)
        GROUP BY DATE_FORMAT(date, '%Y-%m')
        ORDER BY month DESC
    """, nativeQuery = true)
    List<Object[]> getMonthlyRevenueSummary(@Param("months") int months);

    @Query(value = """
        SELECT SUM(sell_price * quantity) AS revenue,
               COUNT(DISTINCT transaction_id) AS transactions,
               SUM(quantity) AS items_sold
        FROM sale
        WHERE DATE(date) = CURDATE()
    """, nativeQuery = true)
    List<Object[]> getTodaysSales();

    List<SaleEntity> findByWarrantyPeriod(String warrantyPeriod);

    @Query(value = """
        SELECT warranty_period, COUNT(*) AS count
        FROM sale
        GROUP BY warranty_period
    """, nativeQuery = true)
    List<Object[]> countSalesByWarrantyPeriod();
}
