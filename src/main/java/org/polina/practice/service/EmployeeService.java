package org.polina.practice.service;

import org.polina.practice.dto.AddEmployeeRequest;
import org.polina.practice.dto.UpsertEmployeeRequest;
import org.polina.practice.entity.Employee;
import org.polina.practice.projection.EmployeeProjection;

import java.util.List;

public interface EmployeeService {
    List<EmployeeProjection> getAllEmployeeProjections();
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee addEmployee(AddEmployeeRequest request);
    Employee updateEmployee(Long id, UpsertEmployeeRequest employee);
    void deleteEmployeeById(Long id);
}
