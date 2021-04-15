package uz.pdp.hrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagementsystem.entity.Task;
import uz.pdp.hrmanagementsystem.entity.Turniket;
import uz.pdp.hrmanagementsystem.entity.TurniketHistory;
import uz.pdp.hrmanagementsystem.entity.User;
import uz.pdp.hrmanagementsystem.entity.enums.RoleName;
import uz.pdp.hrmanagementsystem.payload.ApiResponse;
import uz.pdp.hrmanagementsystem.payload.EmployeeInfoDto;
import uz.pdp.hrmanagementsystem.payload.ManagerDto;
import uz.pdp.hrmanagementsystem.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    TurniketHistoryRepository turniketHistoryRepository;
    @Autowired
    TaskRepository taskRepository;

    /*
    ManagerDto da ism, email , lavozim keladi unga parol generatsiya qilinib emailiga xabar yuboriladi
    Manager ga turniket yaratib biriktirib qo'yiladi
     */
    public ApiResponse addManager(ManagerDto managerDto) {
        boolean exists = userRepository.existsByEmail(managerDto.getEmail());
        if (exists)
            return new ApiResponse("This email manager already added !", false);
        User user = new User();
        user.setFullName(managerDto.getFullName());
        user.setEmail(managerDto.getEmail());
        user.setPosition(managerDto.getPosition());
        String password = Integer.toString((int) (Math.random()*1000));
        user.setPassword(passwordEncoder.encode(password));
        if (managerDto.getPosition().equalsIgnoreCase("hr manager")){
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_HR_MANAGER)));
        }else {
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_MANAGER)));
        }
        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        Turniket turniket=new Turniket();
        turniket.setOwner(savedUser);
        turniketRepository.save(turniket);
        sendMail(user.getEmail(), password);
        return new ApiResponse("Manager added !", true);
    }

    /*
     ManagerDto da ism, email , lavozim keladi unga parol generatsiya qilinib emailiga xabar yuboriladi
     Ishchiga turniket yaratilib biriktirib qo'yiladi
     */
    public ApiResponse addStaff(ManagerDto managerDto){
        boolean exists = userRepository.existsByEmail(managerDto.getEmail());
        if (exists)
            return new ApiResponse("This email employee already added !", false);
        User user = new User();
        user.setFullName(managerDto.getFullName());
        user.setEmail(managerDto.getEmail());
        user.setPosition(managerDto.getPosition());
        String password = Integer.toString((int) (Math.random()*1000));
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_STAFF)));
        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        Turniket turniket=new Turniket();
        turniket.setOwner(savedUser);
        turniketRepository.save(turniket);
        sendMail(user.getEmail(), password);
        return new ApiResponse("Employee added !", true);
    }

    /*
     Yangi qo'shilgan xodimning Emailiga parol va login yo'liga link yuboriladi
     */
    public Boolean sendMail(String sendingMail, String password) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Task@pdp.com");
            mailMessage.setTo(sendingMail);
            mailMessage.setSubject("Accountni tasdiqlash");
            mailMessage.setText("<p>Your password ->" + password + " </p><a href='http://localhost:8888/api/auth/login?'>Login</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    Xodimlar ro'yxatini olish
     */
    public List<User> getAll() {
        List<User> userList = userRepository.findAll();
        return userList;
    }

    /*
    xodimni malumotlarini ko'rish , ismi lavozimi
    ishga kelib ketgan vaqtlari , barcha tasklari
     */
    public ApiResponse getEmployeeInfo(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("Berilgan email bo'yicha xodim topilmadi",false);
        User user = optionalUser.get();
        List<TurniketHistory> turniketHistories = turniketHistoryRepository.findByTurniket_Owner(user);
        List<Task> taskList = taskRepository.findByTaskTaker(user);
        EmployeeInfoDto employeeInfoDto=new EmployeeInfoDto(user.getFullName(),user.getPosition(),turniketHistories,taskList);
        return new ApiResponse("Ischi malumotlari",true,employeeInfoDto);
    }
}
