package com.servify.superadmin;

import com.servify.superadmin.model.SuperAdminEntity;
import com.servify.superadmin.repository.SuperAdminRepository;
import com.servify.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SuperAdminInitializer  implements CommandLineRunner {
  private final PasswordEncoder passwordEncoder;
  private final SuperAdminRepository superAdminRepository;
  @Override
    public void run(String... args) throws Exception {
    if(superAdminRepository.existsByEmail("supadmins@admin.com")){
      return ;
  }
    SuperAdminEntity superAdminEntity = new SuperAdminEntity();
    superAdminEntity.setEmail("supadmins@admin.com");
    superAdminEntity.setPassword(passwordEncoder.encode("123456"));
    superAdminEntity.setRole(Role.SUPER_ADMIN);
    superAdminEntity.setGovernorate("ariana");
    superAdminEntity.setName("Iheb");
    superAdminEntity.setPhone("123456789");
    superAdminRepository.save(superAdminEntity);
  }
}
