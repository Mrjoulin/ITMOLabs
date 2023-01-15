package com.joulin.Lab4.services;

import com.joulin.Lab4.dto.Coordinates;
import com.joulin.Lab4.dto.HitResult;
import com.joulin.Lab4.entities.HitEntity;
import com.joulin.Lab4.repositories.HitRepository;
import com.joulin.Lab4.utils.AreaCheck;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class HitService {
    private final HitRepository hitRepository;

    public List<HitResult> findAllByOwnerId(Long ownerId) {
        return hitRepository.findAllByOwnerId(ownerId).stream()
                .map(HitEntity::toHitResult).collect(Collectors.toList());
    }

    @Transactional
    public HitResult save(Coordinates coordinates, Long ownerId) {
        boolean isHit = AreaCheck.isHit(coordinates);

        HitResult hitResult = new HitResult(coordinates, getCurrentDate(), isHit);

        hitRepository.save(new HitEntity(ownerId, hitResult));

        return hitResult;
    }

    @Transactional
    public void deleteAll(Long ownerId) {
        hitRepository.deleteAllByOwnerId(ownerId);
        // TODO mark Removed
    }

    private String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
