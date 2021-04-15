package uz.pdp.hrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagementsystem.entity.Turniket;
import uz.pdp.hrmanagementsystem.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface TurniketRepository extends JpaRepository<Turniket, UUID> {
    boolean existsByOwner(User owner);
    Optional<Turniket> findByOwner(User owner);
}
