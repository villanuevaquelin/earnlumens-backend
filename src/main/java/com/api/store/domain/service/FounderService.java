package com.api.store.domain.service;

import com.api.store.domain.Founder;
import com.api.store.domain.FounderCountByDate;
import com.api.store.domain.repository.FounderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FounderService {

    @Autowired
    private FounderRepository founderRepository;

    public Optional<Founder> findByEmail(String email){
        return founderRepository.findByEmail(email);
    }

    public Boolean existsByEmail(String email){
        return founderRepository.existsByEmail(email);
    }

    public long countByEntryDateBetween(LocalDateTime start, LocalDateTime end){return founderRepository.countByEntryDateBetween(start, end);}

    public Founder save(Founder founder){
        return founderRepository.save(founder);
    }

    public long count(){return founderRepository.count();}

    public List<FounderCountByDate> countFoundersGroupedByEntryDate(LocalDateTime start, LocalDateTime end) {
        return founderRepository.countFoundersGroupedByEntryDate(start, end);
    }
}
