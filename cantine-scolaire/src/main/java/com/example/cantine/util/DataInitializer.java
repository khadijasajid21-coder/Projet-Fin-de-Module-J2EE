package com.example.cantine.util;

import com.example.cantine.entity.*;
import com.example.cantine.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepo;
    private final UtilisateurRepository utilisateurRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        // Ensure roles exist
        for (RoleType rt : RoleType.values()) {
            if (roleRepo.findByNom(rt).isEmpty()) {
                roleRepo.save(Role.builder().nom(rt).build());
                log.info("Rôle créé: {}", rt);
            }
        }

        // Ensure admin exists
        if (utilisateurRepo.findByEmail("admin@cantine.com").isEmpty()) {
            Role adminRole = roleRepo.findByNom(RoleType.ROLE_ADMIN).orElseThrow();
            Utilisateur admin = Utilisateur.builder()
                .nom("Admin").prenom("Super").email("admin@cantine.com")
                .password(encoder.encode("password123"))
                .telephone("0612345678").actif(true)
                .roles(Set.of(adminRole)).build();
            utilisateurRepo.save(admin);
            log.info("Admin créé: admin@cantine.com / password123");
        }
    }
}
