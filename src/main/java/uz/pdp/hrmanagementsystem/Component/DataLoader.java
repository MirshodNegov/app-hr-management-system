package uz.pdp.hrmanagementsystem.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.hrmanagementsystem.entity.Company;
import uz.pdp.hrmanagementsystem.entity.Role;
import uz.pdp.hrmanagementsystem.entity.User;
import uz.pdp.hrmanagementsystem.repository.CompanyRepository;
import uz.pdp.hrmanagementsystem.repository.RoleRepository;
import uz.pdp.hrmanagementsystem.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            Set<Role> roles = new HashSet<>(roleRepository.findAll());
            User user = new User("Odilbek Mirzayev","Director@gmail.com",passwordEncoder.encode("123"),roles,"Company Director",true);
            User director = userRepository.save(user);
            Company company = new Company("PDP", director);
            Company save = companyRepository.save(company);
        }
    }
}
