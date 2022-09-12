package de.lyth.controller;

import com.google.common.collect.Sets;
import de.lyth.dto.ScheduleDTO;
import de.lyth.entities.Employee;
import de.lyth.entities.Pet;
import de.lyth.entities.Schedule;
import de.lyth.services.PetService;
import de.lyth.services.ScheduleService;
import de.lyth.services.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/schedule")
@AllArgsConstructor
public class ScheduleController {

    private final ModelMapper mapper;
    private final UserService userService;
    private final PetService petService;
    private final ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertToScheduleDTO(scheduleService.addSchedule(convertToUsableSchedule(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> scheduleList = scheduleService.getScheduleList();
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        scheduleList.forEach(schedule -> scheduleDTOList.add(convertToScheduleDTO(schedule)));
        return scheduleDTOList;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> scheduleList = scheduleService.getScheduleListByPetID(petId);
        if(scheduleList.isEmpty())
            throw new EntityNotFoundException("Schedule not found for pet id= " + petId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        scheduleList.forEach(schedule -> scheduleDTOList.add(convertToScheduleDTO(schedule)));
        return scheduleDTOList;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> scheduleList = scheduleService.getScheduleListByEmployeeID(employeeId);
        if(scheduleList.isEmpty())
            throw new EntityNotFoundException("Schedule not found for employee id= " + employeeId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        scheduleList.forEach(schedule -> scheduleDTOList.add(convertToScheduleDTO(schedule)));
        return scheduleDTOList;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> scheduleList = scheduleService.getScheduleListByCustomerID(customerId);
        if(scheduleList.isEmpty())
            throw new EntityNotFoundException("Schedule not found for customer id= " + customerId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        scheduleList.forEach(schedule -> scheduleDTOList.add(convertToScheduleDTO(schedule)));
        return scheduleDTOList;
    }

    private Schedule convertToUsableSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = mapper.map(scheduleDTO, Schedule.class);
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());

        List<Employee> employeeList = new ArrayList<>();
        if(scheduleDTO.getEmployeesIDList() != null)
            scheduleDTO.getEmployeesIDList().forEach(id -> employeeList.add(userService.getEmployee(id)));

        Set<Employee> employeeSet = Sets.newHashSet(employeeList);
        schedule.setEmployees(employeeSet);

        List<Pet> petList = new ArrayList<>();
        if(scheduleDTO.getPetIDList() != null)
            scheduleDTO.getPetIDList().forEach(id -> petList.add(petService.getPetByID(id)));

        Set<Pet> petSet = Sets.newHashSet(petList);
        schedule.setPets(petSet);
        return schedule;
    }

    private ScheduleDTO convertToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = mapper.map(schedule, ScheduleDTO.class);
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        List<Long> employeeIds = new ArrayList<>();
        List<Long> petIds = new ArrayList<>();
        schedule.getEmployees().forEach(employee -> employeeIds.add(employee.getId()));
        schedule.getPets().forEach(pet -> petIds.add(pet.getId()));
        scheduleDTO.setEmployeesIDList(employeeIds);
        scheduleDTO.setPetIDList(petIds);
        return scheduleDTO;
    }

}
