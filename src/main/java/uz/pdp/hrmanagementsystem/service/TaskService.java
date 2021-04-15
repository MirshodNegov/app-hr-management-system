package uz.pdp.hrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagementsystem.entity.Task;
import uz.pdp.hrmanagementsystem.entity.User;
import uz.pdp.hrmanagementsystem.entity.enums.RoleName;
import uz.pdp.hrmanagementsystem.entity.enums.TaskStatus;
import uz.pdp.hrmanagementsystem.payload.ApiResponse;
import uz.pdp.hrmanagementsystem.payload.QueryResponseDto;
import uz.pdp.hrmanagementsystem.payload.TaskDto;
import uz.pdp.hrmanagementsystem.repository.TaskRepository;
import uz.pdp.hrmanagementsystem.repository.UserRepository;
import uz.pdp.hrmanagementsystem.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JavaMailSender javaMailSender;

    /*
    TaskDto da topshiriq nomi , izohi , muddati , bajaruvchi emaili keladi
     */
    public ApiResponse addTask(TaskDto taskDto, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String email = jwtProvider.getEmailFromToken(token);
        Optional<User> userOptional = userRepository.findByEmail(email);
        User taskGiver = userOptional.get();
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setDeadline(taskDto.getDeadline());
        task.setStatus(TaskStatus.NEW);
        task.setTaskGiver(taskGiver);
        Optional<User> optionalUser = userRepository.findByEmail(taskDto.getTaskTakerEmail());
        if (!optionalUser.isPresent())
            return new ApiResponse("Task taker email wrong !", false);
        User taskTaker = optionalUser.get();
        if (taskTaker.getRoles().contains(RoleName.ROLE_DIRECTOR))
            return new ApiResponse("No one can give tasks to the director !", false);
        task.setTaskTaker(taskTaker);
        Task newTask = taskRepository.save(task);
        sendMail(taskTaker.getEmail(), newTask.getId(), newTask.getDeadline());
        return new ApiResponse("Task successfully assigned !", true);
    }

    /*
    Topshiriq berilganligi haqida email yuborish
     */
    public Boolean sendMail(String email, UUID id, Timestamp deadline) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Task@pdp.com");
            mailMessage.setTo(email);
            mailMessage.setSubject("Your new Work");
            mailMessage.setText("Sizga yangi  topshiriq biriktirildi ! Topshiriq id si : " + id + " Muddat " +
                    deadline + " gacha, Task id orqali topshiriq haqida to'liq ma'lumot olishingiz mumkin" +
                    "Quyidagi link orqali taskni olganligingizni tasdiqlang ! " +
                    "<a href='http://localhost:8888/api/task/acceptTask?id=" + id + "'>Tasdiqlash</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    Ishchi taskni olganligini tasdiqlaganda task mode ni in proggress qilib qo'yadi
     */
    public void acceptTask(UUID uuid) {
        Optional<Task> optionalTask = taskRepository.findById(uuid);
        Task task = optionalTask.get();
        task.setStatus(TaskStatus.PROGRESS);
        taskRepository.save(task);
    }

    /*
    Barcha taskni qaytaruvchi metod
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /*
    Taskni bajarib bo'linganda statusni completed qilib bu haqda task giverni emailiga xabar yuboradi
     */
    public ApiResponse completeTask(UUID id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task id wrong", false);
        Task task = optionalTask.get();
        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);
        User taskGiver = task.getTaskGiver();
        String email = taskGiver.getEmail();
        sendMailAboutCompleted(email, id);
        return new ApiResponse("Topshiriq bajarilgnaligi haqida xabar yuborildi", true);
    }

    /*
    Topshiriq berilganligi haqida email yuborish
     */
    public Boolean sendMailAboutCompleted(String email, UUID id) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Task@pdp.com");
            mailMessage.setTo(email);
            mailMessage.setSubject("Task completed");
            mailMessage.setText("Siz bergan topshiriq bajarildi ! Topshiriq id si : " + id);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    Managerlar bergan topshiriqlarini qayataradi
     */
    public List<Task> getGivenTasks(HttpServletRequest request) {
        User user = (User) request.getUserPrincipal();
        return taskRepository.findAllByTaskGiverId(user.getId());
    }

    /*
    topshiriq ni id si orqali ko'rish
     */
    public Task getMyTask(UUID id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.orElse(null);
    }

    /*
    bajarib bo'lingan topshiriqlar haqida malumot olish
     */
    public List<QueryResponseDto> getCompletedTasks() {
        return taskRepository.findAllCompletedTasks();
    }

    /*
    muddati o'tib ketgan topshiriqlar ro'yxati
     */
    public List<QueryResponseDto> getExpiredTasks() {
        return taskRepository.findAllExpiredTasks();
    }
}
