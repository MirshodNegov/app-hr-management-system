package uz.pdp.hrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.hrmanagementsystem.entity.Month;


@Repository
public interface MonthRepository extends JpaRepository<Month, Integer> {

}
