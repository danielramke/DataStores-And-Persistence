package de.lyth;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.lyth.controller.PetController;
import de.lyth.controller.ScheduleController;
import de.lyth.controller.UserController;
import de.lyth.dto.*;
import de.lyth.entities.EmployeeSkill;
import de.lyth.entities.PetType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Transactional
@SpringBootTest(classes = CritterApplication.class)
class CritterApplicationTests {

    @Autowired
    private UserController userController;

    @Autowired
    private PetController petController;

    @Autowired
    private ScheduleController scheduleController;

    /**
     * This test checks for creating a new customer.
     */
    @Test
    public void checkCreateCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        CustomerDTO sameCustomerDTO = userController.saveCustomer(customerDTO);
        CustomerDTO specifiedCustomer = userController.getAllCustomers().get(0);

        //Checks the save method.
        Assertions.assertEquals(sameCustomerDTO.getName(), customerDTO.getName());
        //Checks the first element with the created element.
        Assertions.assertEquals(sameCustomerDTO.getId(), specifiedCustomer.getId());
        //Checks if the id of the element 0 greater than 0.
        Assertions.assertTrue(specifiedCustomer.getId() > 0);
    }

    /**
     * This test checks for creating a new employee.
     */
    @Test
    public void checkCreateEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        EmployeeDTO sameEmployeeDTO = userController.saveEmployee(employeeDTO);
        EmployeeDTO specifiedEmployee = userController.getEmployee(sameEmployeeDTO.getId());

        //Checks the save method.
        Assertions.assertEquals(sameEmployeeDTO.getName(), employeeDTO.getName());
        //Checks the first element with the created element.
        Assertions.assertEquals(sameEmployeeDTO.getId(), specifiedEmployee.getId());
        //Checks if the id of the element 0 greater than 0.
        Assertions.assertTrue(specifiedEmployee.getId() > 0);
    }

    /**
     * This test checks for custom add pets to a customer.
     */
    @Test
    public void checkAddPetsToCustomer() {
        CustomerDTO customerDTO = userController.saveCustomer(createCustomerDTO());

        PetDTO petDTO = createPetDTO();
        petDTO.setHolderID(customerDTO.getId());
        PetDTO interactPet = petController.savePet(petDTO);

        //Checks if the customer id stored in the pet object.
        PetDTO id_check = petController.getPet(interactPet.getId());
        Assertions.assertEquals(id_check.getId(), interactPet.getId());
        Assertions.assertEquals(id_check.getHolderID(), customerDTO.getId());

        //Check for owner can handle the pets.
        List<PetDTO> ownedPets = petController.getPetsByHolder(customerDTO.getId());
        Assertions.assertEquals(interactPet.getId(), ownedPets.get(0).getId());
        Assertions.assertEquals(interactPet.getName(), ownedPets.get(0).getName());

        //Check if the customer finally contains the pet.
        CustomerDTO storage_check = userController.getAllCustomers().get(0);
        Assertions.assertTrue(storage_check.getPetIDList() != null && storage_check.getPetIDList().size() > 0);
        Assertions.assertEquals(storage_check.getPetIDList().get(0), storage_check.getId());
    }

    /**
     * This test checks for finding pets by their holder.
     */
    @Test
    public void checkFindPetsByHolder() {
        CustomerDTO customerDTO = userController.saveCustomer(createCustomerDTO());

        PetDTO petDTO = createPetDTO();
        petDTO.setHolderID(customerDTO.getId());
        PetDTO firstPet = petController.savePet(petDTO);
        petDTO.setType(PetType.DOG);
        petDTO.setName("WauWau");
        //Save 2
        petController.savePet(petDTO);

        //Checks the size of saved pets, element 0 have the holder id and if element 0 equals firstPet.
        List<PetDTO> ownedPets = petController.getPetsByHolder(customerDTO.getId());
        Assertions.assertEquals(ownedPets.size(), 2);
        Assertions.assertEquals(ownedPets.get(0).getHolderID(), customerDTO.getId());
        Assertions.assertEquals(ownedPets.get(0).getId(), firstPet.getId());
    }

    /**
     * This test checks for finding the holder by the pets.
     */
    @Test
    public void checkFindHolderByPet() {
        CustomerDTO customerDTO = userController.saveCustomer(createCustomerDTO());

        PetDTO petDTO = createPetDTO();
        petDTO.setHolderID(customerDTO.getId());
        PetDTO interactPet = petController.savePet(petDTO);

        //Checks the holder with the created customer and petID with the interactPet id.
        CustomerDTO holder = userController.getHolderByPetID(interactPet.getId());
        Assertions.assertEquals(holder.getId(), customerDTO.getId());
        Assertions.assertEquals(holder.getPetIDList().get(0), interactPet.getId());
    }

    /**
     * This test checks if we can change the current available days.
     */
    @Test
    public void checkChangeEmployeeAvailability() {
        EmployeeDTO employeeDTO = userController.saveEmployee(createEmployeeDTO());
        //Check if the created employee have null days.
        Assertions.assertNull(employeeDTO.getAvailableDays());

        Set<DayOfWeek> availableDays = Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.THURSDAY, DayOfWeek.WEDNESDAY);
        userController.setAvailable(availableDays, employeeDTO.getId());

        //Checks the available days with the days of the interactEmployee.
        EmployeeDTO interactEmployee = userController.getEmployee(employeeDTO.getId());
        Assertions.assertEquals(availableDays, interactEmployee.getAvailableDays());
    }

    /**
     * This test checks if we can locate an employee by the service and time.
     */
    @Test
    public void checkFindEmployeeByServiceAndTime() {
        EmployeeDTO employee_01 = createEmployeeDTO();
        EmployeeDTO employee_02 = createEmployeeDTO();
        EmployeeDTO employee_03 = createEmployeeDTO();

        employee_01.setAvailableDays(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
        employee_02.setAvailableDays(Sets.newHashSet(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
        employee_03.setAvailableDays(Sets.newHashSet(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

        employee_01.setSkills(Sets.newHashSet(EmployeeSkill.MEDICATING, EmployeeSkill.PETTING));
        employee_02.setSkills(Sets.newHashSet(EmployeeSkill.PETTING, EmployeeSkill.SHAVING));
        employee_03.setSkills(Sets.newHashSet(EmployeeSkill.WALKING, EmployeeSkill.FEEDING));

        EmployeeDTO saved_employee_01 = userController.saveEmployee(employee_01);
        EmployeeDTO saved_employee_02 = userController.saveEmployee(employee_02);
        EmployeeDTO saved_employee_03 = userController.saveEmployee(employee_03);

        //Check a request which matches employees 1 or 2.
        EmployeeRequestDTO request_01 = new EmployeeRequestDTO();
        request_01.setDate(LocalDate.of(2022, 9, 14));
        request_01.setSkills(Sets.newHashSet(EmployeeSkill.PETTING));

        Set<Long> employee_01_IDs = userController.findEmployeesForService(request_01)
                .stream().map(EmployeeDTO::getId).collect(Collectors.toSet());
        Set<Long> employee_01_IDs_expected = Sets.newHashSet(saved_employee_01.getId(), saved_employee_02.getId());
        Assertions.assertEquals(employee_01_IDs, employee_01_IDs_expected);

        //Check a request that matches only employee 3.
        EmployeeRequestDTO request_02 = new EmployeeRequestDTO();
        request_02.setDate(LocalDate.of(2022, 9, 16));
        request_02.setSkills(Sets.newHashSet(EmployeeSkill.WALKING, EmployeeSkill.FEEDING));

        Set<Long> employee_02_IDs = userController.findEmployeesForService(request_02)
                .stream().map(EmployeeDTO::getId).collect(Collectors.toSet());
        Set<Long> employee_02_IDs_expected = Sets.newHashSet(saved_employee_03.getId());
        Assertions.assertEquals(employee_02_IDs, employee_02_IDs_expected);
    }

    /**
     * This test checks the schedules and his services.
     */
    @Test
    public void checkSchedulePetsForServiceWithEmployee() {
        EmployeeDTO employeeTmp = createEmployeeDTO();
        employeeTmp.setAvailableDays(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
        EmployeeDTO employeeDTO = userController.saveEmployee(employeeTmp);
        CustomerDTO customerDTO = userController.saveCustomer(createCustomerDTO());

        PetDTO petTmp = createPetDTO();
        petTmp.setHolderID(customerDTO.getId());
        PetDTO petDTO = petController.savePet(petTmp);

        LocalDate date = LocalDate.of(2022, 9, 13);
        List<Long> petList = Lists.newArrayList(petDTO.getId());
        List<Long> employeeList = Lists.newArrayList(employeeDTO.getId());
        Set<EmployeeSkill> skillSet = Sets.newHashSet(EmployeeSkill.PETTING);

        scheduleController.createSchedule(createScheduleDTO(petList, employeeList, date, skillSet));
        ScheduleDTO scheduleDTO = scheduleController.getAllSchedules().get(0);

        Assertions.assertEquals(scheduleDTO.getActivities(), skillSet);
        Assertions.assertEquals(scheduleDTO.getDate(), date);
        Assertions.assertEquals(scheduleDTO.getEmployeesIDList(), employeeList);
        Assertions.assertEquals(scheduleDTO.getPetIDList(), petList);
    }

    /**
     * This test the finding of schedules by their entities.
     */
    @Test
    public void checkFindScheduleByEntities() {
        ScheduleDTO schedule_01 = populateSchedule(1, 2, LocalDate.of(2022, 10, 17), Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.WALKING));
        ScheduleDTO schedule_02 = populateSchedule(3, 1, LocalDate.of(2022, 10, 18), Sets.newHashSet(EmployeeSkill.PETTING));

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setEmployeesIDList(schedule_01.getEmployeesIDList());
        scheduleDTO.setPetIDList(schedule_02.getPetIDList());
        scheduleDTO.setActivities(Sets.newHashSet(EmployeeSkill.SHAVING, EmployeeSkill.PETTING));
        scheduleDTO.setDate(LocalDate.of(2022, 10, 20));
        scheduleController.createSchedule(scheduleDTO);

        /*
            In this section we will test the 3 schedules and his attributes.
         */
        List<ScheduleDTO> scheduleList_emp_01 = scheduleController.getScheduleForEmployee(schedule_01.getEmployeesIDList().get(0));
        compareSchedules(schedule_01, scheduleList_emp_01.get(0));
        compareSchedules(scheduleDTO, scheduleList_emp_01.get(1));

        List<ScheduleDTO> scheduleList_emp_02 = scheduleController.getScheduleForEmployee(schedule_02.getEmployeesIDList().get(0));
        compareSchedules(schedule_02, scheduleList_emp_02.get(0));

        List<ScheduleDTO> scheduleList_pet_01 = scheduleController.getScheduleForPet(schedule_01.getPetIDList().get(0));
        compareSchedules(schedule_01, scheduleList_pet_01.get(0));

        List<ScheduleDTO> scheduleList_pet_02 = scheduleController.getScheduleForPet(schedule_02.getPetIDList().get(0));
        compareSchedules(schedule_02, scheduleList_pet_02.get(0));
        compareSchedules(scheduleDTO, scheduleList_pet_02.get(1));

        List<ScheduleDTO> scheduleList_holder_01 = scheduleController.getScheduleForCustomer(userController.getHolderByPetID(schedule_01.getPetIDList().get(0)).getId());
        compareSchedules(schedule_01, scheduleList_holder_01.get(0));

        List<ScheduleDTO> scheduleList_holder_02 = scheduleController.getScheduleForCustomer(userController.getHolderByPetID(schedule_02.getPetIDList().get(0)).getId());
        compareSchedules(schedule_02, scheduleList_holder_02.get(0));
        compareSchedules(scheduleDTO, scheduleList_holder_02.get(1));
    }

    private static final Random random = new Random();

    /**
     * This method generates a random employee DTO with a random string as name.
     * The random is needed because we can't have duplicated names.
     * @return the created employee DTO.
     */
    private static EmployeeDTO createEmployeeDTO() {
        byte[] letters = new byte[5];
        random.nextBytes(letters);
        String generatedName = new String(letters, StandardCharsets.UTF_8);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(generatedName);
        employeeDTO.setSkills(Sets.newHashSet(EmployeeSkill.MEDICATING, EmployeeSkill.FEEDING));
        return employeeDTO;
    }

    /**
     * This method creates a new customer DTO with a random phone number.
     * @return the created customer DTO.
     */
    private static CustomerDTO createCustomerDTO() {
        int phoneNumber = random.nextInt(9_000_000) + 1_000_000_000;
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("");
        customerDTO.setPhoneNumber(String.valueOf(phoneNumber));
        return customerDTO;
    }

    /**
     * This method creates a new pet DTO without a birthday.
     * @return the created pet DTO.
     */
    private static PetDTO createPetDTO() {
        PetDTO petDTO = new PetDTO();
        petDTO.setName("DebugPet");
        petDTO.setType(PetType.CAT);
        return petDTO;
    }

    /**
     * This method creates a new schedule DTO.
     * @param petIDList the id list of pets.
     * @param employeeIDList the id list of employees.
     * @param date the schedule date.
     * @param activities the activities for the schedule.
     * @return the created schedule DTO.
     */
    private static ScheduleDTO createScheduleDTO(List<Long> petIDList, List<Long> employeeIDList, LocalDate date, Set<EmployeeSkill> activities) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setPetIDList(petIDList);
        scheduleDTO.setEmployeesIDList(employeeIDList);
        scheduleDTO.setDate(date);
        scheduleDTO.setActivities(activities);
        return scheduleDTO;
    }

    /**
     * This method checked the schedules of equals his content.
     * @param first the first schedule object.
     * @param second the second schedule object.
     */
    private static void compareSchedules(ScheduleDTO first, ScheduleDTO second) {
        List<Long> sortedFirstPetIds = first.getPetIDList();
        sortedFirstPetIds.sort(Comparator.naturalOrder());

        List<Long> sortedSecondPetIds = second.getPetIDList();
        sortedSecondPetIds.sort(Comparator.naturalOrder());

        List<Long> sortedFirstEmployeeIds = first.getEmployeesIDList();
        sortedFirstEmployeeIds.sort(Comparator.naturalOrder());

        List<Long> sortedSecondEmployeeIds = second.getEmployeesIDList();
        sortedSecondEmployeeIds.sort(Comparator.naturalOrder());

        Assertions.assertEquals(sortedFirstPetIds, sortedSecondPetIds);
        Assertions.assertEquals(first.getActivities(), second.getActivities());
        Assertions.assertEquals(sortedFirstEmployeeIds, sortedSecondEmployeeIds);
        Assertions.assertEquals(first.getDate(), second.getDate());
    }

    /**
     * This method create pets by random birthdays.
     * @param numberOfEmployees the number of employees.
     * @param numberOfPets the number of pets.
     * @param date the schedule date.
     * @param activities the schedule activities.
     * @return the created schedule DTO.
     */
    private ScheduleDTO populateSchedule(int numberOfEmployees, int numberOfPets, LocalDate date, Set<EmployeeSkill> activities) {
        List<Long> employeeIDList = IntStream.range(0, numberOfEmployees)
                .mapToObj(i -> createEmployeeDTO())
                .map(e -> {
                    e.setSkills(activities);
                    e.setAvailableDays(Sets.newHashSet(date.getDayOfWeek()));
                    return userController.saveEmployee(e).getId();
                }).toList();

        CustomerDTO customerDTO = userController.saveCustomer(createCustomerDTO());

        List<Long> petIDList = IntStream.range(0, numberOfPets)
                .mapToObj(i -> createPetDTO())
                .map(p -> {
                    p.setBirthDate(LocalDate.of(random.nextInt(2021) + 1, random.nextInt(11) + 1, random.nextInt(27) + 1));
                    p.setHolderID(customerDTO.getId());
                    return petController.savePet(p).getId();
                }).toList();
        return scheduleController.createSchedule(createScheduleDTO(petIDList, employeeIDList, date, activities));
    }

}
