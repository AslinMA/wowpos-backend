-- V4: view for last 7 days sales aggregation
DROP VIEW IF EXISTS vw_last7_sales;
CREATE VIEW vw_last7_sales AS
SELECT DATE(date) AS d,
       SUM(sell_price * quantity) AS revenue,
       SUM(quantity) AS items
FROM sale
WHERE date >= (CURRENT_DATE - INTERVAL 6 DAY)
GROUP BY DATE(date)
ORDER BY d;
