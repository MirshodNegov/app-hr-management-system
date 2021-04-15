package uz.pdp.hrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.hrmanagementsystem.entity.Month;
import uz.pdp.hrmanagementsystem.entity.Salary;
import uz.pdp.hrmanagementsystem.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface SalaryRepository extends JpaRepository<Salary, UUID> {
    Optional<Salary> findByOwnerAndMonth(User owner, Month month);
    List<Salary> findAllByOwner(User owner);
    List<Salary> findAllByMonth(Month month);
}
