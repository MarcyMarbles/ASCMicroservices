package kz.saya.finals.userservice.Init;

import kz.saya.sbasecore.Entity.NotPersistent.Langs;
import kz.saya.sbasecore.Entity.Roles;
import kz.saya.sbasecore.Entity.User;
import kz.saya.sbasecore.Repositories.RoleRepository;
import kz.saya.sbasecore.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartupValidator implements ApplicationRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Value("${ADMIN_PASSWORD_HASH}")
    private String adminHash;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByLogin("admin").isEmpty()) {
            User user = new User();
            user.setLogin("admin");
            user.setUsername("admin");
            user.setPassword(adminHash);
            user.setLang(Langs.RU);
            Roles role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRoles(Set.of(role));
            userRepository.save(user);
        }
        if (roleRepository.findByName("ROLE_NOTIFICATION_SEND").isEmpty()) {
            Roles role = new Roles();
            role.setName("ROLE_NOTIFICATION_SEND");
            role.setDescription("Роль для отправки уведомлений");
            roleRepository.save(role);
        }
    }
}
