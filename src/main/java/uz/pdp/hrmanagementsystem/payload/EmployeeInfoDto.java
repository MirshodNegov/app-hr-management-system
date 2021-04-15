package uz.pdp.hrmanagementsystem.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.hrmanagementsystem.entity.Task;
import uz.pdp.hrmanagementsystem.entity.TurniketHistory;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInfoDto {
    private String fullName;
    private String position;
    private List<TurniketHistory> turniketHistories;
    private List<Task> tasks;
}
