package org.polina.practice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.polina.practice.entity.Department;
import org.polina.practice.service.DepartmentService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentControllerTest {
    private MockMvc mockMvc;
    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private Department department1;
    private Department department2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
        department1 = new Department(1L, "Name 1", Collections.emptyList());
        department2 = new Department(2L, "Name 2", Collections.emptyList());
    }

    @Test
    public void whenGetAllDepartments_thenReturnListOfDepartments() throws Exception {
        List<Department> departments = Arrays.asList(department1, department2);
        when(departmentService.getAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/api/department/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Name 1"))
                .andExpect(jsonPath("$[0].employees").isEmpty())
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Name 2"))
                .andExpect(jsonPath("$[1].employees").isEmpty());


        verify(departmentService, times(1)).getAllDepartments();
    }
    @Test
    public void whenGetDepartmentById_thenReturnDepartment() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(department1);

        mockMvc.perform(get("/api/department/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Name 1"))
                .andExpect(jsonPath("$.employees").isEmpty());

        verify(departmentService, times(1)).getDepartmentById(1L);
    }
    @Test
    public void whenAddDepartment_thenReturnAddedDepartment() throws Exception {
        when(departmentService.addDepartment(any(Department.class))).thenReturn(department1);

        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Name 1"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Name 1"))
                .andExpect(jsonPath("$.employees").isEmpty());

        verify(departmentService, times(1)).addDepartment(any(Department.class));
    }
    @Test
    public void whenUpdateDepartment_thenReturnUpdatedDepartment() throws Exception {
        Department updatedDepartment = new Department(1L, "Updated Name 1", new ArrayList<>());
        when(departmentService.updateDepartment(eq(1L), any(Department.class))).thenReturn(updatedDepartment);

        mockMvc.perform(put("/api/department/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Updated Name 1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Name 1"))
                .andExpect(jsonPath("$.employees").isEmpty());

        verify(departmentService, times(1)).updateDepartment(eq(1L), any(Department.class));
    }
    @Test
    public void whenDeleteDepartmentById_thenReturnNoContent() throws Exception {
        doNothing().when(departmentService).deleteDepartmentById(1L);

        mockMvc.perform(delete("/api/department/1"))
                .andExpect(status().isNoContent());

        verify(departmentService, times(1)).deleteDepartmentById(1L);
    }
}

