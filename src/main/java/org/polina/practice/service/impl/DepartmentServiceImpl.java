package org.polina.practice.service.impl;

import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.Department;
import org.polina.practice.exception.DepartmentNotFoundException;
import org.polina.practice.repository.DepartmentRepository;
import org.polina.practice.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(()->
                new DepartmentNotFoundException(MessageFormat.format("Отдел с id {0} не найден!", id)));
    }

    @Override
    @Transactional
    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public Department updateDepartment(Long id, Department department) {
        Department updatedDepartment = departmentRepository.findById(id).orElseThrow(()->
                new DepartmentNotFoundException(MessageFormat.format("Отдел с id {0} не найден!", id)));
        updatedDepartment.setName(department.getName());
        return departmentRepository.save(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartmentById(Long id) {
        Department existedDepartment = departmentRepository.findById(id).orElseThrow(()->
                new DepartmentNotFoundException(MessageFormat.format("Отдел с id {0} не найден!", id)));
        departmentRepository.deleteById(existedDepartment.getId());
    }
}
