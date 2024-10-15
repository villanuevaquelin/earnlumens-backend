package com.api.store.domain.repository;

import com.api.store.domain.Founder;
import com.api.store.domain.FounderCountByDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FounderRepository {

    Optional<Founder> findByEmail(String email);

    Boolean existsByEmail(String email);

    long countByEntryDateBetween(LocalDateTime start, LocalDateTime end);

    Founder save(Founder founder);

    long count();

    List<FounderCountByDate> countFoundersGroupedByEntryDate(LocalDateTime start, LocalDateTime end);
}
