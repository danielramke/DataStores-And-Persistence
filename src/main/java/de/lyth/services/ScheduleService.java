package de.lyth.services;

import de.lyth.entities.Schedule;

import java.util.List;

public interface ScheduleService {

    List<Schedule> getScheduleList();
    List<Schedule> getScheduleListByPetID(Long petID);
    List<Schedule> getScheduleListByEmployeeID(Long employeeID);
    List<Schedule> getScheduleListByCustomerID(Long customerID);
    Schedule addSchedule(Schedule schedule);

}
