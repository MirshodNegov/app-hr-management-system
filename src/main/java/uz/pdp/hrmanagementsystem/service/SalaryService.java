package uz.pdp.hrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagementsystem.entity.Month;
import uz.pdp.hrmanagementsystem.entity.Salary;
import uz.pdp.hrmanagementsystem.entity.User;
import uz.pdp.hrmanagementsystem.entity.enums.MonthName;
import uz.pdp.hrmanagementsystem.payload.ApiResponse;
import uz.pdp.hrmanagementsystem.payload.SalaryDto;
import uz.pdp.hrmanagementsystem.payload.SalaryPayDto;
import uz.pdp.hrmanagementsystem.repository.MonthRepository;
import uz.pdp.hrmanagementsystem.repository.SalaryRepository;
import uz.pdp.hrmanagementsystem.repository.UserRepository;
import uz.pdp.hrmanagementsystem.security.JwtProvider;

import java.util.List;
import java.util.Optional;


@Service
public class SalaryService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    MonthRepository monthRepository;
    @Autowired
    SalaryRepository salaryRepository;


    /*
     Ish haqqini belgilash
     */
    public ApiResponse setSalary(SalaryDto salaryDto) {
        Optional<User> optionalUser = userRepository.findByEmail(salaryDto.getEmail());
        if (!optionalUser.isPresent())
            return new ApiResponse("Bunday emailli ishchi topilmadi", false);
        User user = optionalUser.get();
        Salary salary = new Salary();
        salary.setOwner(user);
        salary.setAmount(salaryDto.getAmount());
        Optional<Month> optionalMonth = monthRepository.findById(salaryDto.getMonthId());
        if (!optionalMonth.isPresent())
            return new ApiResponse("Oy id si not'gri berildi", false);
        Month month = optionalMonth.get();
        salary.setMonth(month);
        Salary savedSalary = salaryRepository.save(salary);
        return new ApiResponse("Xodimga oylik belgilandi", true);
    }

    /*
    xodimga oylikni to'lanadi , salary paid fieldi true qilib qo'yiladi
    xodimga oylik tushgani hadiqa email yuboriladi
     */
    public ApiResponse paySalary(SalaryPayDto salaryPayDto) {
        Optional<User> optionalUser = userRepository.findByEmail(salaryPayDto.getEmail());
        if (!optionalUser.isPresent())
            return new ApiResponse("Bunday emailli ishchi topilmadi", false);
        Optional<Month> optionalMonth = monthRepository.findById(salaryPayDto.getMonthId());
        if (!optionalMonth.isPresent())
            return new ApiResponse("Oy id si not'gri berildi", false);
        User owner = optionalUser.get();
        Month month = optionalMonth.get();
        Optional<Salary> optionalSalary = salaryRepository.findByOwnerAndMonth(owner, month);
        if (!optionalSalary.isPresent())
            return new ApiResponse("Bu oyda shu ishchiga oylik belgilanmagan !", false);
        Salary salary = optionalSalary.get();
        salary.setPaid(true);
        salaryRepository.save(salary);
        sendMail(owner.getEmail(), month.getMonthName().name());
        return new ApiResponse("Xodimga oylik to'landi", true);
    }

    /*
     Oylik tushgani xaqida email yuboruvchi metod
     */
    public Boolean sendMail(String sendingMail, String month) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Task@pdp.com");
            mailMessage.setTo(sendingMail);
            mailMessage.setSubject("Oylik to'landi");
            mailMessage.setText("Urra tabriklaymiz sizning "+month+" oyining oyligi hiosb raqamingizga tushirib berildi !");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    Xodim oyliklari ro'yxatini olish
     */
    public ApiResponse getSalariesEmployee(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("Bunday emailli ishchi topilmadi", false);
        User user = optionalUser.get();
        List<Salary> salaryList = salaryRepository.findAllByOwner(user);
        return new ApiResponse("Xodim oyliklari topildi",true,salaryList);
    }


    /*
    Berilgan oy bo'yicha oyliklar ro'yxati
     */
    public ApiResponse getSalariesMonth(Integer monthId) {
        Optional<Month> optionalMonth = monthRepository.findById(monthId);
        if (!optionalMonth.isPresent())
            return new ApiResponse("Oy id si not'gri berildi", false);
        Month month = optionalMonth.get();
        List<Salary> salaryList = salaryRepository.findAllByMonth(month);
        return new ApiResponse("Berilgan oy bo'yicha oyliklar topildi",true,salaryList);
    }
}
