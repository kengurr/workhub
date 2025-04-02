package com.workhub.entity;

import com.workhub.dto.Technology;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "projects")
public class Project extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_id_seq")
    @SequenceGenerator(name = "project_id_seq", sequenceName = "project_id_seq",  allocationSize=1)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Set<Technology> technology = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, mappedBy = "projects")
    private Set<Employee> employees;

    public Project(@NonNull String name) {
        this.name = name;
        this.employees = new HashSet<>();
    }

}
