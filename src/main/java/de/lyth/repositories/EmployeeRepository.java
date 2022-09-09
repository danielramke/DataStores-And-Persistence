package de.lyth.repositories;

import de.lyth.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Set<Employee> findAllByAvailableDaysContaining(DayOfWeek dayOfWeek);
    List<Employee> findAllByName(String name);

}
