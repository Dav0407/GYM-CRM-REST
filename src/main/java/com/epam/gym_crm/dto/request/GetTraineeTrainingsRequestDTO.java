package com.epam.gym_crm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTraineeTrainingsRequestDTO {
    private String traineeUsername;
    private String trainerUsername;
    private Date from;
    private Date to;
    private String trainingType;
}
