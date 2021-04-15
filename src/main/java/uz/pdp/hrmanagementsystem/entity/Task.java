package uz.pdp.hrmanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.hrmanagementsystem.entity.enums.TaskStatus;
import uz.pdp.hrmanagementsystem.entity.template.AbsEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task extends AbsEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private Timestamp deadline;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne(optional = false)
    private User taskTaker; // topshiriqni bajaruchisi

    @ManyToOne(optional = false)
    private User taskGiver; // topshiriqni biriktiruvchi


}
