package uz.pdp.hrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagementsystem.entity.Turniket;
import uz.pdp.hrmanagementsystem.entity.TurniketHistory;
import uz.pdp.hrmanagementsystem.entity.User;
import uz.pdp.hrmanagementsystem.entity.enums.TurniketType;
import uz.pdp.hrmanagementsystem.payload.ApiResponse;
import uz.pdp.hrmanagementsystem.repository.TurniketHistoryRepository;
import uz.pdp.hrmanagementsystem.repository.TurniketRepository;
import uz.pdp.hrmanagementsystem.repository.UserRepository;
import uz.pdp.hrmanagementsystem.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class TurniketService {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    TurniketHistoryRepository turniketHistoryRepository;

    /*
    keldi ketdini turniket historyga yozib qo'yuvchi metod
     */
    public ApiResponse enterExit(HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Turniket> turniketOptional = turniketRepository.findByOwner(user);
        if (!turniketOptional.isPresent())
            return new ApiResponse("Sizga turniket berilmagan", false);
        Turniket turniket = turniketOptional.get();
        TurniketHistory lastTurniketHistory = turniketHistoryRepository.lastTurniketHistory(turniket.getId());
        TurniketHistory turniketHistory = new TurniketHistory();
        turniketHistory.setTurniket(turniket);
        if (lastTurniketHistory == null) {
            turniketHistory.setType(TurniketType.STATUS_IN);
            turniketHistoryRepository.save(turniketHistory);
            return new ApiResponse("Ishchi binoga kirdi", true);
        } else {
            if (lastTurniketHistory.getType().name().equals(TurniketType.STATUS_OUT.name())) {
                turniketHistory.setType(TurniketType.STATUS_IN);
                turniketHistoryRepository.save(turniketHistory);
                return new ApiResponse("Ishchi binoga kirdi",true);
            }else {
                turniketHistory.setType(TurniketType.STATUS_OUT);
                turniketHistoryRepository.save(turniketHistory);
                return new ApiResponse("Ishchi binodan chiqdi",true);
            }
        }
    }

    /*
    turniket info ni qaaytaruvchi metod
     */
    public Turniket getTurniketInfo(HttpServletRequest httpServletRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Turniket> byOwner = turniketRepository.findByOwner(user);
        return byOwner.orElse(null);
    }
}
