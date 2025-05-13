package org.polina.practice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.polina.practice.dto.AddEmployeeRequest;
import org.polina.practice.dto.UpsertEmployeeRequest;
import org.polina.practice.entity.Employee;
import org.polina.practice.projection.EmployeeProjection;
import org.polina.practice.service.EmployeeService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();

        employee1 = new Employee(1L, "First Name 1", "Last Name 1", "Developer", BigDecimal.valueOf(50000), null);
        employee2 = new Employee(2L, "First Name 2", "Last Name 2", "Manager", BigDecimal.valueOf(70000), null);
    }

    @Test
    public void whenGetEmployeesShortInfo_thenReturnListOfProjections() throws Exception {
        List<EmployeeProjection> projections = Arrays.asList(
                new EmployeeProjection() {
                    public String getFullName() {
                        return "First Name 1 Last Name 1";
                    }
                    public String getPosition() {
                        return "Developer";
                    }
                    public String getDepartmentName() {
                        return "IT";
                    }
                },
                new EmployeeProjection() {
                    public String getFullName() {
                        return "First Name 2 Last Name 2";
                    }
                    public String getPosition() {
                        return "Manager";
                    }
                    public String getDepartmentName() {
                        return "HR";
                    }
                }
        );

        when(employeeService.getAllEmployeeProjections()).thenReturn(projections);

        mockMvc.perform(get("/api/employee/short-info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fullName").value("First Name 1 Last Name 1"))
                .andExpect(jsonPath("$[0].position").value("Developer"))
                .andExpect(jsonPath("$[0].departmentName").value("IT"))
                .andExpect(jsonPath("$[1].fullName").value("First Name 2 Last Name 2"))
                .andExpect(jsonPath("$[1].position").value("Manager"))
                .andExpect(jsonPath("$[1].departmentName").value("HR"));

        verify(employeeService, times(1)).getAllEmployeeProjections();
    }

    @Test
    public void whenGetAllEmployees_thenReturnListOfEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(employee1, employee2);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employee/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("First Name 1"))
                .andExpect(jsonPath("$[0].lastName").value("Last Name 1"))
                .andExpect(jsonPath("$[0].position").value("Developer"))
                .andExpect(jsonPath("$[0].salary").value(50000))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("First Name 2"))
                .andExpect(jsonPath("$[1].lastName").value("Last Name 2"))
                .andExpect(jsonPath("$[1].position").value("Manager"))
                .andExpect(jsonPath("$[1].salary").value(70000));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    public void whenGetEmployeeById_thenReturnEmployee() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(employee1);

        mockMvc.perform(get("/api/employee/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("First Name 1"))
                .andExpect(jsonPath("$.lastName").value("Last Name 1"))
                .andExpect(jsonPath("$.position").value("Developer"))
                .andExpect(jsonPath("$.salary").value(50000));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    public void whenAddEmployee_thenReturnAddedEmployee() throws Exception {
        AddEmployeeRequest request = new AddEmployeeRequest("First Name 1", "Last Name 1", "Developer", BigDecimal.valueOf(50000), 1L);

        when(employeeService.addEmployee(any(AddEmployeeRequest.class))).thenReturn(employee1);

        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("First Name 1"))
                .andExpect(jsonPath("$.lastName").value("Last Name 1"))
                .andExpect(jsonPath("$.position").value("Developer"))
                .andExpect(jsonPath("$.salary").value(50000));

        verify(employeeService, times(1)).addEmployee(any(AddEmployeeRequest.class));
    }

    @Test
    public void whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        UpsertEmployeeRequest request = new UpsertEmployeeRequest("Updated First Name", "Updated Last Name", "Senior Developer", BigDecimal.valueOf(200000));

        Employee updatedEmployee = new Employee(1L, "Updated First Name", "Updated Last Name", "Senior Developer", BigDecimal.valueOf(200000), null);

        when(employeeService.updateEmployee(eq(1L), any(UpsertEmployeeRequest.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Updated First Name"))
                .andExpect(jsonPath("$.lastName").value("Updated Last Name"))
                .andExpect(jsonPath("$.position").value("Senior Developer"))
                .andExpect(jsonPath("$.salary").value(200000));

        verify(employeeService, times(1)).updateEmployee(eq(1L), any(UpsertEmployeeRequest.class));
    }

    @Test
    public void whenDeleteEmployeeById_thenReturnNoContent() throws Exception {
        doNothing().when(employeeService).deleteEmployeeById(1L);

        mockMvc.perform(delete("/api/employee/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployeeById(1L);
    }
}