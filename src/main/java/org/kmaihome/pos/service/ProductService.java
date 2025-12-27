package org.kmaihome.pos.service;

import org.kmaihome.pos.models.LowStockItem;
import org.kmaihome.pos.models.Product;

import java.util.List;

public interface ProductService {
    Product save(Product product);

    List<Product> retrive();
    void decrementStock(Integer productId, int qty);
    List<LowStockItem> getLowStockItems(Integer maxThreshold);
    void restock(Integer productId, int qty);


}
