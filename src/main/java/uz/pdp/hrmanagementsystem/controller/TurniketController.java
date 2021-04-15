package uz.pdp.hrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.hrmanagementsystem.entity.Turniket;
import uz.pdp.hrmanagementsystem.payload.ApiResponse;
import uz.pdp.hrmanagementsystem.service.TurniketService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/turniket")
public class TurniketController {
    @Autowired
    TurniketService turniketService;

    /*
    Keldi ketdini turniket historyga yozib qo'yuvchi yo'l
     */
    @GetMapping("/enterExit")
    public HttpEntity<?> enterExit(HttpServletRequest request){
        ApiResponse apiResponse=turniketService.enterExit(request);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /*
    turniketni ko'rish uchun yo'l
    xohlagan xodim o'z turniketini ko'rishi mumkin
     */
    @GetMapping("/getTurniket")
    public HttpEntity<?> getTurniketInfo(HttpServletRequest request){
        Turniket turniket= turniketService.getTurniketInfo(request);
        return ResponseEntity.status(turniket!=null?200:409).body(turniket);
    }
}
