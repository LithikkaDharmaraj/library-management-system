package com.library.service;

import com.library.entity.Fine;
import com.library.repository.FineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FineService {

    private final FineRepository fineRepository;

    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

    public List<Fine> getAllFines() {
        return fineRepository.findAll();
    }

    public Fine getFineByIssuedId(Long issuedId) {
        return fineRepository.findByIssuedId(issuedId)
                .orElseThrow(() -> new RuntimeException("Fine record not found for issued id: " + issuedId));
    }
}
