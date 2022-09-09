package de.lyth.repositories;

import de.lyth.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Pattern;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByPetsId(Long petsID);
    boolean existsByNameAndPhoneNumber(String name,
                                       @Pattern(regexp = "(^$|[0-9]{10})", message = "Please set a valid Phone Number with max 10 Digit") String phoneNumber);

}
