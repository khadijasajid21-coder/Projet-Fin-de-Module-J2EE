package com.example.cantine.service;

import com.example.cantine.dto.PlatDto;
import com.example.cantine.entity.Plat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface PlatService {
    Page<Plat> findAll(String q, Pageable pageable);
    List<Plat> findDisponibles();
    Optional<Plat> findById(Long id);
    Plat create(PlatDto dto, MultipartFile image);
    Plat update(Long id, PlatDto dto, MultipartFile image);
    void delete(Long id);
    void toggleDisponible(Long id);
}
