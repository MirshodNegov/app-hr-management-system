package uz.pdp.hrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagementsystem.entity.Company;

public interface CompanyRepository extends JpaRepository<Company,Integer> {

}
