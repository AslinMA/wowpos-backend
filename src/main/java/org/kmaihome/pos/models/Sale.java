package org.kmaihome.pos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    private String transactionId;
    private String name;
    private String phoneNumber;
    private LocalDate date;
    private String warrantyPeriod;
    private List<SaleItem> items;
}
