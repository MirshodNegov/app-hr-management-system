package uz.pdp.hrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagementsystem.entity.Task;
import uz.pdp.hrmanagementsystem.payload.ApiResponse;
import uz.pdp.hrmanagementsystem.payload.QueryResponseDto;
import uz.pdp.hrmanagementsystem.payload.TaskDto;
import uz.pdp.hrmanagementsystem.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    /*
    Xodimlarga topshiriq biriktirish
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER','ROLE_HR_MANAGER')")
    @PostMapping("/assign")
    public HttpEntity<?> assignTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        ApiResponse apiResponse = taskService.addTask(taskDto, request);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    /*
    Ishchi topshiriqni olganda keladigan yo'l
     */
    @GetMapping("/acceptTask")
    public HttpEntity<?> acceptTask(@RequestParam UUID id) {
        taskService.acceptTask(id);
        return ResponseEntity.ok("Task olinganligi tasdiqlandi raxmat !");
    }


    /*
    Ishchi topshiriqni bajarib bo'lganligini tasdiqlash uchun yo'l
     */
    @PostMapping("/completeTask/{id}")
    public HttpEntity<?> completeTask(@PathVariable UUID id) {
        ApiResponse apiResponse = taskService.completeTask(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    /*
     barcha tasklarni ko'rish director uchun
     */
    @PreAuthorize(value = "hasRole('ROLE_DIRECTOR')")
    @GetMapping("/getAll")
    public HttpEntity<?> getAllTasksInfo() {
        List<Task> taskList = taskService.getAllTasks();
        return ResponseEntity.ok(taskList);
    }


    /*
     bergan topshiriqlarini ko'rish managerlar uchun
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_HR_MANAGER')")
    @GetMapping("/getGivenTasks")
    public HttpEntity<?> getGivenTasks(HttpServletRequest request) {
        List<Task> taskList = taskService.getGivenTasks(request);
        return ResponseEntity.ok(taskList);
    }


    /*
     o'zlariga beriktirilgan topshiriqni ko'rish managerlar va ishchilar uchun
     emaillariga yuborilgan topshiriqni id si orqali
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_HR_MANAGER','ROLE_STAFF')")
    @GetMapping("/getMyTask/{id}")
    public HttpEntity<?> getMyTaskWithTaskId(@PathVariable UUID id) {
        Task task = taskService.getMyTask(id);
        return ResponseEntity.status(task != null ? HttpStatus.FOUND : HttpStatus.NOT_FOUND).body(task);
    }

    /*
    Bajarib bo'lingan topshiriqlarni ko'rish
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER','ROLE_HR_MANAGER')")
    @GetMapping("/getCompletedTasks")
    public HttpEntity<?> getCompletedTasks() {
        List<QueryResponseDto> taskList = taskService.getCompletedTasks();
        return ResponseEntity.ok(taskList);
    }

    /*
    Muddati o'tib ketgan ammo hali bajarib bo'linmagan topshiriqlar ro'yxati
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER','ROLE_HR_MANAGER')")
    @GetMapping("/getExpiredTasks")
    public HttpEntity<?> getExpiredTasks() {
        List<QueryResponseDto> taskList = taskService.getExpiredTasks();
        return ResponseEntity.ok(taskList);
    }
}
