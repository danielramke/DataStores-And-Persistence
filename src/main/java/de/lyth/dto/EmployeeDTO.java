package de.lyth.dto;

import de.lyth.entities.EmployeeSkill;
import lombok.Data;

import java.time.DayOfWeek;
import java.util.Set;

/**
 * This contains all the variables which needed for employees.
 * This object does not map to the database directly.
 * @author Daniel Ramke
 */
@Data
public class EmployeeDTO {

    private long id;
    private String name;
    private Set<EmployeeSkill> skills;
    private Set<DayOfWeek> availableDays;

}
