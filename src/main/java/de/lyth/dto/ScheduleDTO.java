package de.lyth.dto;

import de.lyth.entities.EmployeeSkill;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * This contains all the variables which needed for schedules.
 * This object does not map to the database directly.
 * @author Daniel Ramke
 */
@Data
public class ScheduleDTO {

    private long id;
    private List<Long> employeesIDList;
    private List<Long> petIDList;
    private LocalDate date;
    private Set<EmployeeSkill> activities;

}
