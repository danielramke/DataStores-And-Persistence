package de.lyth.dto;

import de.lyth.entities.EmployeeSkill;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * This class exist to find available employees by skills.
 * This object does not map to the database directly.
 * @author Daniel Ramke
 */
@Data
public class EmployeeRequestDTO {

    private Set<EmployeeSkill> skills;
    private LocalDate date;

}
