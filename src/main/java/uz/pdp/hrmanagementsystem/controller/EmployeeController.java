package uz.pdp.hrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagementsystem.entity.User;
import uz.pdp.hrmanagementsystem.payload.ApiResponse;
import uz.pdp.hrmanagementsystem.payload.EmployeeInfoDto;
import uz.pdp.hrmanagementsystem.payload.ManagerDto;
import uz.pdp.hrmanagementsystem.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /*
    managerlarni qo'shish faqat direktor uchun
    managerga turniket yaratilib biriktirib qo'yiladi
     */
    @PreAuthorize(value = "hasRole('ROLE_DIRECTOR')")
    @PostMapping("/addManager")
    public HttpEntity<?> addManager(@RequestBody ManagerDto managerDto) {
        ApiResponse apiResponse = employeeService.addManager(managerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    /*
    Xodimlarni qo'shish faqat hr manager va director
    Ishchiga turniket yaratilib biriktirib qo'yiladi
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_HR_MANAGER','ROLE_DIRECTOR')")
    @PostMapping("/addStaff")
    public HttpEntity<?> addStaff(@RequestBody ManagerDto managerDto) {
        ApiResponse apiResponse = employeeService.addStaff(managerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    /*
    director va hr manager xodimlar ro'yxatini ko'rishi uchun yo'l
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_HR_MANAGER','ROLE_DIRECTOR')")
    @GetMapping("/getAllEmployees")
    public HttpEntity<?> getAll() {
        List<User> employeesList = employeeService.getAll();
        return ResponseEntity.ok(employeesList);
    }

    /*
    director va hr manager uchun istalgan bitta xodimni malumotlarini ko'rish
    ishga kelib ketgan vaqtlari , barcha tasklari
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_HR_MANAGER','ROLE_DIRECTOR')")
    @GetMapping("/getEmployeeInfo/{email}")
    public HttpEntity<?> getEmployeeInfo(@PathVariable String email) {
        ApiResponse apiResponse = employeeService.getEmployeeInfo(email);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


}
