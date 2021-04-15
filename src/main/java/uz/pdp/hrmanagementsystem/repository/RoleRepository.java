package uz.pdp.hrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagementsystem.entity.Role;
import uz.pdp.hrmanagementsystem.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);
}
