package org.kmaihome.pos.controller;

import org.kmaihome.pos.entity.RequirementItemEntity;
import org.kmaihome.pos.models.LowStockItem;
import org.kmaihome.pos.models.Product;
import org.kmaihome.pos.models.Sale;
import org.kmaihome.pos.models.saleDetailsForDisaply;
import org.kmaihome.pos.service.AddItemsService;
import org.kmaihome.pos.service.ProductService;
import org.kmaihome.pos.service.RequirementListService;
import org.kmaihome.pos.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private RequirementListService requirementListService;

    @Autowired
    private AddItemsService addItemsService;


    // Get all unique categories
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = addItemsService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Get all unique brands
    @GetMapping("/brands")
    public ResponseEntity<List<String>> getAllBrands() {
        List<String> brands = addItemsService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    // Get models filtered by brand only
    @GetMapping("/models-by-brand")
    public ResponseEntity<List<String>> getModelsByBrand(@RequestParam String brand) {
        List<String> models = addItemsService.getModelsByBrand(brand);
        return ResponseEntity.ok(models);
    }

    // Get all unique locations
    @GetMapping("/locations")
    public ResponseEntity<List<String>> getAllLocations() {
        List<String> locations = addItemsService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    // ---------- Products ----------
    @PostMapping("/product")
    public Product save(@RequestBody Product product) {
        System.out.println(product);
//        System.out.println("imwrking as conraller"+product.getDate());
        return productService.save(product);

    }

    @GetMapping("/product")
    public List<Product> retrive() {
        return productService.retrive();
    }

    // ---------- Sales ----------
    @PostMapping("/sale")
    public void saveSale(@RequestBody Sale sale) {
        System.out.println("this is received data from front end for controller " + sale);
        saleService.saveSale(sale);
    }

    @GetMapping("/saler-retrive")
    public List<saleDetailsForDisaply> saleRetrive() {
        return saleService.saleRetrive();
    }

    @PostMapping("/product/{id}/restock")
    public ResponseEntity<Void> restockProduct(
            @PathVariable Integer id,
            @RequestParam("qty") int qty
    ) {
        productService.restock(id, qty);
        return ResponseEntity.ok().build();
    }


    // ---------- Notifications ----------
    @GetMapping("/notifications/low-stock")
    public List<LowStockItem> lowStock(@RequestParam(name = "max", required = false) Integer max) {
        return productService.getLowStockItems(max);
    }

    // ---------- Requirements (single row per request) ----------
    // Save one requirement row
    @PostMapping("/requirement")
    public RequirementItemEntity saveRequirement(@RequestBody RequirementItemEntity item) {
        return requirementListService.save(item);
    }

    // List rows for the table (paged)
    @GetMapping("/requirement/items")
    public Page<RequirementItemEntity> listRequirementItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return requirementListService.list(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
        );
    }

    // Delete one row by id (remove icon)
    @DeleteMapping("/requirement/items/{id}")
    public void deleteRequirementItem(@PathVariable Long id) {
        requirementListService.delete(id);
    }
}
