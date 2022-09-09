package de.lyth.services;

import de.lyth.dto.EmployeeRequestDTO;
import de.lyth.entities.Customer;
import de.lyth.entities.Employee;
import de.lyth.entities.EmployeeSkill;

import javax.validation.constraints.Pattern;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface UserService {

    List<Customer> getCustomerList();
    List<Employee> getEmployeeByRequest(EmployeeRequestDTO requestDTO);
    Customer getCustomerByPetID(Long petID);
    Employee getEmployee(Long employeeID);
    Customer addCustomer(Customer customer);
    Employee addEmployee(Employee employee);
    void addAvailableDaysByEmployeeID(Set<DayOfWeek> dayOfWeeks, Long employeeID);
    Customer getCustomerByID(Long holderID);
    boolean customerExists(String name,
                           @Pattern(regexp = "(^$|[0-9]{10})", message = "Please set a valid Phone Number with max 10 Digit") String phoneNumber);
    boolean employeeExists(String name, Set<EmployeeSkill> skills);

}
