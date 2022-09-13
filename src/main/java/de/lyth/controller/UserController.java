package de.lyth.controller;

import de.lyth.dto.CustomerDTO;
import de.lyth.dto.EmployeeDTO;
import de.lyth.dto.EmployeeRequestDTO;
import de.lyth.entities.Customer;
import de.lyth.entities.Employee;
import de.lyth.entities.Pet;
import de.lyth.services.PetService;
import de.lyth.services.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final PetService petService;
    private final ModelMapper mapper;

    /* ###############################################
     *                    Customers
     * ############################################### */

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        if(userService.customerExists(customerDTO.getName(), customerDTO.getPhoneNumber()))
            throw new EntityExistsException(String
                    .format("Customer with the name %s and phone number %s already exists!",
                            customerDTO.getName(), customerDTO.getPhoneNumber()));
        return convertToCustomerDTO(userService.addCustomer(convertToUsableCustomer(customerDTO)));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customerList = userService.getCustomerList();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customerList.forEach(customer -> customerDTOList.add(convertToCustomerDTO(customer)));
        return customerDTOList;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getHolderByPetID(@PathVariable long petId) {
        try {
            return convertToCustomerDTO(userService.getCustomerByPetID(petId));
        } catch (Exception exception) {
            throw new EntityNotFoundException("Customer was not found by pedID= " + petId);
        }
    }

    private Customer convertToUsableCustomer(CustomerDTO customerDTO) {
        List<Pet> pets = new ArrayList<>();
        Customer customer = mapper.map(customerDTO, Customer.class);
        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());
        if(customerDTO.getPetIDList() != null) {
            customerDTO.getPetIDList().forEach(petId -> {
                Pet pet = petService.getPetByID(petId);
                pets.add(pet);
                customer.setPets(pets);
            });
            return customer;
        }
        customer.setPets(petService.getPetListByHolderID(customerDTO.getId()));
        return customer;
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = mapper.map(customer, CustomerDTO.class);
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        List<Long> petIdList = new ArrayList<>();
        if(!customer.getPets().isEmpty()) {
            List<Long> finalPetIdList = petIdList;
            customer.getPets().forEach(pet -> finalPetIdList.add(pet.getId()));
            customerDTO.setPetIDList(petIdList);
            return customerDTO;
        }
        petIdList = petService.getPetListByHolderID(customer.getId()).stream()
                .map(Pet::getId)
                .collect(Collectors.toList());
        customerDTO.setPetIDList(petIdList);
        return customerDTO;
    }

    /* ###############################################
     *                    Employees
     * ############################################### */

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        if(userService.employeeExists(employeeDTO.getName(), employeeDTO.getSkills()))
            throw new EntityExistsException(String
                    .format("Employee with the name %s and skills %s already exists!",
                            employeeDTO.getName(), employeeDTO.getSkills()));
        return convertToEmployeeDTO(userService.addEmployee(convertToUsableEmployee(employeeDTO)));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        try {
            return convertToEmployeeDTO(userService.getEmployee(employeeId));
        } catch (Exception exception) {
            throw new EntityNotFoundException("Employee with the id= " + employeeId + " wasn't found!");
        }
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailable(@RequestBody Set<DayOfWeek> availableDays, @PathVariable long employeeId) {
        userService.addAvailableDaysByEmployeeID(availableDays, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        try {
            List<Employee> employeeList = userService.getEmployeeByRequest(employeeRequestDTO);
            employeeList.forEach(employee -> employeeDTOList.add(convertToEmployeeDTO(employee)));
        } catch (Exception exception) {
            throw new EntityNotFoundException("List of employees was not correct!");
        }
        return employeeDTOList;
    }

    private Employee convertToUsableEmployee(EmployeeDTO employeeDTO) {
        Employee employee = mapper.map(employeeDTO, Employee.class);
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee.setAvailableDays(employeeDTO.getAvailableDays());
        return employee;
    }

    private EmployeeDTO convertToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = mapper.map(employee, EmployeeDTO.class);
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setAvailableDays(employee.getAvailableDays());
        return employeeDTO;
    }

}
