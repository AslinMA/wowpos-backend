package org.kmaihome.pos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repair {
    private Integer repairId;
    private String customerName;
    private String customerPhone;
    private String deviceBrand;
    private String deviceModel;
    private String issueDescription;
    private Double laborCharge;
    private Double totalCost;
    private String status;
    private List<RepairPart> parts;
}
