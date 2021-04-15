package uz.pdp.hrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagementsystem.payload.ApiResponse;
import uz.pdp.hrmanagementsystem.payload.SalaryDto;
import uz.pdp.hrmanagementsystem.payload.SalaryPayDto;
import uz.pdp.hrmanagementsystem.service.SalaryService;


@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    SalaryService salaryService;

    /*
    Xodimga oylik ish haqqini belgilash
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_HR_MANAGER','ROLE_DIRECTOR')")
    @PostMapping("/setSalary")
    public HttpEntity<?> setSalary(@RequestBody SalaryDto salaryDto){
        ApiResponse apiResponse=salaryService.setSalary(salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /*
    Xodimga oylik to'lash
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_HR_MANAGER','ROLE_DIRECTOR')")
    @PostMapping("/paySalary")
    public HttpEntity<?> paySalary(@RequestBody SalaryPayDto salaryPayDto){
        ApiResponse apiResponse=salaryService.paySalary(salaryPayDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /* yoki belgilagan oy bo’yicha
    Xodim bo’yicha  berilgan oyliklarni ko’rish
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_HR_MANAGER','ROLE_DIRECTOR')")
    @PostMapping("/getSalariesEmployee")
    public HttpEntity<?> getSalariesEmployee(@RequestBody String email){
        ApiResponse apiResponse=salaryService.getSalariesEmployee(email);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /*
     berilgan oy bo’yicha berilgan oyliklarni ko’rish
    */
    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_HR_MANAGER','ROLE_DIRECTOR')")
    @PostMapping("/getSalariesMonth")
    public HttpEntity<?> getSalariesMonth(@RequestBody Integer monthId){
        ApiResponse apiResponse=salaryService.getSalariesMonth(monthId);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
