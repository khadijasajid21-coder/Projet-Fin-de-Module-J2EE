package com.example.cantine.service.impl;

import com.example.cantine.dto.UtilisateurDto;
import com.example.cantine.entity.*;
import com.example.cantine.exception.ResourceNotFoundException;
import com.example.cantine.repository.*;
import com.example.cantine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Set;

@Service @RequiredArgsConstructor @Transactional
public class UserServiceImpl implements UserService {
    private final UtilisateurRepository utilisateurRepo;
    private final RoleRepository roleRepo;
    private final EtudiantRepository etudiantRepo;
    private final PasswordEncoder encoder;

    @Override @Transactional(readOnly = true)
    public Page<Utilisateur> findAll(String q, Pageable pageable) {
        if (q != null && !q.isBlank()) return utilisateurRepo.search(q.trim(), pageable);
        return utilisateurRepo.findAll(pageable);
    }

    @Override @Transactional(readOnly = true)
    public Optional<Utilisateur> findById(Long id) { return utilisateurRepo.findById(id); }

    @Override @Transactional(readOnly = true)
    public Optional<Utilisateur> findByEmail(String email) { return utilisateurRepo.findByEmail(email); }

    @Override
    public Utilisateur create(UtilisateurDto dto) {
        RoleType rt = dto.getRole() != null ? dto.getRole() : RoleType.ROLE_ETUDIANT;
        Role role = roleRepo.findByNom(rt)
            .orElseThrow(() -> new ResourceNotFoundException("Role", 0L));
        Utilisateur u = Utilisateur.builder()
            .nom(dto.getNom()).prenom(dto.getPrenom()).email(dto.getEmail())
            .password(encoder.encode(dto.getPassword()))
            .telephone(dto.getTelephone()).actif(true)
            .roles(Set.of(role)).build();
        u = utilisateurRepo.save(u);
        if (rt == RoleType.ROLE_ETUDIANT && dto.getMatricule() != null) {
            Etudiant e = Etudiant.builder()
                .matricule(dto.getMatricule()).filiere(dto.getFiliere())
                .niveau(dto.getNiveau()).utilisateur(u).build();
            etudiantRepo.save(e);
        }
        return u;
    }

    @Override
    public Utilisateur update(Long id, UtilisateurDto dto) {
        Utilisateur u = utilisateurRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
        u.setNom(dto.getNom()); u.setPrenom(dto.getPrenom());
        u.setTelephone(dto.getTelephone()); u.setActif(dto.isActif());
        if (dto.getPassword() != null && !dto.getPassword().isBlank())
            u.setPassword(encoder.encode(dto.getPassword()));
        if (dto.getRole() != null) {
            Role role = roleRepo.findByNom(dto.getRole()).orElseThrow();
            u.setRoles(Set.of(role));
        }
        return utilisateurRepo.save(u);
    }

    @Override
    public void delete(Long id) {
        if (!utilisateurRepo.existsById(id)) throw new ResourceNotFoundException("Utilisateur", id);
        utilisateurRepo.deleteById(id);
    }

    @Override
    public void toggleActif(Long id) {
        Utilisateur u = utilisateurRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
        u.setActif(!u.isActif());
        utilisateurRepo.save(u);
    }
}
