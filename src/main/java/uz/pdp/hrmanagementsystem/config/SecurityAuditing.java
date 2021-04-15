package uz.pdp.hrmanagementsystem.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.hrmanagementsystem.entity.User;

import java.util.Optional;
import java.util.UUID;

public class SecurityAuditing implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")){
            User user=(User) authentication.getPrincipal();
            return Optional.of(user.getId());
        }
            return Optional.empty();
    }
}
