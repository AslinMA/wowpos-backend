package org.kmaihome.pos.service;

import org.kmaihome.pos.models.Sale;
import org.kmaihome.pos.models.saleDetailsForDisaply;

import java.util.List;

public interface SaleService {
    public void saveSale(Sale sale);

    List<saleDetailsForDisaply> saleRetrive();
}
