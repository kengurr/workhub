package com.workhub.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workhub.entity.Employee;
import com.workhub.entity.QEmployee;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public EmployeeQueryDslRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<Employee> findEmployeesByName(String name) {
        QEmployee qEmployee = QEmployee.employee;
        BooleanExpression condition = qEmployee.name.containsIgnoreCase(name);
        return queryFactory.selectFrom(qEmployee)
                .where(condition)
                .fetch();
    }

    public List<Employee> findEmployeesByProject(Long projectId) {
        QEmployee qEmployee = QEmployee.employee;
        return queryFactory.selectFrom(qEmployee)
                .join(qEmployee.projects).fetchJoin()
                .where(qEmployee.projects.any().id.eq(projectId))
                .fetch();
    }

}
