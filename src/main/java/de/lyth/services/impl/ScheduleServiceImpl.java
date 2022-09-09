package de.lyth.services.impl;

import de.lyth.entities.Schedule;
import de.lyth.repositories.ScheduleRepository;
import de.lyth.services.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository repository;

    @Override
    public List<Schedule> getScheduleList() {
        return repository.findAll();
    }

    @Override
    public List<Schedule> getScheduleListByPetID(Long petID) {
        return repository.findAllByPetsId(petID);
    }

    @Override
    public List<Schedule> getScheduleListByEmployeeID(Long employeeID) {
        return repository.findAllByEmployeesId(employeeID);
    }

    @Override
    public List<Schedule> getScheduleListByCustomerID(Long customerID) {
        return repository.findAllByPetsCustomerId(customerID);
    }

    @Override
    public Schedule addSchedule(Schedule schedule) {
        return repository.save(schedule);
    }
}
