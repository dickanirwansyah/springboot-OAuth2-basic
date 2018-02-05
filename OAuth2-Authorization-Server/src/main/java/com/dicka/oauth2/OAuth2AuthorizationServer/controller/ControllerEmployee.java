package com.dicka.oauth2.OAuth2AuthorizationServer.controller;

import com.dicka.oauth2.OAuth2AuthorizationServer.model.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Controller
public class ControllerEmployee {

    @RequestMapping(value = "/user/getEmployeeList", produces = "application/json")
    @ResponseBody
    public List<Employee> getEmployeeList(){
        List<Employee> employees = new ArrayList<>();
        Employee emp1 = new Employee();
        emp1.setEmpId("emp-001");
        emp1.setEmpName("Muhammad Dicka Nirwansyah");
        employees.add(emp1);

        return employees;
    }
}
