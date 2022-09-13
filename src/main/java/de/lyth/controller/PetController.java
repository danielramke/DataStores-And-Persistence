package de.lyth.controller;

import de.lyth.dto.PetDTO;
import de.lyth.entities.Pet;
import de.lyth.services.PetService;
import de.lyth.services.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pet")
@AllArgsConstructor
public class PetController {

    private final ModelMapper mapper;
    private final UserService userService;
    private final PetService petService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        if(petService.exists(petDTO.getName(), petDTO.getType(), petDTO.getBirthDate()))
            throw new ValidationException(String
                    .format("Pet with the name %s, type %s and birthday %s already exists!",
                            petDTO.getName(), petDTO.getType(), petDTO.getBirthDate()));
        return convertToPetDTO(petService.addPet(convertToUsablePet(petDTO)));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        try {
            return convertToPetDTO(petService.getPetByID(petId));
        } catch (Exception exception) {
            throw new EntityNotFoundException("Pet was not found by id= " + petId);
        }
    }

    @GetMapping
    public List<PetDTO> getPets() {
        List<Pet> petList = petService.getPetList();
        List<PetDTO> petDTOList = new ArrayList<>();
        petList.forEach(pet -> petDTOList.add(convertToPetDTO(pet)));
        return petDTOList;
    }

    @GetMapping("/holder/{holderId}")
    public List<PetDTO> getPetsByHolder(@PathVariable long holderId) {
        List<Pet> petList = petService.getPetListByHolderID(holderId);
        if(petList.isEmpty())
            throw new EntityNotFoundException("Pets was not found by id= " + holderId);
        List<PetDTO> petDTOList = new ArrayList<>();
        petList.forEach(pet -> petDTOList.add(convertToPetDTO(pet)));
        return petDTOList;
    }

    private Pet convertToUsablePet(PetDTO petDTO) {
        Pet pet = mapper.map(petDTO, Pet.class);
        pet.setType(petDTO.getType());
        pet.setName(petDTO.getName());
        pet.setCustomer(userService.getCustomerByID(petDTO.getHolderID()));
        pet.setBirthDay(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());
        return pet;
    }

    private PetDTO convertToPetDTO(Pet pet) {
        PetDTO petDTO = mapper.map(pet, PetDTO.class);
        petDTO.setId(pet.getId());
        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());
        petDTO.setHolderID(pet.getCustomer().getId());
        petDTO.setBirthDate(pet.getBirthDay());
        petDTO.setNotes(pet.getNotes());
        return petDTO;
    }

}
