package org.polina.practice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.AddEmployeeRequest;
import org.polina.practice.dto.UpsertEmployeeRequest;
import org.polina.practice.entity.Department;
import org.polina.practice.entity.Employee;
import org.polina.practice.exception.DepartmentNotFoundException;
import org.polina.practice.exception.EmployeeNotFoundException;
import org.polina.practice.projection.EmployeeProjection;
import org.polina.practice.repository.EmployeeRepository;
import org.polina.practice.repository.DepartmentRepository;
import org.polina.practice.service.EmployeeService;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeProjection> getAllEmployeeProjections() {
        return employeeRepository.findAllProjectedBy();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(()->
                new EmployeeNotFoundException(MessageFormat
                        .format("Сотрудник с id {0} не найден!", id)));
    }

    @Override
    @Transactional
    public Employee addEmployee(AddEmployeeRequest request) {
        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPosition(request.getPosition());
        Department department = departmentRepository.findById(request.getDepartmentId()).orElseThrow(()->
                new DepartmentNotFoundException(MessageFormat.format("Отдел с id {0} не найден!", request.getDepartmentId())));
        employee.setDepartment(department);
        employee.setSalary(request.getSalary());
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee updateEmployee(Long id, UpsertEmployeeRequest request) {
        Employee updatedEmployee = employeeRepository.findById(id).orElseThrow(()->
                new EmployeeNotFoundException(MessageFormat
                        .format("Сотрудник с id {0} не найден!", id)));
        updatedEmployee.setFirstName(request.getFirstName());
        updatedEmployee.setLastName(request.getLastName());
        updatedEmployee.setSalary(request.getSalary());
        updatedEmployee.setPosition(request.getPosition());
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(()->
                new EmployeeNotFoundException(MessageFormat
                        .format("Сотрудник с {0} не найден!", id)));
        Department department = employee.getDepartment();
        department.getEmployees().remove(employee);
        departmentRepository.save(department);
        employeeRepository.deleteById(employee.getId());
    }
}
