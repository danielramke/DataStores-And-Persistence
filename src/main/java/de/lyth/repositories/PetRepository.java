package de.lyth.repositories;

import de.lyth.entities.Pet;
import de.lyth.entities.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByCustomer_id(Long customer_id);
    boolean existsByNameAndTypeAndBirthDay(String name, PetType type, LocalDate birthDay);

}
