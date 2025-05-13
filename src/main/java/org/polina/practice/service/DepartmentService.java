package org.polina.practice.service;

import org.polina.practice.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    Department addDepartment(Department department);
    Department updateDepartment(Long id, Department department);
    void deleteDepartmentById(Long id);
}
