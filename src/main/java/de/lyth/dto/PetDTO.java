package de.lyth.dto;

import de.lyth.entities.PetType;
import lombok.Data;

import java.time.LocalDate;

/**
 * This contains all the variables which needed for pets.
 * This object does not map to the database directly.
 * @author Daniel Ramke
 */
@Data
public class PetDTO {

    private long id;
    private PetType type;
    private String name;
    private long holderID;
    private LocalDate birthDate;
    private String notes;

}
