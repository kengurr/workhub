package com.workhub.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.workhub.entity.Employee;
import com.workhub.entity.QEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, QuerydslPredicateExecutor<Employee> {

    Optional<Employee> findByEmail(String email);

    long countByEmail(String email);

    default List<Employee> findEmployeesByName(String name) {
        QEmployee qEmployee = QEmployee.employee;
        BooleanExpression condition = qEmployee.name.containsIgnoreCase(name);
        return (List<Employee>) findAll(condition);
    }

    default List<Employee> findEmployeesByProject(Long projectId) {
        QEmployee qEmployee = QEmployee.employee;
        return (List<Employee>) findAll(qEmployee.projects.any().id.eq(projectId));
    }

}
