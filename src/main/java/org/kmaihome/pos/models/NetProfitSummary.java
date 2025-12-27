package org.kmaihome.pos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetProfitSummary {
    private String fromDate;
    private String toDate;
    private double salesNet;
    private double salesCOGS;
    private double salesProfit;
    private double repairRevenue;
    private double repairPartsCost;
    private double repairPartsRevenue;
    private double repairLaborRevenue;
    private double repairPartsProfit;
    private double repairProfit;
    private double damageLoss;
    private double returnLoss;
    private double totalLoss;
    private double grossProfit;
    private double trueNetProfit;
    private double lossPctOfSales;
}
