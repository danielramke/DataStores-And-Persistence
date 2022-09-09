package de.lyth.services;

import de.lyth.entities.Pet;
import de.lyth.entities.PetType;

import java.time.LocalDate;
import java.util.List;

public interface PetService {

    List<Pet> getPetList();
    Pet getPetByID(Long petID);
    List<Pet> getPetListByHolderID(Long holderID);
    Pet addPet(Pet pet);
    boolean exists(String name, PetType type, LocalDate birthDay);

}
