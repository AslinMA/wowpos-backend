package org.kmaihome.pos.models;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RequirementList {
    private Integer id;
    private LocalDate date;
    private String category;
    private String brand;
    private String model;
    private Integer quantity;
    private BigDecimal Price;
    private String Description;


}
