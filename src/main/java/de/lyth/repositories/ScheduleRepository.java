package de.lyth.repositories;

import de.lyth.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByPetsId(Long petID);
    List<Schedule> findAllByEmployeesId(Long employeeID);
    List<Schedule> findAllByPetsCustomerId(Long customerID);

}
