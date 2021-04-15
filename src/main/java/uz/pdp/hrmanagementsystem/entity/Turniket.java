package uz.pdp.hrmanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.hrmanagementsystem.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Turniket extends AbsEntity {

    @OneToOne
    private User owner;

    private String number = UUID.randomUUID().toString();
}
