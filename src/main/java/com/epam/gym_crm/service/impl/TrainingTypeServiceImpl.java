package com.epam.gym_crm.service.impl;

import com.epam.gym_crm.dto.response.TrainingTypeResponseDTO;
import com.epam.gym_crm.entity.TrainingType;
import com.epam.gym_crm.mapper.TrainingTypeMapper;
import com.epam.gym_crm.repository.TrainingTypeRepository;
import com.epam.gym_crm.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private static final Logger LOG = LogManager.getLogger(TrainingTypeServiceImpl.class);

    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingTypeMapper trainingTypeMapper;

    @Override
    public Optional<TrainingType> findByValue(String value) {
        LOG.info("Finding TrainingType by value: {}", value);
        Optional<TrainingType> trainingType = trainingTypeRepository.findByValue(value);

        trainingType.ifPresentOrElse(
                type -> LOG.info("TrainingType found: {}", type),
                () -> LOG.warn("TrainingType not found for value: {}", value)
        );

        return trainingType;
    }

    @Override
    public List<TrainingTypeResponseDTO> getAllTrainingTypes() {
        LOG.info("Fetching all TrainingTypes");
        return trainingTypeRepository.findAll().stream()
                .map(trainingTypeMapper::toTrainingTypeResponseDTO)
                .toList();
    }
}
