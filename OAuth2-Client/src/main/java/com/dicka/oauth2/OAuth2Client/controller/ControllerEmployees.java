package com.dicka.oauth2.OAuth2Client.controller;


import com.dicka.oauth2.OAuth2Client.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

@Controller
public class ControllerEmployees {

    @RequestMapping(value = "/getEmployees", method = RequestMethod.GET)
    public ModelAndView getEmployees(){
        ModelAndView view = new ModelAndView();
        view.setViewName("getEmployees");
        return view;
    }

    @RequestMapping(value = "/showEmployees", method = RequestMethod.GET)
    public ModelAndView showEmployees(@RequestParam(value = "code")String code)
    throws JsonProcessingException, IOException{

        ResponseEntity<String> response = null;
        System.out.println("Authorization Code----"+ code);

        RestTemplate restTemplate = new RestTemplate();

        //Mengkonvert client credential dan secret ke base 64
        String credentials = "appdicka:secret";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic "+ encodedCredentials);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        //access token
        String access_token_url = "http://localhost:8080/oauth/token";
        access_token_url += "?code=" + code;
        access_token_url += "&grant_type=authorization_code";
        access_token_url += "&redirect_uri=http://localhost:8081/showEmployees";

        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
        System.out.println("Access Token Response ---- "+response.getBody());

        //get object employee setelah mendapatkan token
        ObjectMapper mapper = new ObjectMapper();
        JsonNode parseJson = mapper.readTree(response.getBody());
        String token = parseJson.path("access_token").asText();

        String url = "http://localhost:8080/user/getEmployeeList";

        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers1);

        ResponseEntity<Employee[]> employeList = restTemplate
                .exchange(url, HttpMethod.GET, entity, Employee[].class);
        System.out.println(employeList);
        Employee[] employeeListArray = employeList.getBody();

        ModelAndView view = new ModelAndView();
        view.setViewName("showEmployees");
        view.addObject("title", "Data Employee OAuth2");
        view.addObject("listEmp", Arrays.asList(employeeListArray));
        return view;
    }
}
