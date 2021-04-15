package uz.pdp.hrmanagementsystem.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.hrmanagementsystem.entity.TurniketHistory;
import uz.pdp.hrmanagementsystem.entity.User;

import java.util.List;
import java.util.UUID;

public interface TurniketHistoryRepository extends JpaRepository<TurniketHistory, UUID> {

    @Query(value = "select * from turniket_history as th where th.turniket_id=:turniketId order by timestamp desc limit 1",nativeQuery = true)
    TurniketHistory lastTurniketHistory(UUID turniketId);

    List<TurniketHistory> findByTurniket_Owner(User turniket_owner);
}
