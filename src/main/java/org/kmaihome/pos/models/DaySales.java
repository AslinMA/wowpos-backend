package org.kmaihome.pos.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DaySales {
    private String date;         // yyyy-MM-dd
    private BigDecimal revenue;  // sum(sell_price * qty)
    private Integer items;       // sum(qty)
}
