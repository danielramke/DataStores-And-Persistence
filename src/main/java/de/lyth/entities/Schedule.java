package de.lyth.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Setter
@Getter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Future(message = "You can't schedule a meeting in the past.")
    private LocalDate date;

    @ElementCollection(targetClass = EmployeeSkill.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "schedule_activities",
            joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "activities")
    private Set<EmployeeSkill> activities;

    @ManyToMany
    private Set<Employee> employees;

    @ManyToMany
    private Set<Pet> pets;

}
