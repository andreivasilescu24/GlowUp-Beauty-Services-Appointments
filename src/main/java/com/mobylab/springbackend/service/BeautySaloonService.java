package com.mobylab.springbackend.service;

import com.mobylab.springbackend.repository.BeautySaloonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BeautySaloonService {
    private final BeautySaloonRepository beautySaloonRepository;

    public BeautySaloonService(BeautySaloonRepository beautySaloonRepository) {
        this.beautySaloonRepository = beautySaloonRepository;
    }
}
