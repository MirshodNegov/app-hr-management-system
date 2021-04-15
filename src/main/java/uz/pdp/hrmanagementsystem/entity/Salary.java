package uz.pdp.hrmanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.hrmanagementsystem.entity.template.AbsEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Salary extends AbsEntity {

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    private Month month;

    private boolean paid = false;

}
