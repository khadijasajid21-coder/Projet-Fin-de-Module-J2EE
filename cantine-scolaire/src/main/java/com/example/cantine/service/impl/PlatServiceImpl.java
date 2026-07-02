package com.example.cantine.service.impl;

import com.example.cantine.dto.PlatDto;
import com.example.cantine.entity.Plat;
import com.example.cantine.exception.ResourceNotFoundException;
import com.example.cantine.repository.PlatRepository;
import com.example.cantine.service.PlatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j @Service @RequiredArgsConstructor @Transactional
public class PlatServiceImpl implements PlatService {
    private final PlatRepository platRepo;
    @Value("${app.upload.dir:uploads/images}") private String uploadDir;

    @Override @Transactional(readOnly = true)
    public Page<Plat> findAll(String q, Pageable pageable) {
        if (q != null && !q.isBlank()) return platRepo.searchAll(q.trim(), pageable);
        return platRepo.findAll(pageable);
    }

    @Override @Transactional(readOnly = true)
    public List<Plat> findDisponibles() { return platRepo.findByDisponibleTrue(); }

    @Override @Transactional(readOnly = true)
    public Optional<Plat> findById(Long id) { return platRepo.findById(id); }

    @Override
    public Plat create(PlatDto dto, MultipartFile image) {
        Plat p = Plat.builder().nom(dto.getNom()).description(dto.getDescription())
            .prix(dto.getPrix()).disponible(dto.isDisponible()).build();
        if (image != null && !image.isEmpty()) p.setImage(saveImage(image));
        return platRepo.save(p);
    }

    @Override
    public Plat update(Long id, PlatDto dto, MultipartFile image) {
        Plat p = platRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plat", id));
        p.setNom(dto.getNom()); p.setDescription(dto.getDescription());
        p.setPrix(dto.getPrix()); p.setDisponible(dto.isDisponible());
        if (image != null && !image.isEmpty()) p.setImage(saveImage(image));
        return platRepo.save(p);
    }

    @Override
    public void delete(Long id) {
        if (!platRepo.existsById(id)) throw new ResourceNotFoundException("Plat", id);
        platRepo.deleteById(id);
    }

    @Override
    public void toggleDisponible(Long id) {
        Plat p = platRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plat", id));
        p.setDisponible(!p.isDisponible());
        platRepo.save(p);
    }

    private String saveImage(MultipartFile file) {
        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            log.error("Erreur sauvegarde image", e);
            return null;
        }
    }
}
