package de.lyth.services.impl;

import com.google.common.collect.Lists;
import de.lyth.dto.EmployeeRequestDTO;
import de.lyth.entities.Customer;
import de.lyth.entities.Employee;
import de.lyth.entities.EmployeeSkill;
import de.lyth.repositories.CustomerRepository;
import de.lyth.repositories.EmployeeRepository;
import de.lyth.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<Customer> getCustomerList() {
        return customerRepository.findAll();
    }

    @Override
    public List<Employee> getEmployeeByRequest(EmployeeRequestDTO requestDTO) {
        Set<Employee> requestEmployee = employeeRepository.findAllByAvailableDaysContaining(requestDTO.getDate().getDayOfWeek());
        List<Employee> employeesWithSkills = new ArrayList<>();
        requestEmployee.forEach(employee -> {
            if(employee.getSkills().containsAll(requestDTO.getSkills())) employeesWithSkills.add(employee);
        });
        return employeesWithSkills;
    }

    @Override
    public Customer getCustomerByPetID(Long petID) {
        return customerRepository.findByPetsId(petID);
    }

    @Override
    public Employee getEmployee(Long employeeID) {
        Optional<Employee> employee = employeeRepository.findById(employeeID);
        if(employee.isPresent()) return employee.get();
        throw new EntityNotFoundException("Employee with id " + employeeID + " wasn't found!");
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void addAvailableDaysByEmployeeID(Set<DayOfWeek> dayOfWeeks, Long employeeID) {
        Optional<Employee> existingEmployee = employeeRepository.findById(employeeID);
        if(existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            employee.setAvailableDays(dayOfWeeks);
            employeeRepository.save(employee);
        }
    }

    @Override
    public Customer getCustomerByID(Long holderID) {
        Optional<Customer> customer = customerRepository.findById(holderID);
        if(customer.isPresent()) return customer.get();
        throw new EntityNotFoundException("Customer with id " + holderID + " wasn't found!");
    }

    @Override
    public boolean customerExists(String name, String phoneNumber) {
        return customerRepository.existsByNameAndPhoneNumber(name, phoneNumber);
    }

    @Override
    public boolean employeeExists(String name, Set<EmployeeSkill> skills) {
        boolean result = false;
        List<Employee> employees = employeeRepository.findAllByName(name);
        if(!employees.isEmpty())
            result = employees.stream().anyMatch(employee -> new ArrayList<>(employee.getSkills()).equals(Lists.newArrayList(skills)));
        return result;
    }
}
