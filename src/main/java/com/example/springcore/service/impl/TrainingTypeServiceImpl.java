package com.example.springcore.service.impl;

import com.example.springcore.model.TrainingType;
import com.example.springcore.repository.TrainingTypeRepository;
import com.example.springcore.service.TrainingTypeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public TrainingType findTrainingTypeByName(String name) {
        log.info("Enter TrainingTypeServiceImpl findTrainingTypeByName: {}", name);

        TrainingType trainingTypeByName = trainingTypeRepository.findTrainingTypeByTrainingTypeName(name)
                .orElseThrow(() -> new EntityNotFoundException("Training type with name " + name + " not found"));

        log.info("Exit TrainingTypeServiceImpl findTrainingTypeByName: {}", name);
        return trainingTypeByName;

    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingType> getTrainingTypeList() {
        log.info("Entry TrainingTypeServiceImpl getTrainingTypeList");

        List<TrainingType> trainingTypeDTOS = trainingTypeRepository.findAll();

        log.info("Exit TrainingTypeServiceImpl getTrainingTypeList");
        return trainingTypeDTOS;
    }
}
