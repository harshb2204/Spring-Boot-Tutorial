package com.example.cachingspringboot.services.impl;

import com.example.cachingspringboot.dto.EmployeeDto;
import com.example.cachingspringboot.entities.Employee;
import com.example.cachingspringboot.repositories.EmployeeRepository;
import com.example.cachingspringboot.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class EmployeeServiceImpl implements EmployeeService {

     private final EmployeeRepository employeeRepository;
     private final ModelMapper modelMapper;

     @Override

     public EmployeeDto getEmployeeById(Long id) {
          Employee employee = employeeRepository.findById(id)
                  .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
          return modelMapper.map(employee, EmployeeDto.class);
     }

     @Override

     public EmployeeDto createNewEmployee(EmployeeDto employeeDto) {
          Employee employee = modelMapper.map(employeeDto, Employee.class);
          Employee savedEmployee = employeeRepository.save(employee);
          return modelMapper.map(savedEmployee, EmployeeDto.class);
     }

     @Override

     public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
          Employee existingEmployee = employeeRepository.findById(id)
                  .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

          existingEmployee.setName(employeeDto.getName());
          existingEmployee.setEmail(employeeDto.getEmail());
          existingEmployee.setSalary(employeeDto.getSalary());

          Employee updatedEmployee = employeeRepository.save(existingEmployee);
          return modelMapper.map(updatedEmployee, EmployeeDto.class);
     }

     @Override
     @Transactional
     public void deleteEmployee(Long id) {
          Employee employee = employeeRepository.findById(id)
                  .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
          employeeRepository.delete(employee);
     }
}
