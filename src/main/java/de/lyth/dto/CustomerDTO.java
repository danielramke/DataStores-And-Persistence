package de.lyth.dto;

import lombok.Data;

import java.util.List;

/**
 * This contains all the variables which needed for customers.
 * This object does not map to the database directly.
 * @author Daniel Ramke
 */
@Data
public class CustomerDTO {

    private long id;
    private String name;
    private String phoneNumber;
    private String notes;
    private List<Long> petIDList;

}
