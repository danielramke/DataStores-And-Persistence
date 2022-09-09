package de.lyth.services.impl;

import de.lyth.entities.Pet;
import de.lyth.entities.PetType;
import de.lyth.repositories.PetRepository;
import de.lyth.services.PetService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository repository;

    @Override
    public List<Pet> getPetList() {
        return repository.findAll();
    }

    @Override
    public Pet getPetByID(Long petID) {
        Optional<Pet> pet = repository.findById(petID);
        if(pet.isPresent()) return pet.get();
        throw new EntityNotFoundException("Pet with the id " + petID + " wasn't found!");
    }

    @Override
    public List<Pet> getPetListByHolderID(Long holderID) {
        return repository.findAllByCustomer_id(holderID);
    }

    @Override
    public Pet addPet(Pet pet) {
        return repository.save(pet);
    }

    @Override
    public boolean exists(String name, PetType type, LocalDate birthDay) {
        return repository.existsByNameAndTypeAndBirthDay(name, type, birthDay);
    }
}
