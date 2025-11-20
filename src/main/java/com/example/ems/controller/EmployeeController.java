package com.example.ems.controller;

import com.example.ems.model.Employee;
import com.example.ems.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("listEmployees", service.getAllEmployees());
        return "index";
    }

    @GetMapping("/showNewEmployeeForm")
    public String showNewEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "new_employee";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        service.saveEmployee(employee);
        return "redirect:/";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        List<Employee> employees = service.getAllEmployees();
        model.addAttribute("listEmployees", employees);

        // Department count for pie chart
        Map<String, Long> deptCount = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
        model.addAttribute("deptCount", deptCount);

        // Salaries list for bar chart
        List<Double> salaries = employees.stream()
                .map(Employee::getSalary)
                .collect(Collectors.toList());
        model.addAttribute("salaries", salaries);

        // Average salary (never null)
        double avgSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0);
        model.addAttribute("avgSalary", avgSalary);

        // Department-wise average salary
        Map<String, Double> deptAvgSalary = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)));
        model.addAttribute("deptAvgSalary", deptAvgSalary);

        return "dashboard";
    }
}
