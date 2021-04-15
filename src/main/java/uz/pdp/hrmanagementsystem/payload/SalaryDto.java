package uz.pdp.hrmanagementsystem.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryDto {
    private String email;
    private Double amount;
    private Integer monthId;
}
