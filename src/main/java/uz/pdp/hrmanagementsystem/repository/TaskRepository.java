package uz.pdp.hrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.hrmanagementsystem.entity.Task;
import uz.pdp.hrmanagementsystem.entity.User;
import uz.pdp.hrmanagementsystem.payload.QueryResponseDto;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findAllByTaskGiverId(UUID taskGiver_id);

    List<Task> findByTaskTaker(User taskTaker);

    @Query(value = "select us.full_name, us.email, ts.name, ts.status,\n" +
            "CAST (ts.task_giver_id as varchar )\n" +
            "from users us join task ts on us.id=ts.task_taker_id  " +
            "where ts.status='COMPLETED'", nativeQuery = true)
    List<QueryResponseDto> findAllCompletedTasks();

    @Query(value = "select us.full_name, us.email, ts.name, ts.status,\n" +
            "CAST (ts.task_giver_id as varchar )\n" +
            "from users us join task ts on us.id=ts.task_taker_id  where ts.status='PROGRESS'\n" +
            "and ts.deadline<NOW()", nativeQuery = true)
    List<QueryResponseDto> findAllExpiredTasks();
}
