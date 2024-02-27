package com.example.springcore.service;

import com.example.springcore.model.Trainer;
import com.example.springcore.model.Training;
import com.example.springcore.model.TrainingType;
import com.example.springcore.model.User;
import com.example.springcore.repository.TrainerDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerDao trainerDao;
    private final ProfileService profileService;
    private final AuthenticationService authenticationService;

    @Transactional
    public Trainer createTrainer(Trainer trainer) {
        User user = User.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .userName(profileService.generateUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName()))
                .password(profileService.generatePassword())
                .isActive(trainer.getUser().getIsActive())
                .build();

        Trainer trainerToSave = Trainer.builder()
                .user(user)
                .specialization(trainer.getSpecialization())
                .build();

        Trainer saved = trainerDao.save(trainerToSave);
        log.info("Created trainer: {}", trainerToSave.getUser().getId());
        return saved;
    }

    @Transactional
    public Trainer updateTrainer(Trainer trainer) {
        if (!authenticationService.isAuthenticated(trainer.getUser().getUserName())) {
            throw new RuntimeException("User is not authenticated");
        }
        Trainer updated = trainerDao.update(trainer);
        log.info("Updated trainer: {}", trainer.getUser().getId());
        return updated;
    }

    @Transactional(readOnly = true)
    public Optional<Trainer> getTrainerByUsername(String username) {
        if (!authenticationService.isAuthenticated(username)) {
            throw new RuntimeException("User is not authenticated");
        }
        Optional<Trainer> byUsername = trainerDao.getTrainerByUsername(username);
        log.info("getByUsername trainer: {}", username);
        return byUsername;
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String trainerName) {
        List<Training> trainerTrainingsByCriteria = trainerDao.getTrainerTrainingsByCriteria(username, fromDate, toDate, trainerName);
        log.info("getTrainerTrainingsByCriteria method: ");
        return trainerTrainingsByCriteria;
    }
}
