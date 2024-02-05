package com.workhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
@Entity
@Table
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_id_seq")
    @SequenceGenerator(name = "project_id_seq", sequenceName = "project_id_seq",  allocationSize=1)
    @Column(name = "ID")
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @Column(name = "technology")
    @Enumerated(EnumType.STRING)
    private Set<Technology> technology;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, mappedBy = "projects")
    private Set<Employee> employees;

    public Project() {
        this.employees = new HashSet<>();
    }

    public void addEmployee(Employee employee) {
        if (!this.employees.contains(employee)) {
            this.employees.add(employee);
            employee.getProjects().add(this);
        }
    }

    public void removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getProjects().remove(this);
    }

}
