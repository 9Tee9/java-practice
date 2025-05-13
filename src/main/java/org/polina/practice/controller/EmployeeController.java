package org.polina.practice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.AddEmployeeRequest;
import org.polina.practice.dto.UpsertEmployeeRequest;
import org.polina.practice.entity.Employee;
import org.polina.practice.projection.EmployeeProjection;
import org.polina.practice.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/short-info")
    public List<EmployeeProjection> getEmployeesShortInfo() {
        return employeeService.getAllEmployeeProjections();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok().body(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok().body(employeeService.getEmployeeById(id));
    }
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody @Valid AddEmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.addEmployee(request));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody @Valid UpsertEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }
}
