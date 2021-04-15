package uz.pdp.hrmanagementsystem.payload;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TaskDto {

    private String name;
    private String description;
    private Timestamp deadline;
    private String taskTakerEmail;

}
